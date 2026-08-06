package com.gl.booking_service.repository;

import com.gl.booking_service.entity.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long> {


    // Bookings created by borrower
    List<Booking> findByUserId(
            Long userId
    );


    // Requests received by resource owner
    List<Booking> findByOwnerId(
            Long ownerId
    );

}