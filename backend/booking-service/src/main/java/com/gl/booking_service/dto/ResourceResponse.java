package com.gl.booking_service.dto;

import lombok.Data;

@Data
public class ResourceResponse {

    private Long resourceId;

    private Long ownerId;

    private String title;

    private Boolean available;

    private Double rentPerDay;

    private Double securityDeposit;
}