package com.like_lion.tomato.domain.session.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SessionListWithStateRes {

    @JsonProperty("sessions")
    private final List<SessionWithState> sessions;

    @Builder
    public SessionListWithStateRes(List<SessionWithState> sessions) {
        this.sessions = sessions;
    }

    public static SessionListWithStateRes from(List<SessionWithState> sessions) {
        return SessionListWithStateRes.builder().sessions(sessions).build();
    }
}