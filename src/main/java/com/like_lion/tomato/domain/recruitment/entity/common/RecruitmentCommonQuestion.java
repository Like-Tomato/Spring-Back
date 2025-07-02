package com.like_lion.tomato.domain.recruitment.entity.common;

import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruitment_common_questions")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentCommonQuestion extends BaseEntity {

    @DomainId(DomainType.RECRUITMENT_COMMON_QUESTION)
    @Id
    @Column(name = "recruitment_common_question_id")
    private String id;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public void updateQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
