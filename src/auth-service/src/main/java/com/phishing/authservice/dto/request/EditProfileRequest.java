package com.phishing.authservice.dto.request;

public record EditProfileRequest(
        String nickname,
        String phnum
) {
}
