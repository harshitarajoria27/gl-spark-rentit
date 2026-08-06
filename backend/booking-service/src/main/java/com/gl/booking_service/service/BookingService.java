package com.gl.booking_service.service;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.dto.BookingResponse;
import com.gl.booking_service.entity.Booking;

import java.util.List;

public interface BookingService {


    Booking createBooking(
            BookingRequest request,
            Long userId
    );


    List<BookingResponse> getMyBookings(Long userId);

    List<BookingResponse> getReceivedRequests(Long ownerId);


    Booking cancelBooking(
            Long bookingId,
            Long userId
    );


    Booking updateRentalDays(
            Long bookingId,
            Long userId,
            Integer rentalDays
    );


    Booking approveBooking(
            Long bookingId,
            Long ownerId
    );


    Booking rejectBooking(
            Long bookingId,
            Long ownerId
    );
    Booking requestExtension(
            Long bookingId,
            Long userId,
            Integer rentalDays
    );
    Booking approveExtension(
            Long bookingId,
            Long ownerId
    );
    Booking rejectExtension(
            Long bookingId,
            Long ownerId
    );

}