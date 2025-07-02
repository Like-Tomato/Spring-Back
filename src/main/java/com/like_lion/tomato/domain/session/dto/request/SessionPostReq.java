package com.like_lion.tomato.domain.session.dto.request;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.entity.session.SessionFIle;
import com.like_lion.tomato.global.common.enums.MimeType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionPostReq {

    @NotNull
    private Integer week;

    @NotNull
    private String title;

    @NotNull
    private String part; // Enum 변환은 서비스에서 처리

    @NotNull
    private String dueDate; // ISO-8601 문자열 (ex: "2024-01-01T23:59:59Z")

    @NotNull
    private String description;

    @NotNull
    private String fileKey; // S3 fileKey (예: "session/uuid/파일명.pdf")

    private String mimeType;

    @Builder
    public SessionPostReq(Integer week, String title, String part, String dueDate,
                          String description, String fileKey) {
        this.week = week;
        this.title = title;
        this.part = part;
        this.dueDate = dueDate;
        this.description = description;
        this.fileKey = fileKey;
    }

    public SessionFIle to(Session session, Member member) {

        return SessionFIle.builder()
                .session(session)
                .member(member)
                .fileKey(this.fileKey)
                .mimeType(MimeType.valueOfMimeType(this.mimeType)) // "PDF" 등 enum 이름
                .build();
    }
}
