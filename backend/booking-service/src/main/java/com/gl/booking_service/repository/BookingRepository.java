package com.gl.booking_service.repository;

import com.gl.booking_service.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository
        extends JpaRepository<Booking,Long> {

}
