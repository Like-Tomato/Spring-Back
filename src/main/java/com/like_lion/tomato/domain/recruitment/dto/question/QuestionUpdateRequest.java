package com.like_lion.tomato.domain.recruitment.dto.question;

import java.util.List;

public record QuestionUpdateRequest(
        List<Detail> questions
) {

    public record Detail(
            String questionId,
            String questionText,
            Boolean isRequired,
            Integer orderIndex
    ) {}
}
