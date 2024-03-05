package com.phishing.authservice.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String type;

    UserRole(String type) {
        this.type = type;
    }
}
