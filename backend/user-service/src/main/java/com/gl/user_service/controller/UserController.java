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
            Authentication authentication
    ){


        String email =
                authentication.getName();



        return ResponseEntity.ok(
                userService.getProfile(email)
        );

    }
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestParam String email,
            @RequestBody UpdateProfileRequest request
    ){

        return ResponseEntity.ok(
                userService.updateProfile(
                        email,
                        request
                )
        );

    }

}
