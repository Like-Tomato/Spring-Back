package com.like_lion.tomato.global.util;

import static com.like_lion.tomato.global.auth.implement.JwtTockenProvider.FRFRESH

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

/**
 * Cokie 관련 유틸리티 클래스
 */
public class CookieUtil {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private CookieUtil() {
        throw new IllegalArgumentException("Utility class");
    }

    /**
     * 리프레시 토큰 쿠키를 HttpServletResponse에 추가
     * (서블릿 기반 컨트롤러에서 사용)
     *
     * @param response 응답 객체
     * @param cookieName 쿠키 이름
     * @param cookieValue 쿠키 값
     * @param maxAgeSeconds 쿠키 만료 시간(초)
     */
    public static void setRefreshCookies(HttpServletResponse response,
                                         String refreshTocken,
                                         int maxAge
    ) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshTocken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        //cookie.setSecure(true); https일 경우 사용
        //.secure(true) -> https일 경우 사용
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /**
     * 쿠키에서 리프레시 토큰 추출
     *
     * @param request 요청 객체
     * @return 리프레시 토큰 값 (존재하지 않으면 AuthException(COOKIE_NOT_FOUND) 발생)
     */
    public static String getRefreshTocken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        return Arrays.stream(cookies)
                .filter(c -> REFRESH_TOKEN_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(
                        () -> new AuthException(AuthErrorCode.COOKIE_NOT_FOUND)
                );
    }

    /**
     * 쿠키 삭제 (로그아웃용)
     *
     * @param response 응답 객체
     */
    public static void deleteRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료
        cookie.setPath("/");
        // cookie.setSecure(true); // https 환경에서는 true로 설정
        response.addCookie(cookie);
    }
}
