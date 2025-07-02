package com.like_lion.tomato.domain.member.dto.response;

import com.like_lion.tomato.domain.member.dto.response.SessionWithState;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberProfileAssignRes {
    private final String id;
    private final String name;
    private final String part;
    private final int year;
    private final PresignedUrlRes profileImage;
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
