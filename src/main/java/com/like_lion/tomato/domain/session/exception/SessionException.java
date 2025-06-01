package com.like_lion.tomato.domain.session.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class SessionException extends CustomException {
    public SessionException(SessionErrorCode errorCode) { super(errorCode); }
}

