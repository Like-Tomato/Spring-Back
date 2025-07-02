package com.like_lion.tomato.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.auth.model.Role;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileRes {

    @JsonProperty("id")
    private String id;

    @JsonProperty("contactEmail")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("major")
    private String major;

    @JsonProperty("year")
    private int year;

    @JsonProperty("image_file")
    private PresignedUrlRes imageFile;

    @JsonProperty("introduce")
    private String introduce;

    @JsonProperty("part")
    private Part part;

    @JsonProperty("links")
    private String links;

    @JsonProperty("role")
    private Role role;

    @Builder
    public MemberProfileRes(String id, String email, String name, String major, int year, PresignedUrlRes imageFile,
                            String introduce, Part part, String links, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.major = major;
        this.year = year;
        this.imageFile = imageFile;
        this.introduce = introduce;
        this.part = part;
        this.links = links;
        this.role = role;
    }

    public static MemberProfileRes from(Member member, PresignedUrlRes imageFile) {
        return MemberProfileRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getUsername())
                .major(member.getMajor())
                .year(member.getLatestYear())
                .imageFile(imageFile)
                .introduce(member.getIntroduce())
                .part(member.getPart())
                .links(member.getLinks())
                .role(member.getRole())
                .build();
    }
}
