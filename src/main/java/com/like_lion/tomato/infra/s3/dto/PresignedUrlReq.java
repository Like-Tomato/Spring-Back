// PresignedUrlReq.java
package com.like_lion.tomato.infra.s3.dto;

import com.like_lion.tomato.global.common.enums.MimeType;
import jakarta.validation.constraints.NotNull;

/**
 * Presigned URL 발급 요청 DTO
 */
public record PresignedUrlReq(
        @NotNull String prefix,          // S3 폴더 경로(예: "session/123")
        @NotNull String fileName,        // 원본 파일명
        MimeType mimeType                // 파일 MIME 타입 (nullable, 필요시 사용)
) {}
