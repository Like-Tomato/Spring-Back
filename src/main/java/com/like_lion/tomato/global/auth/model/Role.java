package com.like_lion.tomato.global.auth.model;

public enum Role {
    ROLE_GUEST, ROLE_MEMBER, ROLE_ADMIN, ROLE_MASTER;

    public static boolean isValid(String value) {
        if (value == null) return false;
        for (Role role : values()) {
            if (role.name().equalsIgnoreCase(value)) return true;
        }
        return false;
    }
}
