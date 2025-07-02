package com.like_lion.tomato.domain.member.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class GenerationException extends CustomException {
    public GenerationException(GenerationErrorCode errorCode) { super(errorCode); }
}

