package com.phishing.authservice.component.token;

import com.phishing.authservice.domain.UserRole;
import com.phishing.authservice.payload.token.MemberInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenResolver {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public MemberInfo getAccessClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(removePrefix(token))
                .getBody();

        return MemberInfo.builder()
                .email(claims.get("USER_ID", String.class))
                .nickname(claims.get("USER_NICKNAME", String.class))
                .role(UserRole.valueOf(claims.get("USER_ROLE", String.class)))
                .build();
    }

    public MemberInfo getRefreshClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(removePrefix(token))
                .getBody();

        return MemberInfo.builder()
                .email(claims.get("USER_ID", String.class))
                .build();
    }

    public long getExpiration(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(removePrefix(token))
                .getBody()
                .getExpiration()
                .getTime();
    }

    private String removePrefix(String token){
        return token.replace("Bearer ", "");
    }
}
