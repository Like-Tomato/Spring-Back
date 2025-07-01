package com.like_lion.tomato.domain.auth.repository;

import com.like_lion.tomato.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByPayload(String payload);

    void deleteAllByUsername(String username);

    Optional<RefreshToken> findByUsername(String username);


    Optional<RefreshToken> findByUserId(String userId);
}
