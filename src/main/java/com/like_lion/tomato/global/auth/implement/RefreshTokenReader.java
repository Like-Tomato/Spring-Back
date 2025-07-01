package com.like_lion.tomato.global.auth.implement;

import com.like_lion.tomato.domain.auth.entity.RefreshToken;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RefreshTokenReader {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findOptionalByUserId(String userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    public Optional<RefreshToken> findOptionalByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    public RefreshToken findByPayload(String payload) {
        return refreshTokenRepository.findByPayload(payload)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }
}
