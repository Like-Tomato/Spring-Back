package com.like_lion.tomato.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionWithState {

    @JsonProperty("session_id")
    private final String sessionId;

    @JsonProperty("session_title")
    private final String sessionTitle;

    @JsonProperty("week")
    private final int week;

    @JsonProperty("submitted")
    private final boolean submitted;
}
