package com.phishing.authservice.controller;

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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final MailService mailService;
    private final UserService userService;

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
    public ResponseEntity<Void> editProfile(HttpServletRequest httpServletRequest
            , @RequestBody @Valid EditProfileRequest editProfileRequest) {
        userService.editProfile(httpServletRequest, editProfileRequest);
        return ResponseEntity.ok().build();
    }
}
