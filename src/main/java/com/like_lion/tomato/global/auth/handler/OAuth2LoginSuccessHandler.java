package com.like_lion.tomato.global.auth.handler;

import com.like_lion.tomato.global.auth.dto.TockenDto;
import com.like_lion.tomato.global.auth.implement.JwtTockenProvider;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.util.CookieUtil;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JwtTockenProvider jwtTockenProvider;

    @Value("${client.url}")
    private String clientUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LikeLionOAuth2User principal = (LikeLionOAuth2User) authentication.getPrincipal();
        TockenDto tockenDto = jwtService.doTockenGenerationProcess(principal);

        HttpHeaderUtil.setAccessTocken(response, tockenDto.getAccessTocken());
        // 리프레시 토큰: 쿠키 설정 (만료 시간은 밀리초 → 초 변환)
        int refreshMaxAge = (int) (jwtTockenProvider.getRefreshTockenExpiration() / 1000);
        CookieUtil.setRefreshCookies(response, tockenDto.getRefreshTocken(), refreshMaxAge);

        response.sendRedirect(createUri(tockenDto, principal.getId()));
    }

    private String createUri(TockenDto tockenDto, String userId) {
        return UriComponentsBuilder
                .fromUriString(clientUrl)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }
}
