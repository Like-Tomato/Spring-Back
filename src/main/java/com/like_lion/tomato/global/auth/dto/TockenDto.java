package com.like_lion.tomato.global.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TockenDto {

    private String username;
    private String accessTocken;
    private String refreshTocken;

    @Builder
    public TockenDto(String username, String accessTocken, String refreshTocken) {
        this.username = username;
        this.accessTocken = accessTocken;
        this.refreshTocken = refreshTocken;
    }
}
