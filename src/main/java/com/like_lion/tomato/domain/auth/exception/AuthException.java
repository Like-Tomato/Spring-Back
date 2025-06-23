package com.like_lion.tomato.domain.auth.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class AuthException extends CustomException {
    public AuthException(AuthErrorCode errorCode) { super(errorCode); }
}

