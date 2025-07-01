package com.like_lion.tomato.domain.session.dto.response;

import com.like_lion.tomato.domain.session.entity.session.Session;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionSimpleRes {
    private String id;
    private int week;
    private String title;
    private String startedAt;
    private String endedAt;
    private String status;

    @Builder
    public SessionSimpleRes(String id, int week, String title, String startedAt, String endedAt, String status) {
        this.id = id;
        this.week = week;
        this.title = title;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.status = status;
    }

    public static SessionSimpleRes from(Session session) {
        return SessionSimpleRes.builder()
                .id(session.getId())
                .week(session.getWeek())
                .title(session.getTitle())
                .startedAt(session.getStartedAt().toString())
                .endedAt(session.getEndedAt().toString())
                .status(session.getStatus().name())
                .build();
    }
}