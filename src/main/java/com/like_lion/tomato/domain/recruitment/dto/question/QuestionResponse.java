package com.like_lion.tomato.domain.recruitment.dto.question;

import java.util.List;

public record QuestionResponse(
        String part,
        int uploadedCount,
        List<QuestionInfo.Detail> questions
) {
}
