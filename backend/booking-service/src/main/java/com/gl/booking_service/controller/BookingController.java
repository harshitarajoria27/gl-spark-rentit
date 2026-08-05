package com.gl.booking_service.controller;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.dto.BookingUpdateRequest;
import com.gl.booking_service.entity.Booking;
import com.gl.booking_service.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody BookingRequest request) {

        return ResponseEntity.ok(
                service.createBooking(request, userId)
        );
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                service.cancelBooking(bookingId)
        );
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Booking> updateRentalDays(
            @PathVariable Long bookingId,
            @RequestBody BookingUpdateRequest request,
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(
                service.updateRentalDays(
                        bookingId,
                        userId,
                        request.getRentalDays()
                )
        );
    }
}