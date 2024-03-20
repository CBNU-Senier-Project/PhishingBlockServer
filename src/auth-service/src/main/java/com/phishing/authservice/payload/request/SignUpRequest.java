package com.phishing.authservice.payload.request;

public record SignUpRequest(
        String email,
        String password,
        String nickname,
        String phnum
) {
}
