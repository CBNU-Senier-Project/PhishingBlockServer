package com.phishing.authservice.exception.exceptions;

public class InvalidAuthCodeException extends RuntimeException{
    public InvalidAuthCodeException(String message) {
        super(message);
    }
}
