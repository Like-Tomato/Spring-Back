package com.like_lion.tomato.domain.member.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenerationErrorCode implements ErrorCode {
    // 조회 및 기타
    GENERATION_NOT_FOUND(404,"기수를 찾을 수 없습니다");

    private final int status;
    private final String message;
}
