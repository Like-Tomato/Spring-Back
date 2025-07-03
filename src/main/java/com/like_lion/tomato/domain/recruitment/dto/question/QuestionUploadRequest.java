package com.like_lion.tomato.domain.recruitment.dto.question;

import java.util.List;

public record QuestionUploadRequest(
        List<Detail> questions
) {
    public record Detail(
            String questionText,
            int orderIndex,
            int answerLimit
    ) {}
}
