package com.gl.user_service.service;


import com.gl.user_service.dto.AuthResponse;
import com.gl.user_service.dto.LoginRequest;
import com.gl.user_service.dto.RegisterRequest;
import com.gl.user_service.dto.UserResponse;

public interface UserService {


    UserResponse register(RegisterRequest request);


    AuthResponse login(LoginRequest request);


    UserResponse getProfile(String email);


}
