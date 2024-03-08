package com.phishing.authservice.dto.request;

public record SignInRequest(
    String email,
    String password
) {
}
