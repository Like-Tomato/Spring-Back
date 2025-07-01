package com.like_lion.tomato.domain.recruitment.entity.subscription;

import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "email_subscriptions")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSubscription extends BaseEntity {

    @DomainId(DomainType.RECRUITMENT_EMAIL_SUBSCRIPTION)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDateTime subscribedAt;
}
