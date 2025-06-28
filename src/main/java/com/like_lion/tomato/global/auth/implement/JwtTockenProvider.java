package com.like_lion.tomato.global.auth.implement;

import com.like_lion.tomato.global.auth.dto.TockenDto;
import com.like_lion.tomato.global.auth.dto.UserInfo;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

// JWT 토큰 발급 서비스

@Component
public class JwtTockenProvider {

    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.access-tocken-expiration}")
    private Long accessTockenExpiration;

    @Value("${jwt.refresh-tocken-expiration}")
    private Long refreshTockenExpiration;

    private SecretKey secretKey;
    private JwtParser jwtParser;

    public JwtTockenProvider(@Value("${jwt.secret}")String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.jwtParser = Jwts.parser()
                .verifyWith(this.secretKey)
                .build();
    }

    // 토큰 생성 메서드
    public TockenDto createJwt(LikeLionOAuth2User likeLionOAuth2User) {
        String accessTocken = generateAccessTocken(likeLionOAuth2User, new Date());
        String refreshTocen = generateRefreshTocken(likeLionOAuth2User.getName(), new Date());

        return TockenDto.builder()
                .username(likeLionOAuth2User.getName())
                .accessTocken(accessTocken)
                .refreshTocken(refreshTocen)
                .build();
    }

    public String generateAccessTocken(LikeLionOAuth2User likeLionOAuth2User, Date now) {
        ArrayList<? extends GrantedAuthority> authorities =
                (ArrayList<? extends GrantedAuthority>) likeLionOAuth2User.getAuthorities();

        return Jwts.builder()
                .subject(likeLionOAuth2User.getName())
                .claim("id", likeLionOAuth2User.getId())
                .claim("username", likeLionOAuth2User.getName())
                .claim("role", likeLionOAuth2User.getRole())
                .claim("profileImage", likeLionOAuth2User.getProfileImage())
                .claim("provider", likeLionOAuth2User.getProvider())
                .issuedAt(new Date(now.getTime() + accessTockenExpiration))
                .signWith(secretKey)
                .compact();
    }

    String generateRefreshTocken(String username, Date now) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTockenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public Long getAccessTockenExpiration() {
        return accessTockenExpiration;
    }

    public Long getRefreshTockenExpiration() {
        return accessTockenExpiration;
    }

    public UserInfo extractMemberDTOFromAccessTocken(String accessToken) {
        return UserInfo.builder()
                .id(this.getId(accessToken))
                .username(this.getUsername(accessToken))
                .provider(this.getProvider(accessToken))
                .profileUrl(this.getProfileImage(accessToken))
                .role(this.getRole(accessToken))
                .build();
    }

    /**
     * 서명된 토큰 값을 파싱하여 payload를 추출
     *
     * public String getUsername(String token) {
     *         return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
     *     }
     *     에서 반복되는 Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()을 모듈화
     *
     * @param tocken
     * @return claims(payload)
     */
    public Claims getPayload(String tocken) {
        return jwtParser
                .parseSignedClaims(tocken)
                .getPayload();
    }

    public String getId(String tocken) {
        return getPayload(tocken).get("username", String.class);
    }

    public String getUsername(String tocken) {
        return getPayload(tocken).get("username", String.class);
    }

    public String getProvider(String tocken) {
        return jwtParser.parseSignedClaims(tocken).getPayload().get("provider", String.class);
    }

    public String getRole(String tocken) {
        return getPayload(tocken).get("role", String.class);
    }

    public String getProfileImage(String tocken) {
        return getPayload(tocken).get("profileImage", String.class);
    }

    //필요시 다른 getter 추가 구현



}
