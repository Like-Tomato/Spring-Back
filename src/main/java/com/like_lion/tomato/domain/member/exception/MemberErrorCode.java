package com.like_lion.tomato.domain.member.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    USER_NOT_FOUND(404,"사용자를 찾을 수 없습니다.");

    private final int status;
    private final String message;
}
