package com.like_lion.tomato.domain.session.dto.response;

import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionDetailRes {
    private final String id;
    private final int year;
    private final String part;
    private final String title;
    private final String assignmentDescription;
    private final String assignmentLinks; // "link1, link2, ..." 형태의 문자열
    private final String createdAt;
    private final String endedAt;
    private final PresignedUrlRes presignedUrlRes;

    @Builder
    private SessionDetailRes(String id, int year, String part, String title,
                             String assignmentDescription, String assignmentLinks,
                             String createdAt, String endedAt, PresignedUrlRes presignedUrlRes) {
        this.id = id;
        this.year = year;
        this.part = part;
        this.title = title;
        this.assignmentDescription = assignmentDescription;
        this.assignmentLinks = assignmentLinks;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.presignedUrlRes = presignedUrlRes;
    }

    /**
     * Session 엔티티와 PresignedUrlRes를 받아서 DTO로 변환
     */
    public static SessionDetailRes from(Session session, PresignedUrlRes presignedUrlRes) {
        return SessionDetailRes.builder()
                .id(session.getId())
                .year(session.getGeneration().getYear())
                .part(session.getPart().name())
                .title(session.getTitle())
                .assignmentDescription(session.getAssignmentDdescription())
                .assignmentLinks(session.getAssignmentLinks()) // 반드시 "link1,link2,..." 형태의 문자열
                .createdAt(session.getCreatedAt().toString())
                .endedAt(session.getEndedAt().toString())
                .presignedUrlRes(presignedUrlRes)
                .build();
    }
}
