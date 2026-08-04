package com.gl.booking_service.service;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.entity.Booking;

public interface BookingService {

    Booking createBooking(BookingRequest request);
    Booking cancelBooking(Long bookingId);
}
