package io.github.bigpig.back.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class LoginNotFoundException extends BadCredentialsException {
    public LoginNotFoundException(String message) {
        super(message);
    }
}
