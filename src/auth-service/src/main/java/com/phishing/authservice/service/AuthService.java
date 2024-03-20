package com.phishing.authservice.service;

import com.phishing.authservice.component.token.ReturnToken;
import com.phishing.authservice.component.token.TokenProvider;
import com.phishing.authservice.component.token.TokenResolver;
import com.phishing.authservice.domain.User;
import com.phishing.authservice.payload.request.SignInRequest;
import com.phishing.authservice.exception.exceptions.InvalidPasswordException;
import com.phishing.authservice.redis.RedisDao;
import com.phishing.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RedisDao redisDao;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;

    @Value("${jwt.secret.refresh}")
    private Long refreshTime;

    public ReturnToken signIn(SignInRequest request) {
        userRepository.existsByEmail(request.email());
        User loginUser = userRepository.findByEmailAndIsDeletedIsFalse(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .orElseThrow(() -> new InvalidPasswordException("Invalid password"));
        ReturnToken returnToken = tokenProvider.provideTokens(loginUser);
        redisDao.setRedisValues(loginUser.getEmail(),
                returnToken.refreshToken(), Duration.ofMillis(refreshTime));

        return returnToken;
    }

    public void signOut(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("RefreshToken");
        long remainTime = tokenResolver.getExpiration(refreshToken);
        long ttl = remainTime - System.currentTimeMillis();
        String email = tokenResolver.getAccessClaims(accessToken).email();
        if (redisDao.isExistKey(email)) {
            redisDao.deleteRedisValues(email);
        }
        redisDao.setRedisValues("Blacklist_" + email, refreshToken, Duration.ofMillis(ttl));
    }

    public ReturnToken refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        String email = tokenResolver.getRefreshClaims(refreshToken).email();
        if (!redisDao.isExistKey(email) || !redisDao.getRedisValues(email).equals(refreshToken)) {
            throw new InvalidPasswordException("Invalid refresh token");
        }
        User loginUser = userRepository.findByEmailAndIsDeletedIsFalse(email)
                .orElseThrow(() -> new InvalidPasswordException("Invalid refresh token"));
        ReturnToken returnToken = tokenProvider.provideTokens(loginUser);
        redisDao.setRedisValues(loginUser.getEmail(),
                returnToken.refreshToken(), Duration.ofMillis(refreshTime));

        return returnToken;
    }
}
