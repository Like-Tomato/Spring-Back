package com.like_lion.tomato.domain.recruitment.entity.part;

import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruitment_part_questions")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentPartQuestion extends BaseEntity {

    @DomainId(DomainType.RECRUITMENT_PART_QUESTION)
    @Id
    @Column(name = "recruitment_part_question_id")
    private String id;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "part", nullable = false, length = 10)
    private Part part;

    public void updateQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
