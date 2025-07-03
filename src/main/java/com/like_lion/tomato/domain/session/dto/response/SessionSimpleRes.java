package com.like_lion.tomato.domain.session.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.session.entity.session.Session;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionSimpleRes {

    @JsonProperty("id")
    private String id;

    @JsonProperty("week")
    private int week;

    @JsonProperty("title")
    private String title;

    @JsonProperty("started_at")
    private String startedAt;

    @JsonProperty("ended_at")
    private String endedAt;


    @Builder
    public SessionSimpleRes(String id, int week, String title, String startedAt, String endedAt) {
        this.id = id;
        this.week = week;
        this.title = title;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static SessionSimpleRes from(Session session) {
        return SessionSimpleRes.builder()
                .id(session.getId())
                .week(session.getWeek())
                .title(session.getTitle())
                .startedAt(session.getCreatedAt().toString())
                .endedAt(session.getEndedAt().toString())
                .build();
    }
}
