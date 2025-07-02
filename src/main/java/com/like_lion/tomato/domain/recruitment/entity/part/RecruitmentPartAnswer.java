package com.like_lion.tomato.domain.recruitment.entity.part;


import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "recruitment_part_answer")
public class RecruitmentPartAnswer {

    @DomainId(DomainType.RECRUITMENT_PART_ANSWER)
    @Id
    @Column(name = "recruitment_part_answer_id")
    private String id;

    @Column(name = "question_id", length = 20, nullable = false)
    private String questionId;

    @Column(length = 500)
    String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_part_question_id")
    private RecruitmentPartQuestion question;

    public void updateAnswer(String newAnswer) {
        this.answer = newAnswer;
    }
}
