package com.gl.booking_service.controller;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.dto.BookingResponse;
import com.gl.booking_service.dto.BookingUpdateRequest;
import com.gl.booking_service.dto.ExtensionRequest;
import com.gl.booking_service.entity.Booking;
import com.gl.booking_service.service.BookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {


    private final BookingService service;


    public BookingController(
            BookingService service
    ) {

        this.service = service;
    }


    /* ========================================
       CREATE BORROW REQUEST
    ======================================== */

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody BookingRequest request
    ) {

        return ResponseEntity.ok(
                service.createBooking(
                        request,
                        userId
                )
        );
    }


    /* ========================================
       GET BOOKINGS CREATED BY ME
    ======================================== */

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>>
    getMyBookings(
            @RequestHeader("X-User-Id")
            Long userId
    ) {

        return ResponseEntity.ok(
                service.getMyBookings(userId)
        );
    }


    /* ========================================
       GET REQUESTS RECEIVED FOR MY RESOURCES
    ======================================== */

    @GetMapping("/requests")
    public ResponseEntity<List<BookingResponse>>
    getReceivedRequests(
            @RequestHeader("X-User-Id")
            Long ownerId
    ) {

        return ResponseEntity.ok(
                service.getReceivedRequests(ownerId)
        );
    }


    /* ========================================
       CANCEL BOOKING
    ======================================== */

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<Booking>
    cancelBooking(
            @PathVariable Long bookingId,

            @RequestHeader("X-User-Id")
            Long userId
    ) {

        return ResponseEntity.ok(
                service.cancelBooking(
                        bookingId,
                        userId
                )
        );
    }


    /* ========================================
       UPDATE RENTAL DAYS
    ======================================== */

    @PutMapping("/{bookingId}")
    public ResponseEntity<Booking>
    updateRentalDays(
            @PathVariable Long bookingId,

            @RequestBody
            BookingUpdateRequest request,

            @RequestHeader("X-User-Id")
            Long userId
    ) {

        return ResponseEntity.ok(
                service.updateRentalDays(
                        bookingId,
                        userId,
                        request.getRentalDays()
                )
        );
    }


    /* ========================================
       APPROVE REQUEST
    ======================================== */

    @PutMapping("/{bookingId}/approve")
    public ResponseEntity<Booking>
    approveBooking(
            @PathVariable Long bookingId,

            @RequestHeader("X-User-Id")
            Long ownerId
    ) {

        return ResponseEntity.ok(
                service.approveBooking(
                        bookingId,
                        ownerId
                )
        );
    }


    /* ========================================
       REJECT REQUEST
    ======================================== */

    @PutMapping("/{bookingId}/reject")
    public ResponseEntity<Booking>
    rejectBooking(
            @PathVariable Long bookingId,

            @RequestHeader("X-User-Id")
            Long ownerId
    ) {

        return ResponseEntity.ok(
                service.rejectBooking(
                        bookingId,
                        ownerId
                )
        );
    }
    @PutMapping("/{bookingId}/extension")
    public ResponseEntity<Booking> requestExtension(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ExtensionRequest request
    ) {

        return ResponseEntity.ok(
                service.requestExtension(
                        bookingId,
                        userId,
                        request.getRentalDays()
                )
        );
    }
    @PutMapping("/{bookingId}/extension/approve")
    public ResponseEntity<Booking> approveExtension(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long ownerId
    ) {

        return ResponseEntity.ok(
                service.approveExtension(
                        bookingId,
                        ownerId
                )
        );
    }
    @PutMapping("/{bookingId}/extension/reject")
    public ResponseEntity<Booking> rejectExtension(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long ownerId
    ) {

        return ResponseEntity.ok(
                service.rejectExtension(
                        bookingId,
                        ownerId
                )
        );
    }

}