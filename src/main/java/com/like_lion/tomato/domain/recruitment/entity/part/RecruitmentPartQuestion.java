package com.like_lion.tomato.domain.recruitment.entity.part;

import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "recruitment_part_questions")
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

    @Builder
    private RecruitmentPartQuestion(Part part, Integer sortOrder, String questionText) {
        this.part = part;
        this.sortOrder = sortOrder;
        this.questionText = questionText;
    }
}
