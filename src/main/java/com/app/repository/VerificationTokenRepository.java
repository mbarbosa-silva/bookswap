package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.VerificationToken;

public interface VerificationTokenRepository  extends JpaRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
}
