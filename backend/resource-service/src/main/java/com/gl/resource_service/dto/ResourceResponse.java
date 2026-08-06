package com.gl.resource_service.dto;

import com.gl.resource_service.entity.Condition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResourceResponse {

    private Long resourceId;

    private Long ownerId;

    private String title;

    private String description;

    private String category;

    private Double rentPerDay;

    private Double securityDeposit;

    private Integer quantity;


    private Condition condition;

    private Boolean available;

    private String city;

    private String state;

    private String imageUrl;

    private LocalDate createdDate;
}