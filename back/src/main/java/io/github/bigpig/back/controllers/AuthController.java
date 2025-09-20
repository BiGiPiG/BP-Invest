package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.JwtRequest;
import io.github.bigpig.back.dto.JwtResponse;
import io.github.bigpig.back.dto.RegistrationUserDto;
import io.github.bigpig.back.dto.UserDto;
import io.github.bigpig.back.services.AuthService;
import io.github.bigpig.back.services.GoogleOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final AuthService authService;
    private final GoogleOAuth2Service googleOAuth2Service;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        String token = authService.createAuthToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registr")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto registrationUserDto) {
        UserDto userDto = authService.registerUser(registrationUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/exchange-code")
    public Map<String, String> exchangeCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String codeVerifier = request.get("code_verifier");
        String redirectUri = request.get("redirect_uri");

        String jwt = googleOAuth2Service.handleGoogleLogin(code, codeVerifier, redirectUri);

        return Map.of("token", jwt);
    }
}