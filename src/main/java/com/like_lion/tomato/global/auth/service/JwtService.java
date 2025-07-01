package com.like_lion.tomato.global.auth.service;

import com.like_lion.tomato.domain.auth.entity.RefreshToken;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.auth.repository.RefreshTokenRepository;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.global.auth.dto.TokenDto;
import com.like_lion.tomato.global.auth.dto.UserInfo;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.implement.RefreshTokenReader;
import com.like_lion.tomato.global.auth.implement.RefreshTokenWriter;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.like_lion.tomato.global.auth.implement.JwtTokenProvider.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberReader memberReader;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenReader refreshTokenReader;
    private final RefreshTokenWriter refreshTokenWriter;

    public LikeLionOAuth2User getPrincipal(String accessToken) {
        return LikeLionOAuth2User.from(jwtTokenProvider.extractMemberDTOFromAccessToken(accessToken));
    }

    @Transactional
    public void logout(String bearerRefreshToken) {
        String username = this.extractUsernameFromRefreshToken(bearerRefreshToken);
        log.debug("로그아웃 === 유저명: {}",  username);
        refreshTokenRepository.deleteAllByUsername(username);
    }

    public String extractMemberIdFromAccessToken(String accessToken) {
        return getPrincipal(accessToken).getId();
    }

    private String extractUsernameFromRefreshToken(String bearerRefreshToken) {
        String refreshToken = this.getTokenFromBearer(bearerRefreshToken);
        return jwtTokenProvider.getUsername(refreshToken);
    }

    public void validate(String token) {
        try {
            jwtTokenProvider.getPayload(token);
        } catch (SecurityException e) {
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION);
        } catch (MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_AUTH_CODE);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e)  {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    @Transactional
    public TokenDto tokenRefresh(String bearerRefreshToken) {
        String refreshToken = this.getTokenFromBearer(bearerRefreshToken);
        this.validate(refreshToken);
        LikeLionOAuth2User likeLionOAuth2User = this.extractLikeLionOAuth2User(refreshToken);
        log.debug("토큰 갱신 === 유저: {}", likeLionOAuth2User);
        return this.doTokenGenerationProcess(likeLionOAuth2User);
    }

    private LikeLionOAuth2User extractLikeLionOAuth2User(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenReader.findByPayload(refreshToken);
        Member member = memberReader.findByUsername(findRefreshToken.getUsername());
        return LikeLionOAuth2User.from(UserInfo.from(member));
    }

    @Transactional
    public TokenDto doTokenGenerationProcess(LikeLionOAuth2User principal) {
        TokenDto tokenDto = jwtTokenProvider.createJwt(principal);
        refreshTokenWriter.upsetRefreshToken(tokenDto);
        return tokenDto;
    }

    /**
     * Bearer Prefix를 포함한 값을 전달받으면 토큰만을 추출하여 반환
     *
     * @param bearerToken
     * @return Token (String)
     */
    public String getTokenFromBearer(String bearerToken) {
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }
}
