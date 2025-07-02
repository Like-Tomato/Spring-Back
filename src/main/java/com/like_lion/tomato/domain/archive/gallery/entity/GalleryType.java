package com.like_lion.tomato.domain.archive.gallery.entity;

public enum GalleryType {
    ALL,
    REGULAR_SESSION,
    CENTRAL_ACTIVITY,
    OWN_ACTIVITY,
    SOCIAL_ACTIVITY,
    ETC;

    // 대소문자 구분 없이 유효성 검사
    public static boolean isValid(String type) {
        if (type == null) return false;
        for (GalleryType t : values()) {
            if (t.name().equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
