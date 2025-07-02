package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionInfo;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionInfo.CommonQuestion;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionUploadRequest;
import com.like_lion.tomato.domain.recruitment.entity.common.RecruitmentCommonQuestion;
import com.like_lion.tomato.domain.recruitment.entity.part.RecruitmentPartQuestion;
import com.like_lion.tomato.domain.recruitment.repository.common.RecruitmentCommonQuestionRepository;
import com.like_lion.tomato.domain.recruitment.repository.part.RecruitmentPartQuestionRepository;
import com.like_lion.tomato.global.common.enums.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.like_lion.tomato.domain.recruitment.dto.question.QuestionInfo.*;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final RecruitmentCommonQuestionRepository commonQuestionRepository;
    private final RecruitmentPartQuestionRepository partQuestionRepository;
    private final MemberService memberService;

    public QuestionResponse uploadQuestions(Part part, QuestionUploadRequest request, String authorization) {

        Member uploader = memberService.extractMemberFromToken(authorization);

        if (!uploader.hasAdminRoleOrHigher()) {
            throw new AuthException((AuthErrorCode.ADMIN_REQUIRED));
        }

        List<QuestionInfo.Detail> questionResponses;

        if (part == null) {
            questionResponses = uploadCommonQuestions(request.questions());
        } else {
            questionResponses = uploadPartQuestions(part, request.questions());
        }

        return new QuestionResponse(
                part != null ? part.name() : "COMMON",
                questionResponses.size(),
                questionResponses
        );
    }

    private List<QuestionInfo.Detail> uploadCommonQuestions(List<QuestionUploadRequest.QuestionDetail> questions) {
        List<RecruitmentCommonQuestion> commonQuestions = questions.stream()
                .map(request -> RecruitmentCommonQuestion.builder()
                        .questionText(request.questionText())
                        .sortOrder(request.orderIndex())
                        .build())
                .toList();

        List<RecruitmentCommonQuestion> savedQuestions = commonQuestionRepository.saveAll(commonQuestions);

        return savedQuestions.stream()
                .map(question -> new QuestionInfo.Detail(
                        question.getId(),
                        question.getQuestionText(),
                        true,
                        question.getSortOrder(),
                        question.getCreatedAt()
                ))
                .toList();
    }

    private List<QuestionInfo.Detail> uploadPartQuestions(Part part, List<QuestionUploadRequest.QuestionDetail> questions) {
        List<RecruitmentPartQuestion> partQuestions = questions.stream()
                .map(request -> RecruitmentPartQuestion.builder()
                        .part(part)
                        .questionText(request.questionText())
                        .sortOrder(request.orderIndex())
                        .build())
                .toList();

        List<RecruitmentPartQuestion> savedQuestions = partQuestionRepository.saveAll(partQuestions);

        return savedQuestions.stream()
                .map(question -> new QuestionInfo.Detail(
                        question.getId(),
                        question.getQuestionText(),
                        true,
                        question.getSortOrder(),
                        question.getCreatedAt()
                ))
                .toList();
    }

    public QuestionInfo getQuestionsByPart(Part part) {
        List<CommonQuestion> commonQuestions = commonQuestionRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(CommonQuestion::from)
                .toList();

        List<PartQuestion> partQuestions = partQuestionRepository.findByPartOrderBySortOrderAsc(part)
                .stream()
                .map(PartQuestion::from)
                .toList();

        return new QuestionInfo(commonQuestions, partQuestions);
    }
}
