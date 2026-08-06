package com.gl.booking_service.dto;


import com.gl.booking_service.entity.BookingStatus;
import com.gl.booking_service.entity.ExtensionStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long bookingId;

    private Long userId;

    private Long ownerId;

    private Long resourceId;

    private Integer rentalDays;

    private Integer requestedRentalDays;

    private ExtensionStatus extensionStatus;

    private BookingStatus status;


    // =========================
    // RENTER DETAILS
    // Owner can see these
    // =========================

    private String renterName;

    private String renterEmail;

    private String renterPhone;


    // =========================
    // OWNER DETAILS
    // Renter sees these only
    // after approval
    // =========================

    private String ownerName;

    private String ownerEmail;

    private String ownerPhone;
}
