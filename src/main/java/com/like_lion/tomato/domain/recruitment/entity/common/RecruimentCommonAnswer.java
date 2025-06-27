package com.like_lion.tomato.domain.recruitment.entity.common;


import com.like_lion.tomato.domain.recruitment.entity.Applycations;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecruimentCommonAnswer {

    @DomainId(DomainType.RECRUITMENT_COMMON_ANSWER)
    @Id
    @Column(name = "recruiment_common_answer_id")
    private String id;

    @Column(length = 500)
    String answer;


    // 지원서 열람시 답변도 필요하므로 즉시조회
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id")
    private Applycations applycation;

    @ManyToOne
    @JoinColumn(name = "recruiment_common_question_id")
    private RecruimentCommonQuestion question;

    public void setQuestion(RecruimentCommonQuestion question) {
        this.question = question;
    }
}