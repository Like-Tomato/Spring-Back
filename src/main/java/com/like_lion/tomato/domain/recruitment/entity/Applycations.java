package com.like_lion.tomato.domain.recruitment.entity;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Applycations {

    @DomainId(DomainType.APPLICATION)
    @Id
    @Column(name = "application_id")
    private String id;

    private String username;
    private String phone;
    private String major;
    private String part;
    private String profileUrl;
    private boolean satus; // 상태는 합격, 불합격?

    // Member:Application = 1:N관계인데 여러 해
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
