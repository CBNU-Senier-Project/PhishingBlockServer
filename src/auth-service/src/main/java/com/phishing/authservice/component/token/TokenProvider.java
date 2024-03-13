package com.phishing.authservice.component.token;

import com.phishing.authservice.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.secret.access}")
    private long ACCESS_TIME;

    @Value("${jwt.secret.refresh}")
    private long REFRESH_TIME;

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public ReturnToken provideTokens(User user) {
        Claims claims = buildClaims(user);
        Claims refresh_claims = buildClaims(user.getEmail());
        return new ReturnToken(
                generateToken(claims, ACCESS_TIME),
                generateToken(refresh_claims, REFRESH_TIME)
        );
    }
    private String generateToken(Claims claims, long time) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static Claims buildClaims(User user) {
        Claims claims = Jwts.claims();
        claims.put("USER_ID", user.getEmail());
        claims.put("USER_NICKNAME", user.getNickname());
        claims.put("USER_ROLE", user.getRole());
        return claims;
    }

    private static Claims buildClaims(String email) {
        Claims claims = Jwts.claims();
        claims.put("USER_ID", email);
        return claims;
    }

}
