package com.like_lion.tomato.domain.auth.entity;

import com.like_lion.tomato.global.common.BaseEntitiy;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseEntitiy {

    @DomainId(DomainType.REFRESH_TOKEN)
    @Id
    @Column(name = "refresh_token_id")
    private String id;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String username;

    @Builder
    public RefreshToken(String payload, String username) {
        this.payload = payload;
        this.username = username;
    }

    public RefreshToken updatePayload(String newPayload) {
        this.payload = newPayload;
        return this;
    }
}
