// PresignedUrlReq.java
package com.like_lion.tomato.infra.s3.dto.request;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.entity.session.SessionFIle;
import com.like_lion.tomato.global.common.enums.MimeType;
import jakarta.validation.constraints.NotNull;

/**
 * Presigned URL 발급 요청 DTO
 */
public record PresignedUrlReq(
        @NotNull String prefix,
        @NotNull String fileKey,          // S3 폴더 경로(예: "session/123")
        String mimeType                // 파일 MIME 타입 (nullable, 필요시 사용)
) {
    private String extractPrefix(String fileKey) {
        int idx = fileKey.lastIndexOf('/');
        return idx > 0 ? fileKey.substring(0, idx) : "";
    }

    /**
     * FileRegisterReq → SessionFIle 변환
     * @param session 세션 엔티티
     * @param member  업로더(관리자) 엔티티
     * fileKey -> prefix S3 prefix (예: "session/1234")
     * @return SessionFIle 엔티티
     */
    public SessionFIle toSessionFIle(Session session, Member member) {
        return SessionFIle.builder()
                .session(session)
                .member(member)
                .fileKey(this.fileKey())
                .prefix(this.extractPrefix(fileKey))
                .mimeType(MimeType.valueOf(this.mimeType().toUpperCase()))
                .build();
    }
}
