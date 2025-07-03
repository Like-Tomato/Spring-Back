package com.like_lion.tomato.global.auth.handler;

import com.like_lion.tomato.global.auth.dto.TokenDto;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.util.CookieUtil;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${client.url}")
    private String clientUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("Google 로그인 성공 후 req: {}", request.toString());

        LikeLionOAuth2User principal = (LikeLionOAuth2User) authentication.getPrincipal();
        TokenDto tokenDto = jwtService.doTokenGenerationProcess(principal);

        log.info("principal : {}", principal);

        log.info("access token: {}", tokenDto.getAccessToken());
        log.info("refresh token: {}", tokenDto.getRefreshToken());

        HttpHeaderUtil.setAccessToken(response, tokenDto.getAccessToken());
        // 리프레시 토큰: 쿠키 설정 (만료 시간은 밀리초 → 초 변환)
        int refreshMaxAge = (int) (jwtTokenProvider.getRefreshTokenExpiration() / 1000);
        CookieUtil.setRefreshCookies(response, tokenDto.getRefreshToken(), refreshMaxAge);

        // 로그로 토큰 정보 확인 (주석처리 해제해서 사용)
        log.info("OAuth2 로그인 성공: userId={}, accessToken={}, refreshToken={}", principal.getId(), tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        response.sendRedirect(createUri(tokenDto, principal.getId()));
    }

    private String createUri(TokenDto tokenDto, String userId) {
        return UriComponentsBuilder
                .fromUriString(clientUrl)
                .queryParam("accessToken", tokenDto.getAccessToken())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }
}
