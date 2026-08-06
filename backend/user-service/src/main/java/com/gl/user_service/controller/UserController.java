package com.gl.user_service.controller;




import com.gl.user_service.dto.*;
import com.gl.user_service.service.UserService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;



    // Register API

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ){

        return new ResponseEntity<>(
                userService.register(request),
                HttpStatus.CREATED
        );

    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                userService.getUserById(userId)
        );
    }


    // Login API

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ){

        return ResponseEntity.ok(
                userService.login(request)
        );

    }





    // Get Profile API
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
            @RequestHeader("X-User-Id") Long userId
    ) {

        return ResponseEntity.ok(
                userService.getProfile(userId)
        );
    }


    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UpdateProfileRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateProfile(
                        userId,
                        request
                )
        );
    }

}
