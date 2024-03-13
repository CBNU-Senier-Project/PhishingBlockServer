package com.phishing.authservice.controller;

import com.phishing.authservice.dto.request.SignInRequest;
import com.phishing.authservice.dto.request.SignUpRequest;
import com.phishing.authservice.dto.request.VerifyEmailRequest;
import com.phishing.authservice.service.AuthService;
import com.phishing.authservice.service.MailService;
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
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;

    @GetMapping("/users/check")
    public ResponseEntity<?> checkEmail(@RequestParam @Valid @NotNull String email) {
        authService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/send")
    public ResponseEntity<?> sendMail(@RequestParam @Valid @NotNull String email) {
        mailService.sendMail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyMail(@RequestBody @Valid VerifyEmailRequest request) {
        mailService.verifyMail(request.email(), request.authCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request){
        authService.signOut(request);
        return ResponseEntity.ok().build();
    }
}
