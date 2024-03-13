package com.phishing.authservice.payload;

import com.phishing.authservice.domain.UserRole;
import lombok.Builder;

@Builder
public record MemberInfo(
        String email,
        String nickname,
        UserRole role
) {
}
