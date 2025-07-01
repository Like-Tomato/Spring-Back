package com.like_lion.tomato.domain.recruitment.entity.common;


import com.like_lion.tomato.global.common.BaseEntity;
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
public class RecruitmentCommonAnswer extends BaseEntity {

    @DomainId(DomainType.RECRUITMENT_COMMON_ANSWER)
    @Id
    @Column(name = "recruitment_common_answer_id")
    private String id;

    @Column(name = "question_id", length = 20, nullable = false)
    private String questionId;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT",  length = 500)
    String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_common_question_id")
    private RecruitmentCommonQuestion question;

    public void updateAnswer(String newAnswer) {
        this.answer = newAnswer;
    }
}