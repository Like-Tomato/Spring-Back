package com.like_lion.tomato.domain.session.entity.quiz;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class QuizOption {

    @DomainId(DomainType.QUIZ_OPTION)
    @Id
    @Column(name = "quiz_option")
    private String id;

    @Column(nullable = false, length = 200)
    private String content;

    private int option;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    // AI 파트에서 받아와서 처리하기(기본값 FALSE)
    private boolean isCollect;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_quiz")
    private SessionQuiz sessionQuiz;
}
