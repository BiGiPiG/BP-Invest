package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.JwtRequest;
import io.github.bigpig.back.dto.JwtResponse;
import io.github.bigpig.back.dto.RegistrationUserDto;
import io.github.bigpig.back.dto.UserDto;
import io.github.bigpig.back.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        String token = authService.createAuthToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registr")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto registrationUserDto) throws BadRequestException {
        UserDto userDto = authService.registerUser(registrationUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}