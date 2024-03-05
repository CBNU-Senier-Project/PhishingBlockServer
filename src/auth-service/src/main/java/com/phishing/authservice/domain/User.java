package com.phishing.authservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "nickname", nullable = false, length = 15)
    private String nickname;

    @Column(name = "phnum", nullable = false, length = 20)
    private String phnum;

    @Column(name = "role", nullable = false, length = 10)
    private UserRole role;

    @Builder
    public User(String email, String password, String nickname, String phnum, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phnum = phnum;
        this.role = role;
    }
}
