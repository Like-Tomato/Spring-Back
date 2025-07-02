package com.like_lion.tomato.domain.member.entity;

import com.like_lion.tomato.domain.member.dto.request.UpdateMemberProfileReq;
import com.like_lion.tomato.global.auth.model.Role;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // 구글 로그인만 진행하므로 provider, provider_id 필드는 형식상 존재, 사용x, 만약 네이버, 카카오 로그인 확장시 추가 구현
    @Column(unique = true)
    private String providerId;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String fileKey;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Part part;

    @Column
    private String links;

    @Column(nullable = false)
    private String major;

    @Column(length = 200)
    private String introduce;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isSubscribed;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isApplied;

    // 최신 기수를 따로 저장하는 필드(DB에는 저장안함)
    @Transient
    private Integer latestYear;

    @PostLoad
    private void setLatestYear() {
        this.latestYear = this.getLatestGenerationYearOptionalInteger().orElse(null);
    }


    @OneToMany(mappedBy = "member")
    private List<MemberGeneration> memberGenerations = new ArrayList<>();

    @Column
    private String phone;

    @Column
    private String studentId;

    @Column
    private String portfolioUrl;

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

    public void update(UpdateMemberProfileReq req) {
        if (req.getName() != null) this.username = req.getName();
        if (req.getMajor() != null) this.major = req.getMajor();
        if (req.getIntroduce() != null) this.introduce = req.getIntroduce();
        if (req.getLinks() != null) this.links = req.getLinks();
        if (req.getFileKey() != null) this.fileKey = req.getFileKey();
        // 필요하다면 email, phone 등 추가 필드도 여기에 업데이트
    }

    // 관리자가 멤버 수정 가능하도록
    public void updatePart(Part part) {
        this.part = part;
    }

    public void updateRole(Role role) {
        this.role = role;
    }



    /**
     public void addassignment(Assignment assignment) {
     assignments.add(assignment);
     assignment.setMember(this);
     }
     **/

    // Service에서  .orElseThrow(() -> new MemberException(MemberErrorCode.GENERATION_NOT_FOUND)) 던지기 위한 설계
    private Optional<Integer> getLatestGenerationYearOptionalInteger() {
        return this.getMemberGenerations().stream()
                .map(MemberGeneration::getGeneration)
                .map(Generation::getYear)
                .max(Integer::compareTo);
    }

    @Builder
    public Member(String username, String email, String providerId, String major, String introduce, String links, String provider, String fileKey, Role role, Part part, boolean isApplied, boolean isSubscribed) {
        this.username = username;
        this.email = email;
        this.providerId = providerId;
        this.provider = provider;
        this.fileKey = fileKey;
        this.role = role;
        this.part = part;
        this.introduce = introduce;
        this.major = major;
        this.links = links;
        this.isSubscribed  = isSubscribed;
        this.isApplied = isApplied;
    }

    public void updateApplicationInfo(String username, String phone, String studentId,
                                      String major, Part part, String portfolioUrl) {
        this.username = username;
        this.phone = phone;
        this.studentId = studentId;
        this.major = major;
        this.part = part;
        this.portfolioUrl = portfolioUrl;
    }

    public boolean hasGuestRoleOrHigher() {
        return this.role != null &&
                (this.role == Role.ROLE_GUEST ||
                        this.role == Role.ROLE_MEMBER ||
                        this.role == Role.ROLE_ADMIN ||
                        this.role == Role.ROLE_MASTER
                );
    }

    public boolean hasAdminRoleOrHigher() {
        return this.role != null &&
                (this.role == Role.ROLE_ADMIN || this.role == Role.ROLE_MASTER);
    }
}
