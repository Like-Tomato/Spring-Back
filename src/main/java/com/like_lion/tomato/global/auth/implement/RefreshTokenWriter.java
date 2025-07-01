package com.like_lion.tomato.global.auth.implement;


import com.like_lion.tomato.domain.auth.entity.RefreshToken;
import com.like_lion.tomato.domain.auth.repository.RefreshTokenRepository;
import com.like_lion.tomato.global.auth.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTokenWriter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenReader refreshTokenReader;

    public void upsetRefreshToken(TokenDto tokenDto) {
        refreshTokenReader.findOptionalByUsername(tokenDto.getUsername())
                .ifPresentOrElse(
                        // 존재하면 업데이트
                        token -> token.updatePayload(tokenDto.getRefreshToken()),
                        // 존재하지 않으면 생성
                        () -> this.create(tokenDto)
                );
    }

    private void create(TokenDto tokenDto) {
        refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(), tokenDto.getUsername()));
    }

    public void removeAllByUsername(String username) {
        refreshTokenRepository.deleteAllByUsername(username);
    }
}

