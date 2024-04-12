package com.phishing.authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phishing.authservice.component.passport.Passport;
import com.phishing.authservice.domain.User;
import com.phishing.authservice.payload.request.EditProfileRequest;
import com.phishing.authservice.payload.request.SignUpRequest;
import com.phishing.authservice.payload.request.VerifyEmailRequest;
import com.phishing.authservice.service.MailService;
import com.phishing.authservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api/v1")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final MailService mailService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("/users/check")
    public ResponseEntity<Void> checkEmail(@RequestParam @Valid @NotNull String email) {
        userService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/send")
    public ResponseEntity<Void> sendMail(@RequestParam @Valid @NotNull String email) {
        mailService.sendMail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyMail(@RequestBody @Valid VerifyEmailRequest request) {
        mailService.verifyMail(request.email(), request.authCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/edit")
    public ResponseEntity<Void> editProfile(@RequestHeader("X-Authorization") String passport
            , @RequestBody @Valid EditProfileRequest editProfileRequest) throws JsonProcessingException {
        Passport userInfo = objectMapper.readValue(passport, Passport.class);
        userService.editProfile(userInfo, editProfileRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resign")
    public ResponseEntity<Void> resignUser(
            @RequestHeader("X-Authorization") String passport) throws JsonProcessingException {
        Passport userInfo = objectMapper.readValue(passport, Passport.class);
        userService.resignUser(userInfo);
        return ResponseEntity.ok().build();
    }

}
