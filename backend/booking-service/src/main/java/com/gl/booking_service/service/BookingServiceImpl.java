package com.gl.booking_service.service;

import com.gl.booking_service.dto.BookingRequest;
import com.gl.booking_service.entity.Booking;
import com.gl.booking_service.entity.BookingStatus;
import com.gl.booking_service.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Booking createBooking(BookingRequest request) {

        Booking booking = new Booking();

        booking.setUserId(request.getUserId());
        booking.setResourceId(request.getResourceId());
        booking.setRentalDays(request.getRentalDays());
        booking.setStatus(BookingStatus.CREATED);

        return repository.save(booking);
    }
    @Override
    public Booking cancelBooking(Long bookingId) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException("Confirmed booking cannot be cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return repository.save(booking);
    }
}
