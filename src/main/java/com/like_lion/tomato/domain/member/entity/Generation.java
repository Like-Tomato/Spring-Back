package com.like_lion.tomato.domain.member.entity;

import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Generation {

    @Id
    @DomainId(DomainType.GENERATION)
    @Column(name = "generation_id")
    private String id;

    @Column
    private int year;

    @Enumerated(EnumType.STRING)
    private Part part;

    @OneToMany(mappedBy = "generation")
    private List<MemberGeneration> memberGenerations = new ArrayList<>();

    // 상수 정의
    public static final int BASE_YEAR = 2012;

    // 연관관계 편의 메서드
    public void addMemberGeneration(MemberGeneration memberGeneration) {
        memberGenerations.add(memberGeneration);
    }

    public void removeMemberGeneration(MemberGeneration memberGeneration) {
        memberGenerations.remove(memberGeneration);
    }

    // 기수 자동 생성(관리자 페이지에서 진행)
    @PrePersist
    public void generateYear() {
        int currentYear = LocalDate.now().getYear();
        this.year = currentYear - BASE_YEAR + 1;
    }

    // 최대 기수
    public static int getMaxYear() {
        int currentYear = LocalDate.now().getYear();
        return currentYear - BASE_YEAR + 1;
    }
    // 기수 유효성 검사(필요시 Valid로 리팩터링!)
    public static boolean isValidYear(int year) {
        return year >= 1 && year <= getMaxYear();
    }

}
