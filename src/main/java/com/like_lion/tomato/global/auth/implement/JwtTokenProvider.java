package com.like_lion.tomato.global.auth.implement;

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.auth.dto.TokenDto;
import com.like_lion.tomato.global.auth.dto.UserInfo;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

// JWT 토큰 발급 서비스

@Getter
@Component
public class JwtTokenProvider {

    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtTokenProvider(@Value("${jwt.secret}")String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.jwtParser = Jwts.parser()
                .verifyWith(this.secretKey)
                .build();
    }

    // 토큰 생성 메서드
    public TokenDto createJwt(LikeLionOAuth2User likeLionOAuth2User) {
        String accessToken = generateAccessToken(likeLionOAuth2User, new Date());
        String refreshToken = generateRefreshToken(likeLionOAuth2User.getName(), new Date());

        return TokenDto.builder()
                .username(likeLionOAuth2User.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(LikeLionOAuth2User likeLionOAuth2User, Date now) {
        ArrayList<? extends GrantedAuthority> authorities =
                (ArrayList<? extends GrantedAuthority>) likeLionOAuth2User.getAuthorities();

        return Jwts.builder()
                .subject(likeLionOAuth2User.getName())
                .claim("id", likeLionOAuth2User.getId())
                .claim("username", likeLionOAuth2User.getName())
                .claim("role", likeLionOAuth2User.getRole())
                .claim("profileImage", likeLionOAuth2User.getProfileImage())
                .claim("provider", likeLionOAuth2User.getProvider())
                .issuedAt(new Date(now.getTime() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessTokenForMember(Member member) {
        LikeLionOAuth2User oAuth2User = LikeLionOAuth2User.from(UserInfo.from(member));
        return generateAccessToken(oAuth2User, new Date());
    }

    String generateRefreshToken(String username, Date now) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public UserInfo extractMemberDTOFromAccessToken(String accessToken) {
        return UserInfo.builder()
                .id(this.getId(accessToken))
                .username(this.getUsername(accessToken))
                .provider(this.getProvider(accessToken))
                .profileUrl(this.getProfileImage(accessToken))
                .role(this.getRole(accessToken))
                .build();
    }

    public String extractMemberIdFromToken(String authorization) {
        return Optional.ofNullable(authorization)
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.substring(7))
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_ACCESS_TOKEN));
    }

    /**
     * 서명된 토큰 값을 파싱하여 payload를 추출
     *
     * public String getUsername(String token) {
     *         return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
     *     }
     *     에서 반복되는 Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()을 모듈화
     *
     * @param token
     * @return claims(payload)
     */
    public Claims getPayload(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getId(String token) {
        return getPayload(token).get("username", String.class);
    }

    public String getUsername(String token) {
        return getPayload(token).get("username", String.class);
    }

    public String getProvider(String token) {
        return jwtParser.parseSignedClaims(token).getPayload().get("provider", String.class);
    }

    public String getRole(String token) {
        return getPayload(token).get("role", String.class);
    }

    public String getProfileImage(String token) {
        return getPayload(token).get("profileImage", String.class);
    }

    //필요시 다른 getter 추가 구현
}
