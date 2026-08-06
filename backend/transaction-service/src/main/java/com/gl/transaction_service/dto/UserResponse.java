package com.gl.transaction_service.dto;



import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;
}
