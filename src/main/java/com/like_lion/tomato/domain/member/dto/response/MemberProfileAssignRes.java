package com.like_lion.tomato.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberProfileAssignRes {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("part")
    private final String part;

    @JsonProperty("year")
    private final int year;

    @JsonProperty("profile_image")
    private final PresignedUrlRes profileImage;

    @JsonProperty("sessions")
    private final List<SessionWithState> sessions;

    @Builder
    public MemberProfileAssignRes(String id, String name, String part, int year, PresignedUrlRes profileImage, List<SessionWithState> sessions) {
        this.id = id;
        this.name = name;
        this.part = part;
        this.year = year;
        this.profileImage = profileImage;
        this.sessions = sessions;
    }

    public static MemberProfileAssignRes from(Member member, List<SessionWithState> sessions, PresignedUrlRes profileImage) {
        return MemberProfileAssignRes.builder()
                .id(member.getId())
                .name(member.getUsername())
                .part(member.getPart().name())
                .year(member.getLatestYear())
                .profileImage(profileImage)
                .sessions(sessions)
                .build();
    }
}
