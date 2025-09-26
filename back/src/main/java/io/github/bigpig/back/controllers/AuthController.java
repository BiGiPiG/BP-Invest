package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.JwtRequest;
import io.github.bigpig.back.dto.JwtResponse;
import io.github.bigpig.back.dto.RegistrationUserDto;
import io.github.bigpig.back.dto.UserDto;
import io.github.bigpig.back.services.AuthService;
import io.github.bigpig.back.services.GoogleOAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final GoogleOAuth2Service googleOAuth2Service;

    @Operation(
            summary = "User authentication",
            description = "Authenticates user and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful authentication",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Invalid credentials"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        String token = authService.createAuthToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(
            summary = "Register new user",
            description = "Creates new user in the system and returns user data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with this email already exists"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto registrationUserDto) {
        UserDto userDto = authService.registerUser(registrationUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Exchange Google authorization code for JWT token",
            description = "Exchanges Google OAuth2 authorization code for application JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful code exchange",
                    content = @Content(schema = @Schema(example = "{\"token\": \"jwt_token_here\"}"))
            )
    })
    @PostMapping("/exchange-code")
    public Map<String, String> exchangeCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String codeVerifier = request.get("code_verifier");
        String redirectUri = request.get("redirect_uri");

        String jwt = googleOAuth2Service.handleGoogleLogin(code, codeVerifier, redirectUri);

        return Map.of("token", jwt);
    }
}