package com.like_lion.tomato.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String refreshTocken = CookieUtil.getRefreshTocken(request);
        jwtService.logout(refreshTocken);
        CookieUtil.deleteRefreshCookie(response);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<ApiResponse.MessageData> apiResponse = ApiResponse.successWithMessage("로그아웃이 완료되었습니다.");
        objectMapper.writeValue(response.getWriter(), response);
    }
}
