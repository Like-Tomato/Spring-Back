package com.like_lion.tomato.domain.recruitment.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class RecruitmentException extends CustomException {
    public RecruitmentException(RecruitmentErrorCode errorCode) { super(errorCode); }
}

