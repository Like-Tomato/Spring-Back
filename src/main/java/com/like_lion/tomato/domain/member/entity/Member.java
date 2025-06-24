package com.like_lion.tomato.domain.member.entity;

import com.like_lion.tomato.global.auth.model.Role;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @DomainId(DomainType.MEMBER)
    @Id
    @Column(name = "member_id")
    private String id;

    @Column(nullable = false, length = 20) // 동명이인 존재 가능성
    private String username;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(unique = true)
    private String providerId;

    @Column(nullable = false)
    private String provider;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 30)
    private String part;

    @Column(nullable = false)
    private String tech;

    @Column(length = 200)
    private String introduce;

    @Column
    private int year;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isSubscribed;

    // 구글 로그인만 진행하므로 provider, provider_id 필드는 형식상 존재, 사용x, 만약 네이버, 카카오 로그인 확장시 추가 구현


    @OneToMany(mappedBy = "member")
    private List<MemberGeneration> memberGenerations = new ArrayList<>();

    // 과제 구현 후 양방향 매핑 예정
    // my_page에 과제칸이 존재하므로 즉시조회 예정!
    //@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //private List<Assignment> assignments = new ArrayList<>();

    // 연관관계 편의 메서드

    public void addMemberGeneration(MemberGeneration memberGeneration) {
        memberGenerations.add(memberGeneration);
    }

    public void removeMemberGeneration(MemberGeneration memberGeneration) {
        memberGenerations.remove(memberGeneration);
    }

    /**
    public void addassignment(Assignment assignment) {
        assignments.add(assignment);
        assignment.setMember(this);
    }
     **/


    @Builder
    public Member(String username, String email, String providerId, String provider, String profileUrl, Role role, String part, String tech) {
        this.username = username;
        this.email = email;
        this.providerId = providerId;
        this.provider = provider;
        this.profileUrl = profileUrl;
        this.part = part;
        this.role = role;
        this.tech = tech;
        // 자기 소개는 프로필 페이지에서 수정하므로, builder에 추가 안함
    }


    // 기수 자동 생성(관리자 페이지에서 진행, 서비스 레이어로 이동 예정)
    @PrePersist
    public void generateYear() {
        int currentYear = LocalDate.now().getYear();
        int baseYear = 2025;
        this.year = currentYear - baseYear + 1;
    }


}
