package com.phishing.authservice.dto.request;

public record SignUpRequest(
        String email,
        String password,
        String nickname,
        String phnum
) {
}
