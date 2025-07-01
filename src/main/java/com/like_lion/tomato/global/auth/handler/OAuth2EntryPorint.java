package com.like_lion.tomato.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2EntryPorint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<String> apiResponse = ApiResponse.error(
                AuthErrorCode.OAUTH2_UNAUTHORIZED.toString(),
                AuthErrorCode.OAUTH2_UNAUTHORIZED.getMessage()
        );
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
