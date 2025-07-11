package com.like_lion.tomato.domain.member.entity;

public enum Part {
    AI,
    BACKEND,
    FRONTEND,
    DESIGN,
    PLAN;

    // 이후 Bean이 관리하는 Valid로 구현한 후 Req에서 사용할 예정(리팩터링)
    public static boolean isValid(String part) {
        if (part == null) return false;
        for (Part p : values()) {
            if (p.name().equalsIgnoreCase(part)) return true;
        }
        return false;
    }
}
