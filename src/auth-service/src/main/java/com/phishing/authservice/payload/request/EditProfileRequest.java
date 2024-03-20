package com.phishing.authservice.payload.request;

public record EditProfileRequest(
        String nickname,
        String phnum
) {
}
