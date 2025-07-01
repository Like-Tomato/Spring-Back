package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.member.entity.Part;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse.CommonQuestion;
import com.like_lion.tomato.domain.recruitment.repository.common.RecruitmentCommonQuestionRepository;
import com.like_lion.tomato.domain.recruitment.repository.part.RecruitmentPartQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse.*;

@RequiredArgsConstructor
@Service
public class RecruitmentQuestionService {
    private final RecruitmentCommonQuestionRepository commonQuestionRepository;
    private final RecruitmentPartQuestionRepository partQuestionRepository;

    public QuestionResponse getQuestionsByPart(Part part) {
        List<CommonQuestion> commonQuestions = commonQuestionRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(CommonQuestion::from)
                .toList();

        List<PartQuestion> partQuestions = partQuestionRepository.findByPartOrderBySortOrderAsc(part)
                .stream()
                .map(PartQuestion::from)
                .toList();

        return new QuestionResponse(commonQuestions, partQuestions);
    }
}
