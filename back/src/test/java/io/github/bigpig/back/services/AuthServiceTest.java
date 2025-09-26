package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.JwtRequest;
import io.github.bigpig.back.dto.RegistrationUserDto;
import io.github.bigpig.back.dto.UserDto;
import io.github.bigpig.back.exceptions.InvalidPasswordException;
import io.github.bigpig.back.exceptions.LoginNotFoundException;
import io.github.bigpig.back.models.User;
import io.github.bigpig.back.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private final String USERNAME = "testuser";
    private final String PASSWORD = "password";
    private final String EMAIL = "test@example.com";
    private final String ENCODED_PASSWORD = "encodedPassword";

    @BeforeEach
    void setUp() {
        authService = new AuthService(authenticationManager, userService, jwtUtils, passwordEncoder);
    }

    @Test
    @DisplayName("createAuthToken should return JWT token when authentication succeeds")
    void createAuthToken_Success() {
        // Arrange
        JwtRequest jwtRequest = new JwtRequest(USERNAME, PASSWORD);
        UserDetails userDetails = mock(UserDetails.class);

        when(userService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        String JWT_TOKEN = "jwt.token.here";
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn(JWT_TOKEN);

        // Act
        String result = authService.createAuthToken(jwtRequest);

        // Assert
        assertEquals(JWT_TOKEN, result);
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)
        );
        verify(userService).loadUserByUsername(USERNAME);
        verify(jwtUtils).generateJwtToken(userDetails);
    }

    @Test
    @DisplayName("createAuthToken should throw LoginNotFoundException when user not found")
    void createAuthToken_UserNotFound_ThrowsLoginNotFoundException() {
        // Arrange
        JwtRequest jwtRequest = new JwtRequest(USERNAME, PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        LoginNotFoundException exception = assertThrows(LoginNotFoundException.class,
                () -> authService.createAuthToken(jwtRequest));

        assertEquals("Invalid login", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByUsername(USERNAME);
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtUtils, never()).generateJwtToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("createAuthToken should throw InvalidPasswordException when password is incorrect")
    void createAuthToken_InvalidPassword_ThrowsInvalidPasswordException() {
        // Arrange
        JwtRequest jwtRequest = new JwtRequest(USERNAME, PASSWORD);
        User existingUser = new User();
        existingUser.setUsername(USERNAME);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> authService.createAuthToken(jwtRequest));

        assertEquals("Invalid password", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByUsername(USERNAME);
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtUtils, never()).generateJwtToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("createAuthToken should propagate other exceptions")
    void createAuthToken_OtherException_PropagatesException() {
        // Arrange
        JwtRequest jwtRequest = new JwtRequest(USERNAME, PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.createAuthToken(jwtRequest));

        assertEquals("Unexpected error", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).findByUsername(anyString());
        verify(userService, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("registerUser should register new user and return UserDto")
    void registerUser_Success() {
        // Arrange
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(USERNAME, PASSWORD, EMAIL);
        User savedUser = new User();
        savedUser.setUsername(USERNAME);
        savedUser.setPassword(ENCODED_PASSWORD);
        savedUser.setEmail(EMAIL);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // Act
        UserDto result = authService.registerUser(registrationUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(EMAIL, result.email());

        verify(passwordEncoder).encode(PASSWORD);
        verify(userService).createNewUser(argThat(user ->
                USERNAME.equals(user.getUsername()) &&
                        ENCODED_PASSWORD.equals(user.getPassword()) &&
                        EMAIL.equals(user.getEmail())
        ));
    }

    @Test
    @DisplayName("registerUser should encode password before saving")
    void registerUser_EncodesPassword() {
        // Arrange
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(USERNAME, PASSWORD, EMAIL);
        String differentEncodedPassword = "differentEncodedPassword";

        when(passwordEncoder.encode(PASSWORD)).thenReturn(differentEncodedPassword);

        // Act
        UserDto result = authService.registerUser(registrationUserDto);

        // Assert
        assertNotNull(result);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userService).createNewUser(argThat(user ->
                differentEncodedPassword.equals(user.getPassword())
        ));
    }

    @Test
    @DisplayName("registerUser should return UserDto with correct data")
    void registerUser_ReturnsCorrectUserDto() {
        // Arrange
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(USERNAME, PASSWORD, EMAIL);

        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        // Act
        UserDto result = authService.registerUser(registrationUserDto);

        // Assert
        assertEquals(USERNAME, result.username());
        assertEquals(EMAIL, result.email());
    }

    @Test
    @DisplayName("registerUser should handle null values gracefully")
    void registerUser_WithNullValues() {
        // Arrange
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(null, null, null);

        when(passwordEncoder.encode(null)).thenReturn("encodedNull");

        // Act
        UserDto result = authService.registerUser(registrationUserDto);

        // Assert
        assertNull(result.username());
        assertNull(result.email());
        verify(passwordEncoder).encode(null);
        verify(userService).createNewUser(argThat(user ->
                user.getUsername() == null &&
                        user.getEmail() == null
        ));
    }
}
