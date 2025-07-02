package com.like_lion.tomato.domain.session.entity.session;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.common.enums.MimeType;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "session_file")
public class SessionFIle extends BaseEntity {

    @DomainId(DomainType.SESSION_FILE)
    @Id
    @Column(name = "session_file_id")
    private String sessionId;

    @Column(nullable = false)
    private String name; // 사용자가 업로드한 파일명

    @Column(nullable = false)
    private String fileKey; // S3에 저장된 전체 key (prefix/uuid-파일명)

    @Column(nullable = false)
    private String prefix; // S3 prefix (예: "session/1234")

    @Enumerated(EnumType.STRING)
    private MimeType mimeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="session_id")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SessionFIle(String name, String fileKey, String prefix, MimeType mimeType, Long size, Session session, Member member) {
        this.name = name;
        this.fileKey = fileKey;
        this.prefix = prefix;
        this.mimeType = mimeType;
        this.session = session;
        this.member = member;
    }


}
