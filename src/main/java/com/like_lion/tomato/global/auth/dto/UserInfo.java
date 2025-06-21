package com.like_lion.tomato.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo {
    // OAuth2user에 담길 정보들
    // 인증에 관련된 최소 정보만 가지고 옴
    private Long id;
    private String username;
    private String email;
    private String profileImageUrl; // (기본 구글, 이후 프로필 수정)
    private String role;

    @Builder
    public UserInfo(Long id, String username, String email, String profileImageUrl, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    // Member -> UserInfo 생성자 만들기

}
