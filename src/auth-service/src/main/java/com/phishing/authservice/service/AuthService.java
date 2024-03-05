package com.phishing.authservice.service;

import com.phishing.authservice.domain.User;
import com.phishing.authservice.domain.UserRole;
import com.phishing.authservice.dto.request.SignUpRequest;
import com.phishing.authservice.exception.exceptions.DuplicateEmailException;
import com.phishing.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public void checkEmail(String email) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }
    }

    public void signUp(SignUpRequest request) {
        // Check if email already exists
        checkEmail(request.email());
        // Save user
        userRepository.save(User.builder()
                        .email(request.email())
                        .password(request.password())
                        .nickname(request.nickname())
                        .phnum(request.phnum())
                        .role(UserRole.USER)
                .build());
    }


}
