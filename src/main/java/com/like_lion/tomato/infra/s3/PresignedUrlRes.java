// PresignedUrlRes.java
package com.like_lion.tomato.infra.s3;

import com.like_lion.tomato.global.common.enums.MimeType;

/**
 * Presigned URL 발급 응답 DTO
 */
public record PresignedUrlRes(
        String url,          // presigned URL
        String fileKey,      // S3에 저장될 파일 key(경로+파일명)
        Long expireAt,       // presigned URL 만료시간(Unix timestamp, 선택)
        MimeType mimeType    // 파일 MIME 타입 (nullable, 필요시 사용)
) {
    public static PresignedUrlRes of(String url, String fileKey) {
        return new PresignedUrlRes(url, fileKey, null, null);
    }

    public static PresignedUrlRes of(String url, String fileKey, Long expireAt, MimeType mimeType) {
        return new PresignedUrlRes(url, fileKey, expireAt, mimeType);
    }
}
