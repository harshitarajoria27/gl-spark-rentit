package com.gl.booking_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long bookingId;


    // User requesting the resource
    @Column(nullable = false)
    private Long userId;


    // Owner of the resource
    @Column(nullable = false)
    private Long ownerId;


    // Resource being requested
    @Column(nullable = false)
    private Long resourceId;


    @Column(nullable = false)
    private Integer rentalDays;

    private Integer requestedRentalDays;

    @Enumerated(EnumType.STRING)
    private ExtensionStatus extensionStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

}