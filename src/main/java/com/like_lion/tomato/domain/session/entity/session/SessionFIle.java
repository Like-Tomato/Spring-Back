package com.like_lion.tomato.domain.session.entity.session;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.BaseEntitiy;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "session_file")
public class SessionFIle extends BaseEntitiy {

    @DomainId(DomainType.SESSION_FILE)
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MimeType mimeType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="member_id")
    private Member member;
}
