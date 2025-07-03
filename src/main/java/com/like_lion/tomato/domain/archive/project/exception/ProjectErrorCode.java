package com.like_lion.tomato.domain.archive.project.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements ErrorCode {
    DUPLICATE_PROJECT_TITLE(409, "이미 존재하는 프로젝트 제목입니다."),
    INVALID_TEAM_MEMBER(400, "존재하지 않는 팀 멤버입니다."),
    INVALID_TEAM_MEMBER_FORMAT(400, "팀 멤버 형식이 올바르지 않습니다. (파트_이름 형식)"),
    INVALID_PART(400, "유효하지 않은 파트입니다."),
    INVALID_MEMBER_NAME(400, "멤버 이름이 비어있습니다."),
    INVALID_DATE_RANGE(400, "시작일이 종료일보다 늦을 수 없습니다."),
    INVALID_FILE_FORMAT(400, "지원하지 않는 파일 형식입니다."),
    TOO_MANY_IMAGES(400, "이미지는 최대 5개까지 업로드 가능합니다."),
    PROJECT_NOT_FOUND(404, "프로젝트를 찾을 수 없습니다."),
    INVALID_TITLE_LENGTH(400, "제목은 3자 이상 100자 이하여야 합니다."),
    INVALID_DESCRIPTION_LENGTH(400, "설명은 10자 이상 1000자 이하여야 합니다."),
    INVALID_URL_FORMAT(400, "유효하지 않은 URL 형식입니다."),
    FILE_TOO_LARGE(400, "파일 크기가 너무 큽니다. (최대 5MB)"),
    INVALID_CATEGORY(400, "유효하지 않은 카테고리입니다."),
    FILE_UPLOAD_SERVICE_UNAVAILABLE(503, "파일 업로드 서비스를 사용할 수 없습니다."),
    INVALID_PAGE(400, "유효하지 않은 페이지입니다" ),
    INVALID_SIZE(400, "유효하지 않은 크기입니다.");

    private final int status;
    private final String message;
}
