package com.phishing.authservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@DynamicUpdate
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 15)
    private String nickname;

    @Column(name = "phnum", nullable = false, length = 20)
    private String phnum;

    @Column(name = "role", nullable = false, length = 10)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static User signUp(String email, String password, String nickname, String phnum) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phnum(phnum)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void editProfile(String nickname, String phnum) {
        this.nickname = nickname;
        this.phnum = phnum;
        this.updatedAt = LocalDateTime.now();
    }
}
