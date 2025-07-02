package com.like_lion.tomato.global.common.enums;

public enum MimeType {
    PDF("application/pdf"),
    JPG("image/jpeg"),
    JPEG("image/jpeg"),
    PNG("image/png");

    private final String value;
    MimeType(String value) { this.value = value; }
    public String getValue() { return value; }

    // 추가: value로 enum을 찾는 정적 메서드
    public static MimeType valueOfMimeType(String value) {
        for (MimeType type : MimeType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 파일 타입입니다: " + value);
    }
}
