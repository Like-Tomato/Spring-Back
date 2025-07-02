package com.like_lion.tomato.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionWithState {
    private final String sessionId;
    private final String sessionTitle;
    private final int week;
    private final boolean submitted; // 과제 제출 여부
}