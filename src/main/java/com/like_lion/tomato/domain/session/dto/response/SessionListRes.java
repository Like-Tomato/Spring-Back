package com.like_lion.tomato.domain.session.dto;

import com.like_lion.tomato.domain.session.dto.response.SessionSimpleRes;
import lombok.Getter;
import java.util.List;

@Getter
public class SessionListRes {
    private final List<SessionSimpleRes> sessions;

    public SessionListRes(List<SessionSimpleRes> sessions) {
        this.sessions = sessions;
    }

    public static SessionListRes from(List<SessionSimpleRes> sessions) {
        return new SessionListRes(sessions);
    }
}
