package com.phishing.authservice.exception.exceptions;

public class FailSendEmailException extends RuntimeException{
    public FailSendEmailException(String message) {
        super(message);
    }
}
