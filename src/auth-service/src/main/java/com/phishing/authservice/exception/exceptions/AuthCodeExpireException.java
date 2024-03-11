package com.phishing.authservice.exception.exceptions;

public class AuthCodeExpireException extends RuntimeException{
    public AuthCodeExpireException(String message) {
        super(message);
    }
}
