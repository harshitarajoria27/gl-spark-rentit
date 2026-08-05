package com.gl.booking_service.service;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.entity.Booking;

public interface BookingService {

    Booking createBooking(
            BookingRequest request,
            Long userId);

    Booking cancelBooking(Long bookingId);

    Booking updateRentalDays(
            Long bookingId,
            Long userId,
            Integer rentalDays);
}