package com.like_lion.tomato.global.auth.dto;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.auth.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo {
    // OAuth2user에 담길 정보들
    // 인증에 관련된 최소 정보만 가지고 옴
    private String id;
    private String username;
    private String email;
    private String profileUrl; // (기본 구글, 이후 프로필 수정)
    private Role role;
    private String provider;

    @Builder
    public UserInfo(String id, String username, String email, String profileUrl, Role role, String provider) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileUrl = profileUrl;
        this.role = role;
        this.provider = provider;
    }

    public static UserInfo from(Member member) {
        return UserInfo.builder()
                .id(member.getId())
                .username(member.getUsername())
                .provider(member.getProvider())
                .role(member.getRole())
                .build();
    }
}
