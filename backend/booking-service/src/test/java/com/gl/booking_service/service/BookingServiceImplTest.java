package com.gl.booking_service.service;

import com.gl.booking_service.client.ResourceClient;
import com.gl.booking_service.client.TransactionClient;
import com.gl.booking_service.client.UserClient;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.dto.BookingResponse;
import com.gl.booking_service.dto.ResourceResponse;
import com.gl.booking_service.dto.TransactionRequest;
import com.gl.booking_service.dto.UserResponse;

import com.gl.booking_service.entity.Booking;
import com.gl.booking_service.entity.BookingStatus;
import com.gl.booking_service.entity.ExtensionStatus;

import com.gl.booking_service.repository.BookingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {


    @Mock
    private BookingRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private ResourceClient resourceClient;

    @Mock
    private TransactionClient transactionClient;


    @InjectMocks
    private BookingServiceImpl bookingService;


    private Booking booking;

    private ResourceResponse resource;


    // =================================================
    // SETUP
    // =================================================

    @BeforeEach
    void setUp() {

        booking = Booking.builder()
                .bookingId(1L)
                .userId(10L)
                .ownerId(20L)
                .resourceId(100L)
                .rentalDays(5)
                .status(BookingStatus.PENDING)
                .build();


        resource = new ResourceResponse();

        resource.setResourceId(100L);
        resource.setOwnerId(20L);
        resource.setTitle("Camera");
        resource.setAvailable(true);
        resource.setRentPerDay(500.0);
        resource.setSecurityDeposit(2000.0);
    }


    // =================================================
    // TEST 1
    // CREATE BOOKING SUCCESS
    // =================================================

    @Test
    void createBookingSuccessfully() {

        BookingRequest request =
                new BookingRequest();

        request.setResourceId(100L);
        request.setRentalDays(5);


        when(
                resourceClient.getResourceById(100L)
        ).thenReturn(resource);


        when(
                repository.save(any(Booking.class))
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Booking result =
                bookingService.createBooking(
                        request,
                        10L
                );


        assertNotNull(result);

        assertEquals(
                10L,
                result.getUserId()
        );

        assertEquals(
                20L,
                result.getOwnerId()
        );

        assertEquals(
                100L,
                result.getResourceId()
        );

        assertEquals(
                5,
                result.getRentalDays()
        );

        assertEquals(
                BookingStatus.PENDING,
                result.getStatus()
        );


        verify(repository)
                .save(any(Booking.class));
    }


    // =================================================
    // TEST 2
    // RESOURCE ID REQUIRED
    // =================================================

    @Test
    void createBookingShouldFailWhenResourceIdIsNull() {

        BookingRequest request =
                new BookingRequest();

        request.setResourceId(null);
        request.setRentalDays(5);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .createBooking(
                                                request,
                                                10L
                                        )
                );


        assertEquals(
                "Resource ID is required.",
                exception.getMessage()
        );


        verifyNoInteractions(
                resourceClient
        );
    }


    // =================================================
    // TEST 3
    // INVALID RENTAL DAYS
    // =================================================

    @Test
    void createBookingShouldFailForInvalidRentalDays() {

        BookingRequest request =
                new BookingRequest();

        request.setResourceId(100L);
        request.setRentalDays(0);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .createBooking(
                                                request,
                                                10L
                                        )
                );


        assertEquals(
                "Rental days must be at least 1.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 4
    // RESOURCE NOT FOUND
    // =================================================

    @Test
    void createBookingShouldFailWhenResourceNotFound() {

        BookingRequest request =
                new BookingRequest();

        request.setResourceId(100L);
        request.setRentalDays(5);


        when(
                resourceClient.getResourceById(100L)
        ).thenReturn(null);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .createBooking(
                                                request,
                                                10L
                                        )
                );


        assertEquals(
                "Resource not found.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 5
    // RESOURCE UNAVAILABLE
    // =================================================

    @Test
    void createBookingShouldFailWhenResourceUnavailable() {

        resource.setAvailable(false);


        BookingRequest request =
                new BookingRequest();

        request.setResourceId(100L);
        request.setRentalDays(5);


        when(
                resourceClient.getResourceById(100L)
        ).thenReturn(resource);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .createBooking(
                                                request,
                                                10L
                                        )
                );


        assertEquals(
                "Resource is currently unavailable.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 6
    // CANNOT BORROW OWN RESOURCE
    // =================================================

    @Test
    void createBookingShouldFailWhenBorrowingOwnResource() {

        resource.setOwnerId(10L);


        BookingRequest request =
                new BookingRequest();

        request.setResourceId(100L);
        request.setRentalDays(5);


        when(
                resourceClient.getResourceById(100L)
        ).thenReturn(resource);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .createBooking(
                                                request,
                                                10L
                                        )
                );


        assertEquals(
                "You cannot borrow your own resource.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 7
    // GET MY BOOKINGS - APPROVED
    // OWNER DETAILS SHOULD BE PRESENT
    // =================================================

    @Test
    void getMyBookingsShouldIncludeOwnerDetailsWhenApproved() {

        booking.setStatus(
                BookingStatus.APPROVED
        );


        UserResponse owner =
                new UserResponse();

        owner.setFullName("Resource Owner");
        owner.setEmail("owner@gmail.com");
        owner.setPhone("9876543210");


        when(
                repository.findByUserId(10L)
        ).thenReturn(
                List.of(booking)
        );


        when(
                userClient.getUserById(20L)
        ).thenReturn(owner);


        List<BookingResponse> result =
                bookingService
                        .getMyBookings(10L);


        assertEquals(
                1,
                result.size()
        );


        BookingResponse response =
                result.get(0);


        assertEquals(
                "Resource Owner",
                response.getOwnerName()
        );

        assertEquals(
                "owner@gmail.com",
                response.getOwnerEmail()
        );

        assertEquals(
                "9876543210",
                response.getOwnerPhone()
        );
    }


    // =================================================
    // TEST 8
    // PENDING BOOKING SHOULD NOT EXPOSE OWNER DETAILS
    // =================================================

    @Test
    void getMyBookingsShouldNotFetchOwnerWhenPending() {

        booking.setStatus(
                BookingStatus.PENDING
        );


        when(
                repository.findByUserId(10L)
        ).thenReturn(
                List.of(booking)
        );


        List<BookingResponse> result =
                bookingService
                        .getMyBookings(10L);


        assertEquals(
                1,
                result.size()
        );


        assertNull(
                result.get(0)
                        .getOwnerName()
        );


        verify(
                userClient,
                never()
        ).getUserById(anyLong());
    }


    // =================================================
    // TEST 9
    // OWNER RECEIVES RENTER DETAILS
    // =================================================

    @Test
    void getReceivedRequestsShouldIncludeRenterDetails() {

        UserResponse renter =
                new UserResponse();

        renter.setFullName(
                "Borrower User"
        );

        renter.setEmail(
                "borrower@gmail.com"
        );

        renter.setPhone(
                "9999999999"
        );


        when(
                repository.findByOwnerId(20L)
        ).thenReturn(
                List.of(booking)
        );


        when(
                userClient.getUserById(10L)
        ).thenReturn(renter);


        List<BookingResponse> result =
                bookingService
                        .getReceivedRequests(
                                20L
                        );


        assertEquals(
                1,
                result.size()
        );


        assertEquals(
                "Borrower User",
                result.get(0)
                        .getRenterName()
        );

        assertEquals(
                "borrower@gmail.com",
                result.get(0)
                        .getRenterEmail()
        );

        assertEquals(
                "9999999999",
                result.get(0)
                        .getRenterPhone()
        );
    }


    // =================================================
    // TEST 10
    // CANCEL BOOKING SUCCESS
    // =================================================

    @Test
    void cancelBookingSuccessfully() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService.cancelBooking(
                        1L,
                        10L
                );


        assertEquals(
                BookingStatus.CANCELLED,
                result.getStatus()
        );


        verify(repository)
                .save(booking);
    }


    // =================================================
    // TEST 11
    // DIFFERENT USER CANNOT CANCEL
    // =================================================

    @Test
    void cancelBookingShouldFailForUnauthorizedUser() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .cancelBooking(
                                                1L,
                                                99L
                                        )
                );


        assertEquals(
                "You are not authorized to cancel this booking.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 12
    // UPDATE RENTAL DAYS
    // =================================================

    @Test
    void updateRentalDaysSuccessfully() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService
                        .updateRentalDays(
                                1L,
                                10L,
                                8
                        );


        assertEquals(
                8,
                result.getRentalDays()
        );


        verify(repository)
                .save(booking);
    }


    // =================================================
    // TEST 13
    // CANNOT UPDATE APPROVED BOOKING
    // =================================================

    @Test
    void updateRentalDaysShouldFailWhenBookingNotPending() {

        booking.setStatus(
                BookingStatus.APPROVED
        );


        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .updateRentalDays(
                                                1L,
                                                10L,
                                                8
                                        )
                );


        assertEquals(
                "Only pending bookings can be updated.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 14
    // APPROVE BOOKING
    // =================================================

    @Test
    void approveBookingSuccessfullyAndCreateTransaction() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                resourceClient.getResourceById(100L)
        ).thenReturn(resource);


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService
                        .approveBooking(
                                1L,
                                20L
                        );


        assertEquals(
                BookingStatus.APPROVED,
                result.getStatus()
        );


        verify(
                transactionClient,
                times(1)
        ).createTransaction(
                any(TransactionRequest.class)
        );
    }


    // =================================================
    // TEST 15
    // VERIFY TRANSACTION DATA AFTER APPROVAL
    // =================================================

    @Test
    void approveBookingShouldSendCorrectTransactionData() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                resourceClient.getResourceById(100L)
        ).thenReturn(resource);


        when(
                repository.save(booking)
        ).thenReturn(booking);


        bookingService.approveBooking(
                1L,
                20L
        );


        ArgumentCaptor<TransactionRequest> captor =
                ArgumentCaptor.forClass(
                        TransactionRequest.class
                );


        verify(
                transactionClient
        ).createTransaction(
                captor.capture()
        );


        TransactionRequest request =
                captor.getValue();


        assertEquals(
                1L,
                request.getBookingId()
        );

        assertEquals(
                100L,
                request.getResourceId()
        );

        assertEquals(
                10L,
                request.getRenterId()
        );

        assertEquals(
                20L,
                request.getOwnerId()
        );

        assertEquals(
                500.0,
                request.getRentPerDay()
        );

        assertEquals(
                2000.0,
                request.getSecurityDeposit()
        );

        assertNotNull(
                request.getBookingDate()
        );

        assertNotNull(
                request.getExpectedReturnDate()
        );

        assertEquals(
                request.getBookingDate()
                        .plusDays(5),
                request.getExpectedReturnDate()
        );
    }


    // =================================================
    // TEST 16
    // WRONG OWNER CANNOT APPROVE
    // =================================================

    @Test
    void approveBookingShouldFailForWrongOwner() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .approveBooking(
                                                1L,
                                                99L
                                        )
                );


        assertEquals(
                "You are not authorized to approve this booking.",
                exception.getMessage()
        );


        verifyNoInteractions(
                transactionClient
        );
    }


    // =================================================
    // TEST 17
    // REJECT BOOKING
    // =================================================

    @Test
    void rejectBookingSuccessfully() {

        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService
                        .rejectBooking(
                                1L,
                                20L
                        );


        assertEquals(
                BookingStatus.REJECTED,
                result.getStatus()
        );
    }


    // =================================================
    // TEST 18
    // REQUEST EXTENSION
    // =================================================

    @Test
    void requestExtensionSuccessfully() {

        booking.setStatus(
                BookingStatus.APPROVED
        );


        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService
                        .requestExtension(
                                1L,
                                10L,
                                10
                        );


        assertEquals(
                10,
                result.getRequestedRentalDays()
        );

        assertEquals(
                ExtensionStatus.PENDING,
                result.getExtensionStatus()
        );
    }


    // =================================================
    // TEST 19
    // EXTENSION MUST BE GREATER
    // =================================================

    @Test
    void requestExtensionShouldFailWhenDaysNotGreater() {

        booking.setStatus(
                BookingStatus.APPROVED
        );


        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .requestExtension(
                                                1L,
                                                10L,
                                                5
                                        )
                );


        assertEquals(
                "New rental duration must be greater than current rental duration.",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 20
    // APPROVE EXTENSION
    // =================================================

    @Test
    void approveExtensionSuccessfully() {

        booking.setStatus(
                BookingStatus.APPROVED
        );

        booking.setRequestedRentalDays(
                10
        );

        booking.setExtensionStatus(
                ExtensionStatus.PENDING
        );


        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService
                        .approveExtension(
                                1L,
                                20L
                        );


        assertEquals(
                10,
                result.getRentalDays()
        );

        assertNull(
                result.getRequestedRentalDays()
        );

        assertEquals(
                ExtensionStatus.APPROVED,
                result.getExtensionStatus()
        );
    }


    // =================================================
    // TEST 21
    // REJECT EXTENSION
    // =================================================

    @Test
    void rejectExtensionSuccessfully() {

        booking.setStatus(
                BookingStatus.APPROVED
        );

        booking.setRequestedRentalDays(
                10
        );

        booking.setExtensionStatus(
                ExtensionStatus.PENDING
        );


        when(
                repository.findById(1L)
        ).thenReturn(
                Optional.of(booking)
        );


        when(
                repository.save(booking)
        ).thenReturn(booking);


        Booking result =
                bookingService
                        .rejectExtension(
                                1L,
                                20L
                        );


        assertNull(
                result.getRequestedRentalDays()
        );

        assertEquals(
                ExtensionStatus.REJECTED,
                result.getExtensionStatus()
        );
    }


    // =================================================
    // TEST 22
    // BOOKING NOT FOUND
    // =================================================

    @Test
    void cancelBookingShouldFailWhenBookingNotFound() {

        when(
                repository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                bookingService
                                        .cancelBooking(
                                                99L,
                                                10L
                                        )
                );


        assertEquals(
                "Booking not found.",
                exception.getMessage()
        );
    }
}