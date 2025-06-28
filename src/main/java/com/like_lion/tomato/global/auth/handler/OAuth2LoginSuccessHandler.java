package com.like_lion.tomato.global.auth.handler;

import com.like_lion.tomato.global.auth.dto.TockenDto;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LikeLionOAuth2User principal = (LikeLionOAuth2User) authentication.getPrincipal();
        //TockenDto tockenDto jwtService.do
    }
}
