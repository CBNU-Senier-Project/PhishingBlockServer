package com.phishing.authservice.dto.request;

public record VerifyEmailRequest(
        String email,
        String authCode
) {
}
