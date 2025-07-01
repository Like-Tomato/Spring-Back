package com.like_lion.tomato.global.auth.implement;

import com.like_lion.tomato.domain.auth.entity.RefreshTocken;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.auth.repository.RefreshTockenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RefreshTockenReader {

    private final RefreshTockenRepository refreshTockenRepository;

    public Optional<RefreshTocken> findOptionalByUserId(String userId) {
        return refreshTockenRepository.findByUserId(userId);
    }

    public Optional<RefreshTocken> findOptionalByUsername(String username) {
        return refreshTockenRepository.findByUsername(username);
    }

    public RefreshTocken findByPayload(String payload) {
        return refreshTockenRepository.findByPayload(payload)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOCKEN_NOT_FOUND));
    }
}
