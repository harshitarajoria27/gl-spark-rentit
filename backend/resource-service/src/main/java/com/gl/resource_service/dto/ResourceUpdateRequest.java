package com.gl.resource_service.dto;



import lombok.Data;


@Data
public class ResourceUpdateRequest {

    private String title;

    private String description;

    private String category;

    private Double rentPerDay;

    private Double securityDeposit;

    private Integer quantity;

    private String city;

    private String state;

    private String imageUrl;

    private Boolean available;
}
