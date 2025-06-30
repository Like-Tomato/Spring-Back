package com.like_lion.tomato.domain.member.dto.response;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.entity.Part;
import com.like_lion.tomato.global.auth.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberProfileRes {

    private String id;
    private String name;
    private Role role;
    private String major;
    private String email;
    private String introduce;
    private int year;
    private String links;
    private String profileUrl;
    private Part part;
    private boolean isActive;
    private boolean isSubcribed;
    private boolean isApplied;

    @Builder
    public MemberProfileRes(String id, String username, String major, String email, String introduce, int year, String links, String profileUrl,Role role, Part part, boolean isActive, boolean isSubcribed, boolean isApplied) {
        this.id = id;
        this.name = username;
        this.major = major;
        this.email = email;
        this.introduce = introduce;
        this.year = year;
        this.links = links;
        this.profileUrl = profileUrl;
        this.part = part;
        this.role = role;
        this.isActive = isActive;
        this.isSubcribed = isSubcribed;
        this.isApplied = isApplied;
    }

    public static MemberProfileRes from(Member member) {
        return MemberProfileRes.builder()
                .id(member.getId())
                .username(member.getUsername())
                .major(member.getMajor())
                .email(member.getEmail())
                .year(member.getLatestYear())
                .introduce(member.getIntroduce())
                .links(member.getLinks())
                .profileUrl(member.getProfileUrl())
                .part(member.getPart())
                .isSubcribed(member.isSubscribed())
                .isApplied(member.isApplied())
                .build();
    }



}
