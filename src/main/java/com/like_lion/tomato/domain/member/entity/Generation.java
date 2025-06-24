package com.like_lion.tomato.domain.member.entity;

import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Generation {

    @Id
    @DomainId(DomainType.Generation)
    @Column(name = "generation_id")
    private String id;

    @Column
    private int year;

    @OneToMany(mappedBy = "generation")
    private List<MemberGeneration> memberGenerations = new ArrayList<>();

    public void addMemberGeneration(MemberGeneration memberGeneration) {
        memberGenerations.add(memberGeneration);
    }

    public void removeMemberGeneration(MemberGeneration memberGeneration) {
        memberGenerations.remove(memberGeneration);
    }




}
