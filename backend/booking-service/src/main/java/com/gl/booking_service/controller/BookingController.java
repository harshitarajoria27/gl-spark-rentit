package com.gl.booking_service.controller;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.entity.Booking;
import com.gl.booking_service.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {


    private final BookingService service;


    public BookingController(BookingService service){

        this.service=service;
    }



    @PostMapping
    public Booking createBooking(
            @RequestBody BookingRequest request){

        return service.createBooking(request);

    }
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId) {

        return ResponseEntity.ok(service.cancelBooking(bookingId));

    }


}
