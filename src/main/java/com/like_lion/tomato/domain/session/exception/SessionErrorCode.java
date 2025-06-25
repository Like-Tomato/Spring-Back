package com.like_lion.tomato.domain.session.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionErrorCode implements ErrorCode {
    // 잘못된 요청 오류
    INVALID_PART(400, "유효하지 않은 파트입니다"),
    ASSIGNMENT_DEADLINE_PASSED(400, "과제 제출 기한이 지났습니다"),
    MISSING_CONTENT(400, "제출 내용이 없습니다"),

    // 권한 관련 오류
    SESSION_NOT_ACCESSIBLE(403, "해당 세션에 접근할 수 없습니다"),

    // 리소스 없음 오류
    PART_NOT_FOUND(404, "해당 파트를 찾을 수 없습니다");

    private final int status;
    private final String message;
}
