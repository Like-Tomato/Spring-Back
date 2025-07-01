package com.like_lion.tomato.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(
                AuthErrorCode.ACCESS_DENIED.toString(),
                AuthErrorCode.ACCESS_DENIED.getMessage()
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
