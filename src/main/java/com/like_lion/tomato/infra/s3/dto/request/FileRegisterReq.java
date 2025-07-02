package com.like_lion.tomato.infra.s3.dto.request;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.entity.session.SessionFIle;
import com.like_lion.tomato.global.common.enums.MimeType;
import jakarta.validation.constraints.NotNull;

//파일 업로드시 세션, 갤러리, 과제 등에서 모두 import해서 사용
public record FileRegisterReq(
        @NotNull String fileKey,
        @NotNull String originalName,
        @NotNull String mimeType,
        @NotNull Long size
) {
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
                .name(this.originalName())
                .fileKey(this.fileKey())
                .prefix(this.extractPrefix(fileKey))
                .mimeType(MimeType.valueOf(this.mimeType().toUpperCase()))
                .size(this.size())
                .build();
    }

    private String extractPrefix(String fileKey) {
        int idx = fileKey.lastIndexOf('/');
        return idx > 0 ? fileKey.substring(0, idx) : "";
    }
}