package com.gl.booking_service.dto;

import lombok.Data;

@Data
public class BookingRequest {


    private Long userId;

    private Long resourceId;

    private Integer rentalDays;

}
