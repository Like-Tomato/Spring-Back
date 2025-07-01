package com.like_lion.tomato.global.common.enums;

public enum MimeType {
    PDF("application/pdf"),
    JPG("image/jpeg"),
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
