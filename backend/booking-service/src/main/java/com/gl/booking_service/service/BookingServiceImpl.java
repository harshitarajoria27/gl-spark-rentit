package com.gl.booking_service.service;

import com.gl.booking_service.client.ResourceClient;
import com.gl.booking_service.client.TransactionClient;
import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.dto.ResourceResponse;
import com.gl.booking_service.entity.Booking;
import com.gl.booking_service.entity.BookingStatus;
import com.gl.booking_service.entity.ExtensionStatus;
import com.gl.booking_service.repository.BookingRepository;
import com.gl.booking_service.client.TransactionClient;
import com.gl.booking_service.dto.TransactionRequest;
import com.gl.booking_service.client.UserClient;
import com.gl.booking_service.dto.BookingResponse;
import com.gl.booking_service.dto.UserResponse;

import java.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookingServiceImpl
        implements BookingService {


    private final BookingRepository repository;
    private final UserClient userClient;
    private final ResourceClient resourceClient;
    private final TransactionClient transactionClient;

    public BookingServiceImpl(
            BookingRepository repository, UserClient userClient,
            ResourceClient resourceClient, TransactionClient transactionClient
    ) {

        this.repository = repository;
        this.userClient = userClient;

        this.resourceClient = resourceClient;
        this.transactionClient = transactionClient;
    }


    /* ========================================
       CREATE BORROW REQUEST
    ======================================== */

    @Override
    public Booking createBooking(
            BookingRequest request,
            Long userId
    ) {


        if (request.getResourceId() == null) {

            throw new RuntimeException(
                    "Resource ID is required."
            );
        }


        if (request.getRentalDays() == null ||
                request.getRentalDays() < 1) {

            throw new RuntimeException(
                    "Rental days must be at least 1."
            );
        }


        /*
         * Get resource information
         * from Resource Service
         */

        ResourceResponse resource =
                resourceClient.getResourceById(
                        request.getResourceId()
                );


        if (resource == null) {

            throw new RuntimeException(
                    "Resource not found."
            );
        }


        /*
         * Resource must be available
         */

        if (!Boolean.TRUE.equals(
                resource.getAvailable()
        )) {

            throw new RuntimeException(
                    "Resource is currently unavailable."
            );
        }


        /*
         * User cannot borrow their own resource
         */

        if (resource.getOwnerId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You cannot borrow your own resource."
            );
        }


        Booking booking =
                Booking.builder()

                        .userId(userId)

                        .ownerId(
                                resource.getOwnerId()
                        )

                        .resourceId(
                                resource.getResourceId()
                        )

                        .rentalDays(
                                request.getRentalDays()
                        )

                        .status(
                                BookingStatus.PENDING
                        )

                        .build();


        return repository.save(booking);
    }


    /* ========================================
       GET MY BOOKINGS
    ======================================== */

    @Override
    public List<BookingResponse> getMyBookings(
            Long userId
    ) {

        return repository
                .findByUserId(userId)
                .stream()
                .map(booking -> {

                    BookingResponse.BookingResponseBuilder response =
                            BookingResponse.builder()

                                    .bookingId(
                                            booking.getBookingId()
                                    )

                                    .userId(
                                            booking.getUserId()
                                    )

                                    .ownerId(
                                            booking.getOwnerId()
                                    )

                                    .resourceId(
                                            booking.getResourceId()
                                    )

                                    .rentalDays(
                                            booking.getRentalDays()
                                    )

                                    .requestedRentalDays(
                                            booking.getRequestedRentalDays()
                                    )

                                    .extensionStatus(
                                            booking.getExtensionStatus()
                                    )

                                    .status(
                                            booking.getStatus()
                                    );


                    // Owner contact details ONLY after approval

                    if (booking.getStatus()
                            == BookingStatus.APPROVED) {

                        UserResponse owner =
                                userClient.getUserById(
                                        booking.getOwnerId()
                                );

                        response
                                .ownerName(
                                        owner.getFullName()
                                )

                                .ownerEmail(
                                        owner.getEmail()
                                )

                                .ownerPhone(
                                        owner.getPhone()
                                );
                    }


                    return response.build();
                })
                .toList();
    }


    /* ========================================
       GET REQUESTS RECEIVED BY OWNER
    ======================================== */

    @Override
    public List<BookingResponse> getReceivedRequests(
            Long ownerId
    ) {

        return repository
                .findByOwnerId(ownerId)
                .stream()
                .map(booking -> {

                    UserResponse renter =
                            userClient.getUserById(
                                    booking.getUserId()
                            );

                    return BookingResponse.builder()

                            .bookingId(
                                    booking.getBookingId()
                            )

                            .userId(
                                    booking.getUserId()
                            )

                            .ownerId(
                                    booking.getOwnerId()
                            )

                            .resourceId(
                                    booking.getResourceId()
                            )

                            .rentalDays(
                                    booking.getRentalDays()
                            )

                            .requestedRentalDays(
                                    booking.getRequestedRentalDays()
                            )

                            .extensionStatus(
                                    booking.getExtensionStatus()
                            )

                            .status(
                                    booking.getStatus()
                            )


                            // RENTER CONTACT DETAILS

                            .renterName(
                                    renter.getFullName()
                            )

                            .renterEmail(
                                    renter.getEmail()
                            )

                            .renterPhone(
                                    renter.getPhone()
                            )

                            .build();
                })
                .toList();
    }


    /* ========================================
       CANCEL BOOKING
    ======================================== */

    @Override
    public Booking cancelBooking(
            Long bookingId,
            Long userId
    ) {


        Booking booking =
                repository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found."
                                )
                        );


        /*
         * Only borrower can cancel
         */

        if (!booking.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to cancel this booking."
            );
        }


        /*
         * Rejected booking cannot
         * be cancelled
         */

        if (booking.getStatus()
                == BookingStatus.REJECTED) {

            throw new RuntimeException(
                    "Rejected booking cannot be cancelled."
            );
        }


        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new RuntimeException(
                    "Booking is already cancelled."
            );
        }


        booking.setStatus(
                BookingStatus.CANCELLED
        );


        return repository.save(booking);
    }


    /* ========================================
       UPDATE RENTAL DAYS
    ======================================== */

    @Override
    public Booking updateRentalDays(
            Long bookingId,
            Long userId,
            Integer rentalDays
    ) {


        if (rentalDays == null ||
                rentalDays < 1) {

            throw new RuntimeException(
                    "Rental days must be at least 1."
            );
        }


        Booking booking =
                repository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found."
                                )
                        );


        /*
         * Only borrower can update
         */

        if (!booking.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to update this booking."
            );
        }


        /*
         * Only pending requests should
         * be editable
         */

        if (booking.getStatus()
                != BookingStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending bookings can be updated."
            );
        }


        booking.setRentalDays(
                rentalDays
        );


        return repository.save(booking);
    }


    /* ========================================
       APPROVE BOOKING
    ======================================== */

    @Override
    public Booking approveBooking(
            Long bookingId,
            Long ownerId
    ) {

        Booking booking =
                repository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found."
                                )
                        );


        // Only owner can approve

        if (!booking.getOwnerId().equals(ownerId)) {

            throw new RuntimeException(
                    "You are not authorized to approve this booking."
            );
        }


        // Only pending booking can be approved

        if (booking.getStatus()
                != BookingStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending bookings can be approved."
            );
        }


        // Get resource details

        ResourceResponse resource =
                resourceClient.getResourceById(
                        booking.getResourceId()
                );


        if (resource == null) {

            throw new RuntimeException(
                    "Resource not found."
            );
        }


        // Approve booking

        booking.setStatus(
                BookingStatus.APPROVED
        );


        Booking savedBooking =
                repository.save(booking);


        // Create transaction automatically

        LocalDate bookingDate =
                LocalDate.now();

        LocalDate expectedReturnDate =
                bookingDate.plusDays(
                        booking.getRentalDays()
                );


        TransactionRequest transactionRequest =
                TransactionRequest.builder()

                        .bookingId(
                                booking.getBookingId()
                        )

                        .resourceId(
                                booking.getResourceId()
                        )

                        .renterId(
                                booking.getUserId()
                        )

                        .ownerId(
                                booking.getOwnerId()
                        )

                        .rentPerDay(
                                resource.getRentPerDay()
                        )

                        .securityDeposit(
                                resource.getSecurityDeposit()
                        )

                        .bookingDate(
                                bookingDate
                        )

                        .expectedReturnDate(
                                expectedReturnDate
                        )

                        .build();


        transactionClient.createTransaction(
                transactionRequest
        );


        return savedBooking;
    }


    /* ========================================
       REJECT BOOKING
    ======================================== */

    @Override
    public Booking rejectBooking(
            Long bookingId,
            Long ownerId
    ) {


        Booking booking =
                repository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found."
                                )
                        );


        /*
         * Only resource owner
         * can reject
         */

        if (!booking.getOwnerId()
                .equals(ownerId)) {

            throw new RuntimeException(
                    "You are not authorized to reject this booking."
            );
        }


        /*
         * Only pending request
         * can be rejected
         */

        if (booking.getStatus()
                != BookingStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending bookings can be rejected."
            );
        }


        booking.setStatus(
                BookingStatus.REJECTED
        );


        return repository.save(booking);
    }
    @Override
    public Booking requestExtension(
            Long bookingId,
            Long userId,
            Integer rentalDays
    ) {

        Booking booking = repository
                .findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );


        // Only borrower can request extension
        if (!booking.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to extend this booking."
            );
        }


        // Only approved bookings can request extension
        if (booking.getStatus() != BookingStatus.APPROVED) {

            throw new RuntimeException(
                    "Only approved bookings can be extended."
            );
        }


        // New duration must actually be greater
        if (rentalDays == null ||
                rentalDays <= booking.getRentalDays()) {

            throw new RuntimeException(
                    "New rental duration must be greater than current rental duration."
            );
        }


        // Prevent multiple pending extension requests
        if (booking.getExtensionStatus()
                == ExtensionStatus.PENDING) {

            throw new RuntimeException(
                    "An extension request is already pending."
            );
        }


        booking.setRequestedRentalDays(
                rentalDays
        );

        booking.setExtensionStatus(
                ExtensionStatus.PENDING
        );


        return repository.save(booking);
    }
    @Override
    public Booking approveExtension(
            Long bookingId,
            Long ownerId
    ) {

        Booking booking = repository
                .findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );


        if (!booking.getOwnerId().equals(ownerId)) {

            throw new RuntimeException(
                    "You are not authorized to approve this extension."
            );
        }


        if (booking.getExtensionStatus()
                != ExtensionStatus.PENDING) {

            throw new RuntimeException(
                    "No pending extension request found."
            );
        }


        booking.setRentalDays(
                booking.getRequestedRentalDays()
        );

        booking.setRequestedRentalDays(null);

        booking.setExtensionStatus(
                ExtensionStatus.APPROVED
        );


        return repository.save(booking);
    }
    @Override
    public Booking rejectExtension(
            Long bookingId,
            Long ownerId
    ) {

        Booking booking = repository
                .findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );


        if (!booking.getOwnerId().equals(ownerId)) {

            throw new RuntimeException(
                    "You are not authorized to reject this extension."
            );
        }


        if (booking.getExtensionStatus()
                != ExtensionStatus.PENDING) {

            throw new RuntimeException(
                    "No pending extension request found."
            );
        }


        booking.setRequestedRentalDays(null);

        booking.setExtensionStatus(
                ExtensionStatus.REJECTED
        );


        return repository.save(booking);
    }

}