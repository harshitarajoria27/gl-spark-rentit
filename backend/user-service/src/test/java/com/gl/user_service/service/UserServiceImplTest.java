package com.gl.user_service.service;

import com.gl.user_service.config.JwtService;
import com.gl.user_service.dto.*;
import com.gl.user_service.entity.User;
import com.gl.user_service.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;


    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private JwtService jwtService;


    @InjectMocks
    private UserServiceImpl userService;


    private User user;


    // =================================================
    // RUNS BEFORE EVERY TEST
    // =================================================

    @BeforeEach
    void setUp() {

        user = User.builder()

                .userId(1L)

                .fullName("Vishnu Rajoria")

                .email("vishnu@gmail.com")

                .phone("9876543210")

                .password("encodedPassword")

                .address("Ghaziabad")

                .city("Ghaziabad")

                .state("Uttar Pradesh")

                .pincode("201206")

                .bio("Student")

                .profileImage("profile.jpg")

                .build();
    }


    // =================================================
    // TEST 1
    // REGISTER USER SUCCESSFULLY
    // =================================================

    @Test
    void registerUserSuccessfully() {

        RegisterRequest request =
                new RegisterRequest();

        request.setFullName(
                "Vishnu Rajoria"
        );

        request.setEmail(
                "vishnu@gmail.com"
        );

        request.setPhone(
                "9876543210"
        );

        request.setPassword(
                "Password@123"
        );

        request.setAddress(
                "Ghaziabad"
        );

        request.setCity(
                "Ghaziabad"
        );

        request.setState(
                "Uttar Pradesh"
        );

        request.setPincode(
                "201206"
        );


        when(
                userRepository.existsByEmail(
                        request.getEmail()
                )
        ).thenReturn(false);


        when(
                userRepository.existsByPhone(
                        request.getPhone()
                )
        ).thenReturn(false);


        when(
                passwordEncoder.encode(
                        request.getPassword()
                )
        ).thenReturn(
                "encodedPassword"
        );


        when(
                userRepository.save(
                        any(User.class)
                )
        ).thenReturn(user);


        UserResponse response =
                userService.register(
                        request
                );


        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Vishnu Rajoria",
                response.getFullName()
        );

        assertEquals(
                "vishnu@gmail.com",
                response.getEmail()
        );

        assertEquals(
                "9876543210",
                response.getPhone()
        );


        verify(
                userRepository,
                times(1)
        ).save(
                any(User.class)
        );
    }


    // =================================================
    // TEST 2
    // DUPLICATE EMAIL
    // =================================================

    @Test
    void registerShouldFailWhenEmailAlreadyExists() {

        RegisterRequest request =
                new RegisterRequest();

        request.setEmail(
                "vishnu@gmail.com"
        );


        when(
                userRepository.existsByEmail(
                        "vishnu@gmail.com"
                )
        ).thenReturn(true);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.register(
                                        request
                                )
                );


        assertEquals(
                "Email already exists",
                exception.getMessage()
        );


        verify(
                userRepository,
                never()
        ).save(
                any(User.class)
        );
    }


    // =================================================
    // TEST 3
    // DUPLICATE PHONE
    // =================================================

    @Test
    void registerShouldFailWhenPhoneAlreadyExists() {

        RegisterRequest request =
                new RegisterRequest();

        request.setEmail(
                "vishnu@gmail.com"
        );

        request.setPhone(
                "9876543210"
        );


        when(
                userRepository.existsByEmail(
                        request.getEmail()
                )
        ).thenReturn(false);


        when(
                userRepository.existsByPhone(
                        request.getPhone()
                )
        ).thenReturn(true);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.register(
                                        request
                                )
                );


        assertEquals(
                "Phone number already exists",
                exception.getMessage()
        );


        verify(
                userRepository,
                never()
        ).save(
                any(User.class)
        );
    }


    // =================================================
    // TEST 4
    // LOGIN SUCCESS
    // =================================================

    @Test
    void loginSuccessfully() {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "vishnu@gmail.com"
        );

        request.setPassword(
                "Password@123"
        );


        when(
                userRepository.findByEmail(
                        request.getEmail()
                )
        ).thenReturn(
                Optional.of(user)
        );


        when(
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )
        ).thenReturn(true);


        when(
                jwtService.generateToken(user)
        ).thenReturn(
                "test-jwt-token"
        );


        AuthResponse response =
                userService.login(
                        request
                );


        assertNotNull(response);

        assertEquals(
                "test-jwt-token",
                response.getToken()
        );

        assertEquals(
                "Login Successful",
                response.getMessage()
        );
    }


    // =================================================
    // TEST 5
    // LOGIN - EMAIL NOT FOUND
    // =================================================

    @Test
    void loginShouldFailWhenEmailDoesNotExist() {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "wrong@gmail.com"
        );

        request.setPassword(
                "Password@123"
        );


        when(
                userRepository.findByEmail(
                        request.getEmail()
                )
        ).thenReturn(
                Optional.empty()
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.login(
                                        request
                                )
                );


        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 6
    // LOGIN - WRONG PASSWORD
    // =================================================

    @Test
    void loginShouldFailWhenPasswordIsWrong() {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "vishnu@gmail.com"
        );

        request.setPassword(
                "WrongPassword"
        );


        when(
                userRepository.findByEmail(
                        request.getEmail()
                )
        ).thenReturn(
                Optional.of(user)
        );


        when(
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )
        ).thenReturn(false);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.login(
                                        request
                                )
                );


        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );


        verify(
                jwtService,
                never()
        ).generateToken(
                any(User.class)
        );
    }


    // =================================================
    // TEST 7
    // GET USER BY ID
    // =================================================

    @Test
    void getUserByIdSuccessfully() {

        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(user)
        );


        UserResponse response =
                userService.getUserById(
                        1L
                );


        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Vishnu Rajoria",
                response.getFullName()
        );

        assertEquals(
                "vishnu@gmail.com",
                response.getEmail()
        );

        assertEquals(
                "9876543210",
                response.getPhone()
        );
    }


    // =================================================
    // TEST 8
    // GET USER - NOT FOUND
    // =================================================

    @Test
    void getUserByIdShouldFailWhenUserNotFound() {

        when(
                userRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.getUserById(
                                        99L
                                )
                );


        assertEquals(
                "User not found with id : 99",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 9
    // GET PROFILE
    // =================================================

    @Test
    void getProfileSuccessfully() {

        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(user)
        );


        UserResponse response =
                userService.getProfile(
                        1L
                );


        assertNotNull(response);

        assertEquals(
                "Vishnu Rajoria",
                response.getFullName()
        );

        assertEquals(
                "Ghaziabad",
                response.getCity()
        );

        assertEquals(
                "Uttar Pradesh",
                response.getState()
        );
    }


    // =================================================
    // TEST 10
    // GET PROFILE - USER NOT FOUND
    // =================================================

    @Test
    void getProfileShouldFailWhenUserNotFound() {

        when(
                userRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.getProfile(
                                        99L
                                )
                );


        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 11
    // UPDATE PROFILE
    // =================================================

    @Test
    void updateProfileSuccessfully() {

        UpdateProfileRequest request =
                new UpdateProfileRequest();

        request.setPhone(
                "9999999999"
        );

        request.setProfileImage(
                "new-profile.jpg"
        );

        request.setBio(
                "Updated Bio"
        );

        request.setAddress(
                "New Address"
        );

        request.setCity(
                "Delhi"
        );

        request.setState(
                "Delhi"
        );

        request.setPincode(
                "110001"
        );


        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(user)
        );


        when(
                userRepository.save(
                        any(User.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        UserResponse response =
                userService.updateProfile(
                        1L,
                        request
                );


        assertNotNull(response);

        assertEquals(
                "9999999999",
                response.getPhone()
        );

        assertEquals(
                "Updated Bio",
                response.getBio()
        );

        assertEquals(
                "Delhi",
                response.getCity()
        );

        assertEquals(
                "Delhi",
                response.getState()
        );

        assertEquals(
                "110001",
                response.getPincode()
        );


        verify(
                userRepository,
                times(1)
        ).save(user);
    }


    // =================================================
    // TEST 12
    // UPDATE PROFILE - USER NOT FOUND
    // =================================================

    @Test
    void updateProfileShouldFailWhenUserNotFound() {

        UpdateProfileRequest request =
                new UpdateProfileRequest();


        when(
                userRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                userService.updateProfile(
                                        99L,
                                        request
                                )
                );


        assertEquals(
                "User not found",
                exception.getMessage()
        );


        verify(
                userRepository,
                never()
        ).save(
                any(User.class)
        );
    }
}