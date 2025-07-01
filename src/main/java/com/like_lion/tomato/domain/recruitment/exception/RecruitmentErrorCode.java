package com.like_lion.tomato.domain.recruitment.exception;

import com.like_lion.tomato.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitmentErrorCode implements ErrorCode {
    // 잘못된 요청 오류
    INVALID_NAME_FORMAT(400, "올바른 이름 형식이 아닙니다"),
    INVALID_PHONE_FORMAT(400, "올바른 전화번호 형식이 아닙니다"),
    INVALID_STUDENT_ID_FORMAT(400, "올바른 학번 형식이 아닙니다"),
    INVALID_EMAIL_FORMAT(400, "올바른 이메일 형식이 아닙니다"),
    MISSING_COMMON_QUESTIONS(400, "공통 질문에 대한 답변이 누락되었습니다"),
    MISSING_PART_QUESTIONS(400, "파트별 질문에 대한 답변이 누락되었습니다"),
    INVALID_QUESTION_FORMAT(400, "질문 답변 형식이 올바르지 않습니다"),
    ANSWER_TOO_SHORT(400, "답변이 너무 짧습니다. (최소 50자 이상)"),
    ANSWER_TOO_LONG(400, "답변이 너무 깁니다. (최대 1000자)"),
    RECRUITMENT_CLOSED(400, "모집 기간이 아닙니다"),
    MODIFICATION_DEADLINE_PASSED(400, "지원서 수정 기한이 지났습니다"),
    RESULT_NOT_ANNOUNCED(400, "아직 결과가 발표되지 않았습니다"),

    // 리소스 없음 오류
    APPLICATION_NOT_FOUND(404, "지원서를 찾을 수 없습니다"),

    // 충돌 오류
    APPLICATION_ALREADY_EXISTS(409, "이미 지원서가 존재합니다"),
    APPLICATION_ALREADY_SUBMITTED(409, "이미 지원서가 제출되었습니다"),
    ALREADY_SUBSCRIBED(409, "이미 모집 알림을 신청하셨습니다");

    private final int status;
    private final String message;
}
