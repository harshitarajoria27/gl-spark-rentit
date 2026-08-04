package com.gl.booking_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private Long userId;

    private Long resourceId;

    private Integer rentalDays;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
