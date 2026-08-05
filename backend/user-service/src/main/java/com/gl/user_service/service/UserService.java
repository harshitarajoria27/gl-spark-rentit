package com.gl.user_service.service;


import com.gl.user_service.dto.*;

public interface UserService {


    UserResponse register(RegisterRequest request);


    AuthResponse login(LoginRequest request);


    UserResponse getProfile(String email);
    UserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    );

}
