package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.ExceptionDto;
import io.github.bigpig.back.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    void UserAlreadyExistsExceptionTest() {

        UserAlreadyExistsException ex = new UserAlreadyExistsException("Ivan");

        ResponseEntity<ExceptionDto> expected = new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_ALREADY_EXISTS.toString()), HttpStatus.CONFLICT);

        ResponseEntity<ExceptionDto> actual = globalExceptionHandler.handleException(ex);

        assertEquals(expected, actual);
    }

    @Test
    void UsernameNotFoundExceptionTest() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User Ivan not found");

        ResponseEntity<ExceptionDto> expected = new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_NOT_FOUND.toString()), HttpStatus.NOT_FOUND);

        ResponseEntity<ExceptionDto> actual = globalExceptionHandler.handleException(ex);

        assertEquals(expected, actual);
    }

    @Test
    void FetchDataExceptionTest() {
        FetchDataException ex = new FetchDataException("Test Exception");

        ResponseEntity<ExceptionDto> expected = new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.DATA_NOT_FETCHED.toString()), HttpStatus.INTERNAL_SERVER_ERROR);

        ResponseEntity<ExceptionDto> actual = globalExceptionHandler.handleException(ex);

        assertEquals(expected, actual);
    }

    @Test
    void LoginNotFoundException() {
        LoginNotFoundException ex = new LoginNotFoundException("Test Exception");

        ResponseEntity<ExceptionDto> expected = new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.USER_NOT_FOUND.toString()), HttpStatus.NOT_FOUND);

        ResponseEntity<ExceptionDto> actual = globalExceptionHandler.handleException(ex);

        assertEquals(expected, actual);
    }

    @Test
    void InvalidPasswordException() {
        InvalidPasswordException ex = new InvalidPasswordException("Test Exception");

        ResponseEntity<ExceptionDto> expected = new ResponseEntity<>(new ExceptionDto(ex.getMessage(),
                ErrorCodes.INVALID_PASSWORD.toString()), HttpStatus.NOT_FOUND);

        ResponseEntity<ExceptionDto> actual = globalExceptionHandler.handleException(ex);

        assertEquals(expected, actual);
    }
}
