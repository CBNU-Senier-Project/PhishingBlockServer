package com.phishing.authservice.exception;

import com.phishing.authservice.exception.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // todo: handle exception with custom message and status code, separated by exception type
    @ExceptionHandler( value = {
            DuplicateEmailException.class,
            InvalidPasswordException.class,
            FailSendEmailException.class,
            AuthCodeExpireException.class,
            InvalidAuthCodeException.class
    })
    protected ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
