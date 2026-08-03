package com.gl.user_service.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String profileImage;

    private String address;

    private String city;

    private String state;

    private String pincode;
}
