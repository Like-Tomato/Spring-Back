package com.like_lion.tomato.domain.recruitment.dto.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.global.common.enums.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ApplicationRequest(
        String name,

        @Pattern(regexp = "^010\\d{4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
        String phone,

        @JsonProperty("student_id")
        String studentId,

        String major,

        Part part,

        String portfolioUrl,

        @JsonProperty("common_questions")
        List<QuestionAnswer> commonQuestions,

        @JsonProperty("part_questions")
        List<QuestionAnswer> partQuestions
) {
    public record QuestionAnswer(
            @JsonProperty("question_id")
            String questionId,

            String answer
    ) {
    }
}

