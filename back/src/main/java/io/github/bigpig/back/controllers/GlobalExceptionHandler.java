package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.ExceptionDto;
import io.github.bigpig.back.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> handleException(EmailAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.EMAIL_ALREADY_EXISTS.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> handleException(UserAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_ALREADY_EXISTS.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(UsernameNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FetchDataException.class)
    public ResponseEntity<ExceptionDto> handleException(FetchDataException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.DATA_NOT_FETCHED.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDto> handleException(BadCredentialsException ex) {
        log.error(ex.getMessage());
        if (ex instanceof LoginNotFoundException) {
            return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                    ErrorCodes.USER_NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.INVALID_PASSWORD.toString()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionDto> handleException(TokenExpiredException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.TOKEN_EXPIRED.toString()), HttpStatus.UNAUTHORIZED);
    }
}
