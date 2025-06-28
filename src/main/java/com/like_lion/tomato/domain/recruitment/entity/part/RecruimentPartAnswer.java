package com.like_lion.tomato.domain.recruitment.entity.part;


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
public class RecruimentPartAnswer {

    @DomainId(DomainType.RECRUITMENT_PART_ANSWER)
    @Id
    @Column(name = "recruiment_part_answer_id")
    private String id;

    @Column(length = 500)
    String answer;

    // 지원서 열람시 답변도 필요하므로 즉시조회
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id")
    private Applycations applycation;

    @ManyToOne
    @JoinColumn(name = "recruitment_part_question_id")
    private RecruimentPartQuestion question;

    public void setQuestion(RecruimentPartQuestion question) {
        this.question = question;
    }
}
