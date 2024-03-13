package com.phishing.authservice.service;

import com.phishing.authservice.component.token.TokenResolver;
import com.phishing.authservice.domain.User;
import com.phishing.authservice.dto.request.EditProfileRequest;
import com.phishing.authservice.dto.request.SignUpRequest;
import com.phishing.authservice.exception.exceptions.DuplicateEmailException;
import com.phishing.authservice.payload.MemberInfo;
import com.phishing.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenResolver tokenResolver;

    public void checkEmail(String email) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }
    }

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

    public void editProfile(HttpServletRequest httpServletRequest, EditProfileRequest request){
        MemberInfo memberInfo = getMemberInfoToToken(httpServletRequest);

        User targetUser = userRepository.findByEmail(memberInfo.email())
                .orElseThrow(() -> new DuplicateEmailException("Email Not exists"));

        targetUser.editProfile(
                request.nickname(),
                request.phnum()
        );
        System.out.println("targetUser = " + targetUser.getNickname());
    }

    private MemberInfo getMemberInfoToToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        MemberInfo result = tokenResolver.getClaims(token);
        return result;
    }
}
