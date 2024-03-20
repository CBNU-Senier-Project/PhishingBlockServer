package com.phishing.authservice.component.token;

public record ReturnToken (
        String accessToken,
        String refreshToken
){
}
