package com.gl.resource_service.service;



import com.gl.resource_service.dto.ResourceRequest;
import com.gl.resource_service.dto.ResourceResponse;
import com.gl.resource_service.dto.ResourceUpdateRequest;

import com.gl.resource_service.entity.Condition;
import com.gl.resource_service.entity.Resource;

import com.gl.resource_service.exception.ResourceNotFoundException;

import com.gl.resource_service.repository.ResourceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {


    @Mock
    private ResourceRepository repository;


    @InjectMocks
    private ResourceServiceImpl resourceService;


    private Resource resource;


    // =================================================
    // SETUP
    // =================================================

    @BeforeEach
    void setUp() {

        resource = Resource.builder()

                .resourceId(1L)

                .ownerId(10L)

                .title("Camera")

                .description("DSLR Camera")

                .category("Electronics")

                .rentPerDay(500.0)

                .securityDeposit(2000.0)

                .quantity(1)

                .condition(Condition.GOOD)

                .available(true)

                .city("Ghaziabad")

                .state("Uttar Pradesh")

                .imageUrl("camera.jpg")

                .createdDate(LocalDate.now())

                .build();
    }


    // =================================================
    // TEST 1
    // ADD RESOURCE
    // =================================================

    @Test
    void addResourceSuccessfully() {

        ResourceRequest request =
                new ResourceRequest();

        request.setTitle("Camera");

        request.setDescription(
                "DSLR Camera"
        );

        request.setCategory(
                "Electronics"
        );

        request.setRentPerDay(
                500.0
        );

        request.setSecurityDeposit(
                2000.0
        );

        request.setQuantity(
                1
        );

        request.setCondition(
                Condition.GOOD
        );

        request.setCity(
                "Ghaziabad"
        );

        request.setState(
                "Uttar Pradesh"
        );

        request.setImageUrl(
                "camera.jpg"
        );


        when(
                repository.save(
                        any(Resource.class)
                )
        ).thenReturn(resource);


        ResourceResponse response =
                resourceService.addResource(
                        request,
                        10L
                );


        assertNotNull(response);

        assertEquals(
                1L,
                response.getResourceId()
        );

        assertEquals(
                10L,
                response.getOwnerId()
        );

        assertEquals(
                "Camera",
                response.getTitle()
        );

        assertEquals(
                "Electronics",
                response.getCategory()
        );

        assertEquals(
                500.0,
                response.getRentPerDay()
        );

        assertEquals(
                2000.0,
                response.getSecurityDeposit()
        );

        assertTrue(
                response.getAvailable()
        );


        verify(
                repository,
                times(1)
        ).save(
                any(Resource.class)
        );
    }


    // =================================================
    // TEST 2
    // VERIFY NEW RESOURCE IS AVAILABLE
    // =================================================

    @Test
    void addResourceShouldSetAvailableTrue() {

        ResourceRequest request =
                new ResourceRequest();

        request.setTitle("Laptop");
        request.setDescription("Gaming Laptop");
        request.setCategory("Electronics");
        request.setRentPerDay(800.0);
        request.setSecurityDeposit(3000.0);
        request.setQuantity(1);
        request.setCondition(Condition.GOOD);
        request.setCity("Delhi");
        request.setState("Delhi");
        request.setImageUrl("laptop.jpg");


        when(
                repository.save(
                        any(Resource.class)
                )
        ).thenAnswer(invocation -> {

            Resource saved =
                    invocation.getArgument(0);

            saved.setResourceId(2L);

            return saved;
        });


        ResourceResponse response =
                resourceService.addResource(
                        request,
                        10L
                );


        assertTrue(
                response.getAvailable()
        );

        assertNotNull(
                response.getCreatedDate()
        );
    }


    // =================================================
    // TEST 3
    // GET RESOURCE BY ID
    // =================================================

    @Test
    void getResourceByIdSuccessfully() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(resource)
        );


        ResourceResponse response =
                resourceService
                        .getResourceById(1L);


        assertNotNull(response);

        assertEquals(
                1L,
                response.getResourceId()
        );

        assertEquals(
                "Camera",
                response.getTitle()
        );

        assertEquals(
                "DSLR Camera",
                response.getDescription()
        );

        assertEquals(
                "Electronics",
                response.getCategory()
        );

        assertEquals(
                10L,
                response.getOwnerId()
        );
    }


    // =================================================
    // TEST 4
    // RESOURCE NOT FOUND
    // =================================================

    @Test
    void getResourceByIdShouldThrowExceptionWhenNotFound() {

        when(
                repository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                resourceService
                                        .getResourceById(
                                                99L
                                        )
                );


        assertEquals(
                "Resource not found with id : 99",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 5
    // GET ALL AVAILABLE RESOURCES EXCEPT OWN
    // =================================================

    @Test
    void getAllResourcesSuccessfully() {

        Resource secondResource =
                Resource.builder()

                        .resourceId(2L)

                        .ownerId(20L)

                        .title("Laptop")

                        .description(
                                "Gaming Laptop"
                        )

                        .category(
                                "Electronics"
                        )

                        .rentPerDay(
                                800.0
                        )

                        .securityDeposit(
                                3000.0
                        )

                        .quantity(1)

                        .condition(
                                Condition.GOOD
                        )

                        .available(true)

                        .city("Delhi")

                        .state("Delhi")

                        .imageUrl(
                                "laptop.jpg"
                        )

                        .createdDate(
                                LocalDate.now()
                        )

                        .build();


        when(
                repository
                        .findByOwnerIdNotAndAvailableTrue(
                                30L
                        )
        ).thenReturn(
                List.of(
                        resource,
                        secondResource
                )
        );


        List<ResourceResponse> responses =
                resourceService
                        .getAllResources(
                                30L
                        );


        assertNotNull(responses);

        assertEquals(
                2,
                responses.size()
        );

        assertEquals(
                "Camera",
                responses.get(0)
                        .getTitle()
        );

        assertEquals(
                "Laptop",
                responses.get(1)
                        .getTitle()
        );
    }


    // =================================================
    // TEST 6
    // GET ALL RESOURCES - EMPTY
    // =================================================

    @Test
    void getAllResourcesShouldReturnEmptyList() {

        when(
                repository
                        .findByOwnerIdNotAndAvailableTrue(
                                10L
                        )
        ).thenReturn(
                List.of()
        );


        List<ResourceResponse> responses =
                resourceService
                        .getAllResources(
                                10L
                        );


        assertNotNull(responses);

        assertTrue(
                responses.isEmpty()
        );
    }


    // =================================================
    // TEST 7
    // GET RESOURCES BY OWNER
    // =================================================

    @Test
    void getResourcesByOwnerSuccessfully() {

        when(
                repository.findByOwnerId(
                        10L
                )
        ).thenReturn(
                List.of(resource)
        );


        List<ResourceResponse> responses =
                resourceService
                        .getResourcesByOwner(
                                10L
                        );


        assertEquals(
                1,
                responses.size()
        );

        assertEquals(
                10L,
                responses.get(0)
                        .getOwnerId()
        );

        assertEquals(
                "Camera",
                responses.get(0)
                        .getTitle()
        );
    }


    // =================================================
    // TEST 8
    // UPDATE RESOURCE
    // =================================================

    @Test
    void updateResourceSuccessfully() {

        ResourceUpdateRequest request =
                new ResourceUpdateRequest();

        request.setTitle(
                "Updated Camera"
        );

        request.setDescription(
                "Updated DSLR Camera"
        );

        request.setCategory(
                "Photography"
        );

        request.setRentPerDay(
                600.0
        );

        request.setSecurityDeposit(
                2500.0
        );

        request.setQuantity(
                2
        );

        request.setCondition(
                Condition.GOOD
        );

        request.setAvailable(
                true
        );

        request.setCity(
                "Delhi"
        );

        request.setState(
                "Delhi"
        );

        request.setImageUrl(
                "updated-camera.jpg"
        );


        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(resource)
        );


        when(
                repository.save(
                        any(Resource.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        ResourceResponse response =
                resourceService
                        .updateResource(
                                1L,
                                request
                        );


        assertNotNull(response);

        assertEquals(
                "Updated Camera",
                response.getTitle()
        );

        assertEquals(
                "Updated DSLR Camera",
                response.getDescription()
        );

        assertEquals(
                "Photography",
                response.getCategory()
        );

        assertEquals(
                600.0,
                response.getRentPerDay()
        );

        assertEquals(
                2500.0,
                response.getSecurityDeposit()
        );

        assertEquals(
                2,
                response.getQuantity()
        );

        assertEquals(
                "Delhi",
                response.getCity()
        );

        assertEquals(
                "updated-camera.jpg",
                response.getImageUrl()
        );


        verify(
                repository,
                times(1)
        ).save(resource);
    }


    // =================================================
    // TEST 9
    // UPDATE RESOURCE - NOT FOUND
    // =================================================

    @Test
    void updateResourceShouldThrowExceptionWhenNotFound() {

        ResourceUpdateRequest request =
                new ResourceUpdateRequest();


        when(
                repository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                resourceService
                                        .updateResource(
                                                99L,
                                                request
                                        )
                );


        assertEquals(
                "Resource not found with id : 99",
                exception.getMessage()
        );


        verify(
                repository,
                never()
        ).save(
                any(Resource.class)
        );
    }


    // =================================================
    // TEST 10
    // DELETE RESOURCE
    // =================================================

    @Test
    void deleteResourceSuccessfully() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(resource)
        );


        resourceService.deleteResource(
                1L
        );


        verify(
                repository,
                times(1)
        ).delete(resource);
    }


    // =================================================
    // TEST 11
    // DELETE RESOURCE - NOT FOUND
    // =================================================

    @Test
    void deleteResourceShouldThrowExceptionWhenNotFound() {

        when(
                repository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                resourceService
                                        .deleteResource(
                                                99L
                                        )
                );


        assertEquals(
                "Resource not found",
                exception.getMessage()
        );


        verify(
                repository,
                never()
        ).delete(
                any(Resource.class)
        );
    }


    // =================================================
    // TEST 12
    // VERIFY CONVERT METHOD
    // =================================================

    @Test
    void getResourceShouldMapAllFieldsCorrectly() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(resource)
        );


        ResourceResponse response =
                resourceService
                        .getResourceById(
                                1L
                        );


        assertEquals(
                resource.getResourceId(),
                response.getResourceId()
        );

        assertEquals(
                resource.getOwnerId(),
                response.getOwnerId()
        );

        assertEquals(
                resource.getTitle(),
                response.getTitle()
        );

        assertEquals(
                resource.getDescription(),
                response.getDescription()
        );

        assertEquals(
                resource.getCategory(),
                response.getCategory()
        );

        assertEquals(
                resource.getRentPerDay(),
                response.getRentPerDay()
        );

        assertEquals(
                resource.getSecurityDeposit(),
                response.getSecurityDeposit()
        );

        assertEquals(
                resource.getQuantity(),
                response.getQuantity()
        );

        assertEquals(
                resource.getCondition(),
                response.getCondition()
        );

        assertEquals(
                resource.getAvailable(),
                response.getAvailable()
        );

        assertEquals(
                resource.getCity(),
                response.getCity()
        );

        assertEquals(
                resource.getState(),
                response.getState()
        );

        assertEquals(
                resource.getImageUrl(),
                response.getImageUrl()
        );

        assertEquals(
                resource.getCreatedDate(),
                response.getCreatedDate()
        );
    }
}
