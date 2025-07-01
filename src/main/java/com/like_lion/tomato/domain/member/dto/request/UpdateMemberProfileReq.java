package com.like_lion.tomato.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
public class UpdateMemberProfileReq {

    private String name; // username 대신 name으로 통일
    private String major;
    private String introduce;
    private String links;
    private MultipartFile profileImg; // 이미지 파일

    @Builder
    public UpdateMemberProfileReq(String name, String major, String introduce, String links, MultipartFile profileImg) {
        this.name = name;
        this.major = major;
        this.introduce = introduce;
        this.links = links;
        this.profileImg = profileImg;
    }
}
