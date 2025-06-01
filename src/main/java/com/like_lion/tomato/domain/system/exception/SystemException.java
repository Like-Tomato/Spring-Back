package com.like_lion.tomato.domain.system.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class SystemException extends CustomException {
    public SystemException(SystemErrorCode errorCode) { super(errorCode); }
}

