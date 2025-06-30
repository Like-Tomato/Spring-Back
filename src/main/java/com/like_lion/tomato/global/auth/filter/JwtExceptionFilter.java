package com.like_lion.tomato.global.auth.filter;

import ch.qos.logback.core.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtService jwtService;;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // JwtAuthenticatioonFilter에서 예외 발생시 바로 setErrorResponse 호출
            setErrorResponse(request, response, e);
        }
    }

    public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, JwtException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<String> apiResponse = ApiResponse.error(
                AuthErrorCode.INVALID_TOKEN.toString(),
                AuthErrorCode.INVALID_TOKEN.getMessage()
        );
        objectMapper.writeValue(response.getWriter(), response);
    }
}
