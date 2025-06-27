package com.like_lion.tomato.global.id;

import lombok.Getter;

@Getter
public enum DomainType {
    MEMBER("MEMBER"),
    GENERATION("GENERATION"),
    APPLICATION("APPLICATION"),
    RECRUITMENT_COMMON_ANSWER("RECRUIMENT_COMMON_ANSWER"),
    RECRUITMENT_COMMON_QUESTION("RECRUIMENT_COMMON_QUESTION"),
    RECRUITMENT_PART_ANSWER("RECRUITMENT_PART_ANSWER"),
    RECRUITMENT_PART_QUESTION("RECRUITMENT_PART_QUESTION"),;


    private final String prefix;

    DomainType(String prefix) {
        this.prefix = prefix;
    }
}
