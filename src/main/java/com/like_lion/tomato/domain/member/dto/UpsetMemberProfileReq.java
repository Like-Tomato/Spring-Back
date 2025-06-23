package com.like_lion.tomato.domain.member.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
// Tostring 필요시
public class UpsetMemberProfileReq {

    private Long id;
    private String profileUrl; // memberfield 변경
    private String username;
    private String major;
    private String contactEmail; // 인증문제...
    private String introduce;
    private String tech;
    private String link; // 여러 링크일 경우 어떻게 할건지

    @Builder
    public UpsetMemberProfileReq(String profileUrl, String username, String major, String contactEmail, String introduce, String tech) {
        this.profileUrl = profileUrl;
        this.username = username;
        this.major = major;
        this.contactEmail = contactEmail;
        this.introduce = introduce;
        this.tech = tech;
        // link 논의 후 여러 링크이면 어떻게 할지.
    }


}
