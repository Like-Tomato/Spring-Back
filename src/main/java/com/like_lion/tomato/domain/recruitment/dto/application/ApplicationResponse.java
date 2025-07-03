package com.like_lion.tomato.domain.recruitment.dto.application;

import com.like_lion.tomato.domain.recruitment.dto.question.QuestionAnswerDto;
import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.global.common.enums.Part;

import java.time.LocalDateTime;
import java.util.List;

public record ApplicationResponse(
        String id,
        String memberId,
        String name,
        String phone,
        String studentId,
        String major,
        Part part,
        String portfolioUrl,
        List<QuestionAnswerDto> commonAnswers,
        List<QuestionAnswerDto> partAnswers,
        String accessToken,
        LocalDateTime createdAt,
        boolean isSubmitted
) {
    public static ApplicationResponse from(
            Application application, String accessToken
    ) {
        return new ApplicationResponse(
                application.getId(),
                application.getMember().getId(),
                application.getUsername(),
                application.getPhone(),
                application.getStudentId(),
                application.getMajor(),
                application.getPart(),
                application.getPortfolioUrl(),
                application.getCommonAnswers().stream()
                        .map(QuestionAnswerDto::fromCommon)
                        .toList(),
                application.getPartAnswers().stream()
                        .map(QuestionAnswerDto::fromPart)
                        .toList(),
                accessToken,
                application.getCreatedAt(),
                application.isSubmitted()
        );
    }

}