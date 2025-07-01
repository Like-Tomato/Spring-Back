package com.like_lion.tomato.domain.recruitment.dto.application;

import com.like_lion.tomato.domain.member.entity.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ApplicationRequest(
        @NotBlank(message = "이름은 필수입니다")
        String name,

        @NotBlank(message = "전화번호는 필수입니다")
        @Pattern(regexp = "^010\\d{4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
        String phone,

        @NotBlank(message = "학번은 필수입니다")
        String studentId,

        @NotBlank(message = "전공은 필수입니다")
        String major,

        @NotNull(message = "지원 분야는 필수입니다")
        Part part,

        String portfolioUrl,

        @NotBlank(message = "공통 질문 답변은 필수입니다")
        String commonQuestions, // JSON string

        @NotBlank(message = "분야별 질문 답변은 필수입니다")
        String partQuestions // JSON string
) {
    public record QuestionAnswer(
            @NotBlank String questionId,
            @NotBlank String answer
    ) {
    }
}

