package com.like_lion.tomato.domain.session.entity.quiz;

import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SessionQuiz extends BaseEntity {

    @DomainId(DomainType.SESSION_QUIZ)
    @Id
    @Column(name = "session_quiz")
    private String id;

    @OneToMany(mappedBy = "sessionQuiz")
    private List<QuizQuestion> quizQuestions;


    public void addQuizQuestion(QuizQuestion quizQuestion) {
        quizQuestions.add(quizQuestion);
        quizQuestion.updateSessionQuiz(this);
    }

    public int getTotalCount() {
        return quizQuestions == null ? 0 : quizQuestions.size();
    }

}
