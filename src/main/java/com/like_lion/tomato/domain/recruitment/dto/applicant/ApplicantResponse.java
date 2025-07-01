package com.like_lion.tomato.domain.recruitment.dto.applicant;

import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationResponse;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionAnswerDto;
import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.domain.recruitment.service.application.ApplicantService;
import com.like_lion.tomato.global.common.enums.Part;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Builder
public record ApplicantResponse(
        String id,
        String name,
        String major,
        String studentId,
        String email,
        String phone,
        List<QuestionAnswerDto> commonAnswers,
        List<QuestionAnswerDto> partAnswers,
        String portfolioUrls,
        Part part,
        LocalDateTime appliedAt,
        String reviewerId
) {
    @Builder
    public static ApplicantResponse from(Application application, String reviewerId) {
        List<QuestionAnswerDto> commonAnswers = application.getCommonAnswers().stream()
                .map(QuestionAnswerDto::fromCommon)
                .toList();

        List<QuestionAnswerDto> partAnswers = application.getPartAnswers().stream()
                .map(QuestionAnswerDto::fromPart)
                .toList();

        return ApplicantResponse.builder()
                .id(application.getId())
                .name(application.getUsername())
                .major(application.getMajor())
                .studentId(application.getStudentId())
                .email(application.getMember().getEmail())
                .phone(application.getPhone())
                .commonAnswers(commonAnswers)
                .partAnswers(partAnswers)
                .portfolioUrls(application.getPortfolioUrl())
                .part(application.getPart())
                .appliedAt(application.getSubmittedAt())
                .reviewerId(reviewerId)
                .build();
    }

    public static ApplicantResponse fromPart(Application application, Part part, String reviewerId) {
        List<QuestionAnswerDto> commonAnswers = application.getCommonAnswers().stream()
                .map(QuestionAnswerDto::fromCommon)
                .toList();

        List<QuestionAnswerDto> partAnswers = application.getPartAnswers().stream()
                .map(QuestionAnswerDto::fromPart)
                .toList();

        return ApplicantResponse.builder()
                .id(application.getId())
                .name(application.getUsername())
                .major(application.getMajor())
                .studentId(application.getStudentId())
                .email(application.getMember().getEmail())
                .phone(application.getPhone())
                .commonAnswers(commonAnswers)
                .partAnswers(partAnswers)
                .portfolioUrls(application.getPortfolioUrl())
                .part(part)
                .appliedAt(application.getSubmittedAt())
                .reviewerId(reviewerId)
                .build();
    }
}
