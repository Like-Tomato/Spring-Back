package com.like_lion.tomato.global.auth.filter;

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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // OAuth2 인증 관련 엔드포인트는 예외처리 없이 바로 넘김
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            setErrorResponse(request, response, e);
        }
    }
/** --------------이거 해도 일단 해결 안됨
    // OAuth2 콜백, 인증 시작, 인증 필요 없는 경로는 필터를 건너뜀!
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/login/oauth2/code/")
                || path.startsWith("/oauth2/authorization/")
                || path.startsWith("/api/v1/auth/login")
                || path.startsWith("/api/v1/auth/refresh")
                || path.startsWith("/api/v1/auth/logout");
    }
 **/

    public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, JwtException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<String> apiResponse = ApiResponse.error(
                AuthErrorCode.INVALID_TOKEN.toString(),
                AuthErrorCode.INVALID_TOKEN.getMessage()
        );
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
