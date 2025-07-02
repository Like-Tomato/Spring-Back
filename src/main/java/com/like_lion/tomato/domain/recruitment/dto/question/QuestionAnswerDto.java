package com.like_lion.tomato.domain.recruitment.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.recruitment.entity.common.RecruitmentCommonAnswer;
import com.like_lion.tomato.domain.recruitment.entity.part.RecruitmentPartAnswer;

public record QuestionAnswerDto(
        @JsonProperty("question_id")
        String questionId,
        String question,
        String answer
) {
    public static QuestionAnswerDto fromCommon(RecruitmentCommonAnswer commonAnswer) {
        return new QuestionAnswerDto(
                commonAnswer.getQuestionId(),
                commonAnswer.getQuestion() != null ? commonAnswer.getQuestion().getQuestionText() : "",
                commonAnswer.getAnswer()
        );
    }

    public static QuestionAnswerDto fromPart(RecruitmentPartAnswer partAnswer) {
        return new QuestionAnswerDto(
                partAnswer.getQuestionId(),
                partAnswer.getQuestion() != null ? partAnswer.getQuestion().getQuestionText() : "",
                partAnswer.getAnswer()
        );
    }
}
