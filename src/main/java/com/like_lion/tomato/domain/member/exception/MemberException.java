package com.like_lion.tomato.domain.member.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class MemberException extends CustomException {
    public MemberException(MemberErrorCode errorCode) { super(errorCode); }
}

