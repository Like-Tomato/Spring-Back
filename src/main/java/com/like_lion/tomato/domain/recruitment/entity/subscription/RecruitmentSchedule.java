package com.like_lion.tomato.domain.recruitment.entity.subscription;

import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "recruitment_schedule")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentSchedule extends BaseEntity {

    @DomainId(DomainType.RECRUITMENT_SCHEDULE)
    @Id
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;
    private String description;
    private boolean isActive;
    private boolean emailSent;

    public void markEmailSent() {
        this.emailSent = true;
    }
}