package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationRequest;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationRequest.QuestionAnswer;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationResponse;
import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.domain.recruitment.entity.common.RecruitmentCommonAnswer;
import com.like_lion.tomato.domain.recruitment.entity.part.RecruitmentPartAnswer;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentErrorCode;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentException;
import com.like_lion.tomato.domain.recruitment.repository.application.ApplicationRepository;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public ApplicationResponse submitApplication(@Valid ApplicationRequest request, String authorization) {
        Member member = memberService.extractMemberFromToken(authorization);

        if (applicationRepository.existsByMemberIdAndSubmittedAtIsNotNull(member.getId())) {
            throw new RecruitmentException(RecruitmentErrorCode.APPLICATION_ALREADY_EXISTS);
        }

        applicationRepository.deleteByMemberIdAndSubmittedAtIsNull(member.getId());

        member.updateApplicationInfo(
                request.name(),
                request.phone(),
                request.studentId(),
                request.major(),
                request.part(),
                request.portfolioUrl()
        );

        List<RecruitmentCommonAnswer> commonAnswerEntities = createCommonAnswers(request.commonQuestions());
        List<RecruitmentPartAnswer> partAnswerEntities = createPartAnswers(request.partQuestions());

        Application application = Application.builder()
                .username(request.name())
                .phone(request.phone())
                .studentId(request.studentId())
                .major(request.major())
                .part(request.part())
                .portfolioUrl(request.portfolioUrl())
                .member(member)
                .submittedAt(LocalDateTime.now())
                .build();

        application.addCommonAnswers(commonAnswerEntities);
        application.addPartAnswers(partAnswerEntities);

        Application savedApplication = applicationRepository.save(application);

        String accessToken = jwtTokenProvider.generateAccessTokenForMember(member);

        return ApplicationResponse.from(savedApplication, accessToken);
    }

    public ApplicationResponse getApplicationDetail(String authorization) {
        Member member = memberService.extractMemberFromToken(authorization);

        if (!applicationRepository.existsByMemberId(member.getId())) {
            throw new RecruitmentException(RecruitmentErrorCode.APPLICATION_NOT_FOUND);
        }

        Application application = applicationRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RecruitmentException(RecruitmentErrorCode.APPLICATION_NOT_FOUND));

        String accessToken = jwtTokenProvider.generateAccessTokenForMember(member);

        return ApplicationResponse.from(application, accessToken);
    }

    public ApplicationResponse draftApplication(ApplicationRequest request, String authorization) {
        Member member = memberService.extractMemberFromToken(authorization);

        if (applicationRepository.existsByMemberIdAndSubmittedAtIsNotNull(member.getId())) {
            throw new RecruitmentException(RecruitmentErrorCode.APPLICATION_ALREADY_SUBMITTED);
        }

        Optional<Application> existingDraft = applicationRepository.findByMemberIdAndSubmittedAtIsNull(member.getId());

        List<RecruitmentCommonAnswer> commonAnswerEntities = createCommonAnswersWithBlanks(request.commonQuestions());
        List<RecruitmentPartAnswer> partAnswerEntities = createPartAnswersWithBlanks(request.partQuestions());

        Application application;
        if (existingDraft.isPresent()) {
            application = updateExistingDraft(
                    existingDraft.get(),
                    request,
                    commonAnswerEntities,
                    partAnswerEntities
            );
        } else {
            application = Application.builder()
                    .username(request.name())
                    .phone(request.phone())
                    .studentId(request.studentId())
                    .major(request.major())
                    .part(request.part())
                    .portfolioUrl(request.portfolioUrl())
                    .member(member)
                    .submittedAt(null)
                    .build();

            application.addCommonAnswers(commonAnswerEntities);
            application.addPartAnswers(partAnswerEntities);
        }

        Application savedApplication = applicationRepository.save(application);
        String accessToken = jwtTokenProvider.generateAccessTokenForMember(member);

        return ApplicationResponse.from(savedApplication, accessToken);
    }

    private List<RecruitmentCommonAnswer> createCommonAnswersWithBlanks(List<QuestionAnswer> answers) {
        return answers.stream()
                .map(qa -> RecruitmentCommonAnswer.builder()
                        .questionId(qa.questionId())
                        .answer(qa.answer() != null ? qa.answer() : "")
                        .build())
                .toList();
    }

    private List<RecruitmentPartAnswer> createPartAnswersWithBlanks(List<QuestionAnswer> answers) {
        return answers.stream()
                .map(qa -> RecruitmentPartAnswer.builder()
                        .questionId(qa.questionId())
                        .answer(qa.answer() != null ? qa.answer() : "") // 빈값 허용
                        .build())
                .toList();
    }

    private List<RecruitmentCommonAnswer> createCommonAnswers(List<QuestionAnswer> answers) {
        return answers.stream()
                .map(qa -> RecruitmentCommonAnswer.builder()
                        .questionId(qa.questionId())
                        .answer(qa.answer())
                        .build())
                .toList();
    }

    private List<RecruitmentPartAnswer> createPartAnswers(List<QuestionAnswer> answers) {
        return answers.stream()
                .map(qa -> RecruitmentPartAnswer.builder()
                        .questionId(qa.questionId())
                        .answer(qa.answer())
                        .build())
                .toList();
    }

    private boolean isNotBlank(String str) { return str != null && !str.trim().isEmpty(); }

    private Application updateExistingDraft(
            Application existingDraft, ApplicationRequest request,
            List<RecruitmentCommonAnswer> commonAnswers,
            List<RecruitmentPartAnswer> partAnswers
    ) {
        existingDraft.clearAnswers();

        existingDraft.addCommonAnswers(commonAnswers);
        existingDraft.addPartAnswers(partAnswers);

        if (isNotBlank(request.name())) {
            existingDraft.updateUsername(request.name());
        }
        if (isNotBlank(request.phone())) {
            existingDraft.updatePhone(request.phone());
        }
        if (isNotBlank(request.studentId())) {
            existingDraft.updateStudentId(request.studentId());
        }
        if (isNotBlank(request.major())) {
            existingDraft.updateMajor(request.major());
        }
        if (request.part() != null) {
            existingDraft.updatePart(request.part());
        }
        if (isNotBlank(request.portfolioUrl())) {
            existingDraft.updatePortfolioUrl(request.portfolioUrl());
        }

        return existingDraft;
    }
}
