package com.like_lion.tomato.domain.recruitment.dto.question;

import com.like_lion.tomato.domain.recruitment.entity.common.RecruitmentCommonQuestion;
import com.like_lion.tomato.domain.recruitment.entity.part.RecruitmentPartQuestion;
import com.like_lion.tomato.global.common.enums.Part;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionInfo(
        List<CommonQuestion> commonQuestions,
        List<PartQuestion> partQuestions
) {
    public record Detail(
            String id,
            String questionText,
            int orderIndex,
            int answerLimit,
            LocalDateTime createdAt
    ) {
    }

    public record CommonQuestion(
            String questionId,
            String questionText,
            Integer sortOrder
    ) {
        public static CommonQuestion from(RecruitmentCommonQuestion question) {
            return new CommonQuestion(
                    question.getId(),
                    question.getQuestionText(),
                    question.getSortOrder()
            );
        }
    }

    public record PartQuestion(
            String questionId,
            String questionText,
            Integer sortOrder,
            Part part
    ) {
        public static PartQuestion from(RecruitmentPartQuestion question) {
            return new PartQuestion(
                    question.getId(),
                    question.getQuestionText(),
                    question.getSortOrder(),
                    question.getPart()
            );
        }
    }
}
