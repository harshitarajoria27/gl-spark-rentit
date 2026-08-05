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
    public Booking createBooking(
            BookingRequest request,
            Long userId) {

        Booking booking = new Booking();

        booking.setUserId(userId);
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

    @Override
    public Booking updateRentalDays(
            Long bookingId,
            Long userId,
            Integer rentalDays) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Ensure only the owner of the booking can update it
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this booking.");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Cancelled booking cannot be updated.");
        }

        booking.setRentalDays(rentalDays);

        return repository.save(booking);
    }
}