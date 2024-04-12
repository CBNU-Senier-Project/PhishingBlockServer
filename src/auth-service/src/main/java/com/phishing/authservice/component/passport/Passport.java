package com.phishing.authservice.component.passport;

public record Passport (
    Long userId,
    String email,
    String nickname,
    String role
){
}
