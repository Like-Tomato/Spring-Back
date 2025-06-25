package com.like_lion.tomato.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class MemberGeneration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_generation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "generation_id")
    private Generation generation;

    public void associate(Member member, Generation generation) {
        this.member = member;
        this.generation = generation;
        member.addMemberGeneration(this);
        generation.addMemberGeneration(this);
    }

    //필요시 연관관계 제거 메서드 추가
}
