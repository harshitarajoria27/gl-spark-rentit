package com.gl.resource_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    private Long ownerId;

    private String title;

    private String description;

    private String category;

    private Double rentPerDay;

    private Double securityDeposit;

    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private Condition condition;

    private Boolean available;

    private String city;

    private String state;

    private String imageUrl;

    private LocalDate createdDate;
}