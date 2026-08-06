package com.gl.resource_service.service;


import com.gl.resource_service.dto.*;
import com.gl.resource_service.entity.Resource;
import com.gl.resource_service.exception.ResourceNotFoundException;
import com.gl.resource_service.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;


    @Override
    public ResourceResponse addResource(
            ResourceRequest request,
            Long ownerId) {

        Resource resource = Resource.builder()
                .ownerId(ownerId)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .rentPerDay(request.getRentPerDay())
                .securityDeposit(request.getSecurityDeposit())
                .quantity(request.getQuantity())
                .condition(request.getCondition())
                .available(true)
                .city(request.getCity())
                .state(request.getState())
                .imageUrl(request.getImageUrl())
                .createdDate(LocalDate.now())
                .build();

        return convert(repository.save(resource));
    }

    @Override
    public ResourceResponse getResourceById(Long id) {

        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource not found with id : " + id
                ));

        return convert(resource);
    }

    @Override
    public List<ResourceResponse> getAllResources(Long userId) {

        return repository
                .findByOwnerIdNotAndAvailableTrue(userId)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<ResourceResponse> getResourcesByOwner(Long ownerId) {

        return repository.findByOwnerId(ownerId)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public ResourceResponse updateResource(
            Long id,
            ResourceUpdateRequest request) {

        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource not found with id : " + id
                ));

        resource.setTitle(request.getTitle());

        resource.setDescription(request.getDescription());

        resource.setCategory(request.getCategory());

        resource.setRentPerDay(request.getRentPerDay());

        resource.setSecurityDeposit(request.getSecurityDeposit());

        resource.setQuantity(request.getQuantity());

        resource.setCondition(request.getCondition());

        resource.setAvailable(request.getAvailable());

        resource.setCity(request.getCity());

        resource.setState(request.getState());

        resource.setImageUrl(request.getImageUrl());

        return convert(repository.save(resource));
    }

    @Override
    public void deleteResource(Long id) {

        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource not found"
                ));

        repository.delete(resource);
    }

    private ResourceResponse convert(Resource resource) {




        return ResourceResponse.builder()

                .resourceId(
                        resource.getResourceId()
                )

                .ownerId(
                        resource.getOwnerId()
                )

                .title(
                        resource.getTitle()
                )

                .description(
                        resource.getDescription()
                )

                .category(
                        resource.getCategory()
                )

                .rentPerDay(
                        resource.getRentPerDay()
                )

                .securityDeposit(
                        resource.getSecurityDeposit()
                )

                .quantity(
                        resource.getQuantity()
                )

                .condition(
                        resource.getCondition()
                )

                .available(
                        resource.getAvailable()
                )

                .city(
                        resource.getCity()
                )

                .state(
                        resource.getState()
                )

                .imageUrl(
                        resource.getImageUrl()
                )

                .createdDate(
                        resource.getCreatedDate()
                )


                // OWNER DETAILS



                .build();
    }
}