package com.like_lion.tomato.domain.member.dto.response;

import com.like_lion.tomato.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberProfileRes {

    private String id;
    private String username;
    private String major;
    private String email;
    private String introduce;
    private List<String> links;
    private String profileUrl;
    private String part;
    private List<String> skills; // teck를 split해서 리스트로 변환
    private int projectCount;
    private boolean isActive;
    private boolean isSubcribed;
    // 생성 시각 추가하기

    @Builder
    public MemberProfileRes(String id, String username, String major, String email, String introduce, List<String> links, String profileUrl, String part, List<String> skills, int projectCount, boolean isActive, boolean isSubcribed) {
        this.id = id;
        this.username = username;
        this.major = major;
        this.email = email;
        this.introduce = introduce;
        this.links = links;
        this.profileUrl = profileUrl;
        this.part = part;
        this.skills = skills;
        this.projectCount = projectCount;
        this.isActive = isActive;
        this.isSubcribed = isSubcribed;
    }

    public static MemberProfileRes from(Member member) {
        return MemberProfileRes.builder()
                .id(member.getId())
                .username(member.getUsername())
                .major(member.getMajor())
                .email(member.getEmail())
                .introduce(member.getIntroduce())
                .links(List.of(member.getLinks()))
                .profileUrl(member.getProfileUrl())
                .part(member.getPart())
                .skills(List.of(member.getTechs()))
                .projectCount(member.getProjectCount())
                .isSubcribed(member.isSubscribed())
                .isActive(member.isActive())
                .build();
    }



}
