package com.like_lion.tomato.domain.auth.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    // 인증 관련 오류
    AUTH_FAILED(401, "인증에 실패하였습니다"),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(401, "만료된 토큰입니다"),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다"),
    EXPIRED_REFRESH_TOKEN(401, "만료된 리프레시 토큰입니다"),
    GOOGLE_AUTH_FAILED(401, "구글 인증에 실패했습니다"),

    // 권한 관련 오류
    REFRESH_TOKEN_REVOKED(403, "폐기된 리프레시 토큰입니다"),
    ADMIN_REQUIRED(403, "관리자 권한이 필요합니다"),
    MEMBER_REQUIRED(403, "멤버 권한이 필요합니다"),
    ACCESS_DENIED(403, "접근이 거부되었습니다"),

    // 잘못된 요청 오류
    INVALID_AUTH_CODE(400, "유효하지 않은 인증 코드입니다"),
    INVALID_REDIRECT_URI(400, "유효하지 않은 리다이렉트 URI입니다"),

    // 외부 인증 서버 오류
    AUTH_SERVER_ERROR(500, "인증 서버 오류가 발생했습니다");

    private final int status;
    private final String message;
}
