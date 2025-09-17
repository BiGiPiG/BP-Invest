package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.JwtRequest;
import io.github.bigpig.back.dto.RegistrationUserDto;
import io.github.bigpig.back.dto.UserDto;
import io.github.bigpig.back.models.User;
import io.github.bigpig.back.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public String createAuthToken(JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.username(), jwtRequest.password())
            );
        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.username());
        return jwtUtils.generateJwtToken(userDetails);
    }

    public UserDto registerUser(RegistrationUserDto registrationUserDto) throws BadRequestException {
        if (userService.findByUsername(registrationUserDto.username()).isPresent()) {
            throw new BadRequestException("Username is already in use");
        }
        User user = new User();
        user.setUsername(registrationUserDto.username());
        user.setPassword(passwordEncoder.encode(registrationUserDto.password()));
        user.setEmail(registrationUserDto.email());

        userService.createNewUser(user);
        return new UserDto(
                registrationUserDto.username(),
                registrationUserDto.email()
        );
    }


}
