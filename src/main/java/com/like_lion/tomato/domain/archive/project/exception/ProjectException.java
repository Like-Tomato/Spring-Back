package com.like_lion.tomato.domain.archive.project.exception;

import com.like_lion.tomato.global.exception.CustomException;

public class ProjectException extends CustomException {
    public ProjectException(ProjectErrorCode errorCode) { super(errorCode); }
}

