package com.phishing.authservice.repository;

import com.phishing.authservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByIdAndIsDeletedIsFalse(Long id);
    Optional<User> findByEmailAndIsDeletedIsFalse(String email);

}
