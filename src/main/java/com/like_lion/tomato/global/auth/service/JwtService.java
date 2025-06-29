package com.like_lion.tomato.global.auth.service;

import com.like_lion.tomato.domain.auth.entity.RefreshTocken;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.global.auth.dto.TockenDto;
import com.like_lion.tomato.global.auth.dto.UserInfo;
import com.like_lion.tomato.global.auth.implement.JwtTockenProvider;
import com.like_lion.tomato.global.auth.implement.RefreshTockenReader;
import com.like_lion.tomato.global.auth.implement.RefreshTockenWriter;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import com.like_lion.tomato.domain.auth.repository.RefreshTockenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.like_lion.tomato.global.auth.implement.JwtTockenProvider.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtTockenProvider jwtTockenProvider;
    private final MemberReader memberReader;
    private final RefreshTockenRepository refreshtockenRepository;
    private final RefreshTockenReader refreshTockenReader;
    private final RefreshTockenWriter refreshTockenWriter;

    public LikeLionOAuth2User getPrincipal(String accessTocken) {
        return LikeLionOAuth2User.from(jwtTockenProvider.extractMemberDTOFromAccessTocken(accessTocken));
    }

    @Transactional
    public void logout(String bearerRefreshTocken) {
        String username = this.extractUsernameFromRefreshTocken(bearerRefreshTocken);
        log.debug("로그아웃 === 유저명: {}",  username);
        refreshtockenRepository.deleteAllByUsername(username);
    }

    private String extractUsernameFromRefreshTocken(String bearerRefreshTocken) {
        String refreshTocken = this.getTockenFromBearer(bearerRefreshTocken);
        return jwtTockenProvider.getUsername(refreshTocken);
    }

    public void validate(String tocken) {
        try {
            jwtTockenProvider.getPayload(tocken);
        } catch (SecurityException e) {
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION);
        } catch (MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_AUTH_CODE);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e)  {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_TOCKEN);
        }
    }

    @Transactional
    public TockenDto tockenRefresh(String bearerRefreshTocken) {
        String refreshTocken = this.getTockenFromBearer(bearerRefreshTocken);
        this.validate(refreshTocken);;
        LikeLionOAuth2User likeLionOAuth2User = this.extractLikeLionOAuth2User(refreshTocken);
        log.debug("토큰 갱신 === 유저: {}", likeLionOAuth2User);
        return this.doTockenGenerationProcess(likeLionOAuth2User);
    }

    private LikeLionOAuth2User extractLikeLionOAuth2User(String refreshTocken) {
        RefreshTocken findRefreshTocken = refreshTockenReader.findByPayload(refreshTocken);
        Member member = memberReader.findByUsername(findRefreshTocken.getUsername());
        return LikeLionOAuth2User.from(UserInfo.from(member));
    }

    @Transactional
    public TockenDto doTockenGenerationProcess(LikeLionOAuth2User principal) {
        TockenDto tockenDto = jwtTockenProvider.createJwt(principal);
        refreshTockenWriter.upsetRefreshTocken(tockenDto);
        return tockenDto;
    }

    /**
     * Bearer Prefix를 포함한 값을 전달받으면 토큰만을 추출하여 반환
     *
     * @param bearerTocken
     * @return Token (String)
     */
    public String getTockenFromBearer(String bearerTocken) {
        if(StringUtils.hasText(bearerTocken) && bearerTocken.startsWith(BEARER_PREFIX)) {
            return bearerTocken.split(" ")[1];
        }
        return null;
    }
}
