package com.like_lion.tomato.domain.session.entity.session;

public enum MimeType {
    PDF("application/pdf"),
    JPEG("image/jpeg"),
    PNG("image/png");

    private final String value;
    MimeType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
