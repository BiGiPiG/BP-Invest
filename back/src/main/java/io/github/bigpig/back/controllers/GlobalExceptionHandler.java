package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.ExceptionDto;
import io.github.bigpig.back.exceptions.ErrorCodes;
import io.github.bigpig.back.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> handleException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_ALREADY_EXISTS.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_NOT_FOUND.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
