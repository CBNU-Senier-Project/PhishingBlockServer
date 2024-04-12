package com.phishing.authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phishing.authservice.component.token.ReturnToken;
import com.phishing.authservice.payload.request.SignInRequest;
import com.phishing.authservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api/v1")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ReturnToken> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(HttpServletRequest request){
        authService.signOut(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReturnToken> refresh(HttpServletRequest request){
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/passport")
    public ResponseEntity<String> generatePassport(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(authService.generatePassport(request));
    }
}
