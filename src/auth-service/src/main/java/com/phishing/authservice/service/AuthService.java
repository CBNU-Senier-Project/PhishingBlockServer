package com.phishing.authservice.service;

import com.phishing.authservice.component.token.ReturnToken;
import com.phishing.authservice.component.token.TokenProvider;
import com.phishing.authservice.domain.User;
import com.phishing.authservice.domain.UserRole;
import com.phishing.authservice.dto.request.SignInRequest;
import com.phishing.authservice.dto.request.SignUpRequest;
import com.phishing.authservice.exception.exceptions.DuplicateEmailException;
import com.phishing.authservice.exception.exceptions.InvalidPasswordException;
import com.phishing.authservice.redis.RedisDao;
import com.phishing.authservice.repository.UserRepository;
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

    @Value("${jwt.secret.refresh}")
    private Long refreshTime;

    @Transactional
    public void checkEmail(String email) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }
    }

    @Transactional
    public void signUp(SignUpRequest request) {
        // Check if email already exists
        checkEmail(request.email());
        // Create user
        User user = User.signUp(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname(),
                request.phnum()
        );
        // Save user
        userRepository.save(user);
    }

    @Transactional
    public ReturnToken signIn(SignInRequest request) {
        // Check if email isn't exists
        userRepository.existsByEmail(request.email());
        // Check if password is correct
        User loginUser = userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .orElseThrow(() -> new InvalidPasswordException("Invalid password"));
        // return jwt token
        ReturnToken returnToken = tokenProvider.provideTokens(loginUser);
        // save refresh token in redis
        redisDao.setRedisValues(String.valueOf(loginUser.getId()),
                returnToken.refreshToken(), Duration.ofMillis(refreshTime));

        return returnToken;
    }

}
