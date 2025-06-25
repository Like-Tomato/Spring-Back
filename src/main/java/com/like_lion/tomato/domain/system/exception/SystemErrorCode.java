package com.like_lion.tomato.domain.system.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemErrorCode implements ErrorCode {
    // 잘못된 요청 오류
    INVALID_CATEGORY(400, "유효하지 않은 카테고리입니다"),
    INVALID_SORT_OPTION(400, "유효하지 않은 정렬 옵션입니다"),
    INVALID_STATUS(400, "유효하지 않은 상태값입니다"),
    TOO_MANY_IMAGES(400, "이미지는 최대 10개까지 업로드 가능합니다"),
    INVALID_TEAM_MEMBER(400, "존재하지 않는 팀 멤버입니다"),

    // 리소스 없음 오류
    GALLERY_NOT_FOUND(404, "갤러리를 찾을 수 없습니다");

    private final int status;
    private final String message;
}
