package com.phishing.authservice.payload.request;

public record VerifyEmailRequest(
        String email,
        String authCode
) {
}
