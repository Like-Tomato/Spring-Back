package com.like_lion.tomato.global.id;

import lombok.Getter;

@Getter
public enum DomainType {
    MEMBER("MEMBER");

    private final String prefix;

    DomainType(String prefix) {
        this.prefix = prefix;
    }
}
