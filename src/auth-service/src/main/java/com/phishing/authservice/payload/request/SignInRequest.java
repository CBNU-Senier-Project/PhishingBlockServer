package com.phishing.authservice.payload.request;

public record SignInRequest(
    String email,
    String password
) {
}
