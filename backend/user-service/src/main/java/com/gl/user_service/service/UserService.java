package com.gl.user_service.service;


import com.gl.user_service.dto.*;

public interface UserService {


    UserResponse register(RegisterRequest request);

    UserResponse getUserById(Long userId);
    AuthResponse login(LoginRequest request);


    UserResponse getProfile(Long userId);
    UserResponse updateProfile(
            Long userId,
            UpdateProfileRequest request
    );

}
