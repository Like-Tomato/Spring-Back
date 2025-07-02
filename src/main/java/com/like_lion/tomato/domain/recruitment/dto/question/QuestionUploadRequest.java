package com.like_lion.tomato.domain.recruitment.dto.question;

import java.util.List;

public record QuestionUploadRequest(
        List<QuestionDetail> questions
) {
    public record QuestionDetail(
            String questionText,
            boolean isRequired,
            int orderIndex
    ) {}
}
