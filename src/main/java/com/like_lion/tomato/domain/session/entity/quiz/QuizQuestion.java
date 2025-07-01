package com.like_lion.tomato.domain.session.entity.quiz;
import com.like_lion.tomato.global.common.BaseEntitiy;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class QuizQuestion extends BaseEntitiy {

    @DomainId(DomainType.QUIZ_QUESTION)
    @Id
    @Column(name = "quiz_question_id")
    private String id;

    @Column(nullable = false, length = 200)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_quiz")
    private SessionQuiz sessionQuiz;

    public void setSessionQuiz(SessionQuiz quizQuestion) {
        this.sessionQuiz = sessionQuiz;
    }

}
