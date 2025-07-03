package com.like_lion.tomato.global.util;

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

import static com.like_lion.tomato.global.auth.implement.JwtTokenProvider.BEARER_PREFIX;

public class HttpHeaderUtil {

    public static final String AUTHORIZATION_HEADER = "accessToken";

    private HttpHeaderUtil() {
        throw new IllegalArgumentException("Utility class");
    }

    /**
     * 액세스 토큰을 Authorization 헤더에 추가
     *
     * @param response    응답 헤더 객체
     * @param accessToken  액세스 토큰 값
     */
    public static void setAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
    }

    /**
     * 요청에서 액세스 토큰 추출
     *
     * @param request 요청 객체
     * @return 액세스 토큰 값 (존재하지 않으면 AuthException(INVALID_TOKEN) 발생)
     */
    public static String getAccessToken(HttpServletRequest request, String bearerPrefix) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(bearerPrefix)) {
            throw new AuthException(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND);
        }
        return header.substring(bearerPrefix.length());
    }

    /**
     * 액세스 토큰 헤더 삭제 (로그아웃 등에서 사용)
     *
     * @param headers 응답 헤더 객체
     */
    public static void deleteAccessToken(HttpHeaders headers) {
        headers.remove(AUTHORIZATION_HEADER);
    }
}
