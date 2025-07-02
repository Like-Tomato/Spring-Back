package com.like_lion.tomato.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
public class UpdateMemberProfileReq {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("major")
    private String major;

    @JsonProperty("fileKey")
    private String fileKey; // MEMBER/UUID/12444.jpg

    @JsonProperty("introduce")
    private String introduce;

    @JsonProperty("links")
    private String links;

    @Builder
    public UpdateMemberProfileReq(String id, String email, String name, String major, String fileKey, String introduce, String links) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.major = major;
        this.fileKey = fileKey;
        this.introduce = introduce;
        this.links = links;
    }
}
