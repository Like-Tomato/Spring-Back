package com.like_lion.tomato.domain.member.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    // 인증 관련
    AUTH_FAILED(401, "인증이 필요합니다."),
    // 권한 관련
    MEMBER_REQUIRED(403, "멤버 권한이 필요합니다."),
    MEMBER_PROFILE_PRIVATE(403, "비공개 프로필입니다."),
    // 충돌 및 중복
    MEMBER_PROFILE_ALREADY_EXISTS(409, "이미 멤버 소개가 존재합니다."),
    APPLICATION_ALREADY_EXISTS(409, "이미 지원서가 존재합니다."),
    // 유효성 검사
    INVALID_PART(400, "유효하지 않은 파트입니다."),
    INVALID_YEAR(400, "유효하지 않은 기수입니다."),
    INVALID_NAME_LENGTH(400, "이름은 2자 이상 50자 이하여야 합니다."),
    INVALID_BIO_LENGTH(400, "소개는 10자 이상 500자 이하여야 합니다."),
    INVALID_URL_FORMAT(400, "유효하지 않은 URL 형식입니다."),
    INVALID_FILE_FORMAT(400, "지원하지 않는 파일 형식입니다. (JPG, PNG, GIF만 가능)"),
    FILE_TOO_LARGE(400, "파일 크기가 너무 큽니다. (최대 2MB)"),
    INVALID_NAME_FORMAT(400, "올바른 이름 형식이 아닙니다."),
    INVALID_PHONE_FORMAT(400, "올바른 전화번호 형식이 아닙니다."),
    INVALID_STUDENT_ID_FORMAT(400, "올바른 학번 형식이 아닙니다."),
    INVALID_EMAIL_FORMAT(400, "올바른 이메일 형식이 아닙니다."),
    // 조회 및 기타
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "멤버를 찾을 수 없습니다."),
    APPLICATION_NOT_FOUND(404, "지원서를 찾을 수 없습니다.");


    private final int status;
    private final String message;
}
