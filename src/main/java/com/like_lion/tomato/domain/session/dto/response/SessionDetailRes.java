package com.like_lion.tomato.domain.session.dto.response;

import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.domain.session.entity.session.Session;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionDetailRes {
    private String id;
    private int year;
    private String part;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private String sessionUrl;
    private final AssignmentSubmissionRes assignment; // null 가능

    public SessionDetailRes(String id, int year, String part, String title,
                            LocalDateTime createdAt, LocalDateTime endedAt,
                            String sessionUrl, AssignmentSubmissionRes assignment) {
        this.id = id;
        this.year = year;
        this.part = part;
        this.title = title;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.sessionUrl = sessionUrl;
        this.assignment = assignment;
    }


    public static SessionDetailRes of(Session session, AssignmentSubmission submission) {
        return new SessionDetailRes(
                session.getId(),
                session.getGeneration().getYear(),
                session.getPart().name(),
                session.getTitle(),
                session.getStartedAt(),
                session.getEndedAt(),
                session.getSessionUrl(), // 없으면 null
                submission != null ? AssignmentSubmissionRes.from(submission) : null
        );
    }
}

