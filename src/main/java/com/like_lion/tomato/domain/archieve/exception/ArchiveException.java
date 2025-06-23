package com.like_lion.tomato.domain.archieve.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class ArchiveException extends CustomException {
    public ArchiveException(ArchiveErrorCode errorCode) { super(errorCode); }
}

