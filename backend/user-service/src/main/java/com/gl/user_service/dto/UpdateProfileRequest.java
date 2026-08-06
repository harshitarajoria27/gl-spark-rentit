package com.gl.user_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    private String phone;

    private String profileImage;

    private String bio;

    private String address;

    private String city;

    private String state;

    private String pincode;
}