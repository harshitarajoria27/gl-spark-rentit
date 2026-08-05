package com.gl.user_service.service;




import com.gl.user_service.config.JwtService;
import com.gl.user_service.dto.*;
import com.gl.user_service.entity.User;
import com.gl.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;



    // Register User

    @Override
    public UserResponse register(RegisterRequest request) {


        if(userRepository.existsByEmail(request.getEmail())){

            throw new RuntimeException(
                    "Email already exists"
            );

        }


        if(userRepository.existsByPhone(request.getPhone())){

            throw new RuntimeException(
                    "Phone number already exists"
            );

        }



        User user = User.builder()

                .fullName(request.getFullName())

                .email(request.getEmail())

                .phone(request.getPhone())

                // Encrypt password
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )

                .address(request.getAddress())

                .city(request.getCity())

                .state(request.getState())

                .pincode(request.getPincode())

                .build();



        User savedUser =
                userRepository.save(user);



        return mapToResponse(savedUser);

    }





    // Login User

    @Override
    public AuthResponse login(LoginRequest request) {


        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid email or password"
                                )
                        );


        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )){

            throw new RuntimeException(
                    "Invalid email or password"
            );

        }


        String token =
                jwtService.generateToken(user);


        return AuthResponse.builder()

                .token(token)

                .message("Login Successful")

                .build();

    }




    // Get Profile

    @Override
    public UserResponse getProfile(String email) {


        User user =
                userRepository.findByEmail(email)

                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );


        return mapToResponse(user);

    }




    // Entity -> DTO

    private UserResponse mapToResponse(User user){


        return UserResponse.builder()

                .id(user.getUserId())

                .fullName(user.getFullName())

                .email(user.getEmail())

                .phone(user.getPhone())

                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .address(user.getAddress())

                .city(user.getCity())

                .state(user.getState())

                .pincode(user.getPincode())

                .build();

    }
    @Override
    public UserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ) {


        User user =
                userRepository.findByEmail(email)

                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );


        if(request.getProfileImage() != null){

            user.setProfileImage(
                    request.getProfileImage()
            );

        }


        if(request.getBio() != null){

            user.setBio(
                    request.getBio()
            );

        }


        if(request.getAddress() != null){

            user.setAddress(
                    request.getAddress()
            );

        }


        User updatedUser =
                userRepository.save(user);


        return mapToResponse(updatedUser);

    }

}
