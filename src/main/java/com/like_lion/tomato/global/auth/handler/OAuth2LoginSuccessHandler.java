package com.like_lion.tomato.global.auth.handler;

import com.like_lion.tomato.global.auth.dto.TockenDto;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import com.like_lion.tomato.global.auth.service.JwtService;
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

    @Value("${client.url}")
    private String clientUrl;

    private final JwtService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LikeLionOAuth2User principal = (LikeLionOAuth2User) authentication.getPrincipal();
        TockenDto tockenDto = jwtService.doTockenGenerationProcess(principal);
        jwtService.setCookie(tockenDto, response);
        String targetUri = this.createUri(tockenDto, principal.getId());
        response.sendRedirect(targetUri);
    }

    private String createUri(TockenDto tockenDto, String userId) {
        return UriComponentsBuilder
                .fromUriString(clientUrl)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }
}
