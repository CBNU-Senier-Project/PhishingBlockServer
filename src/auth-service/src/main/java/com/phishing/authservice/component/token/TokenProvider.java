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
        Claims claims = buildAccessClaims(user.getId());
        Claims refresh_claims = buildRefreshClaims(user.getId());
        return new ReturnToken(
                generateToken(claims, ACCESS_TIME),
                generateToken(refresh_claims, REFRESH_TIME)
        );
    }
    private String generateToken(Claims claims, long time) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static Claims buildAccessClaims(Long id) {
        Claims claims = Jwts.claims();
        claims.put("TOKEN_TYPE", "ACCESS");
        claims.put("USER_ID", id);
        return claims;
    }

    private static Claims buildRefreshClaims(Long id) {
        Claims claims = Jwts.claims();
        claims.put("TOKEN_TYPE", "REFRESH");
        claims.put("USER_ID", id);
        return claims;
    }

}
