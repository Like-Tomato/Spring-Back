package com.like_lion.tomato.domain.session.entity.session;

import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.BaseEntitiy;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Session extends BaseEntitiy {

    @DomainId(DomainType.SESSION)
    @Id
    @Column(name = "session_id")
    private String id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false)
    private int week;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.NOT_EXIST;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private Generation generation;

    // 추가: 세션 시작/종료 시간
    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    // 추가: 파트(백엔드/프론트/디자인 등)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Part part;

    // Status Enum은 명세에 맞게 정의!
    public enum Status {
        ASSIGNED, ONGOING, COMPLETED, NOT_EXIST, NOT_COMPLETED
    }

    // Part Enum도 명세에 맞게 정의!
    public enum Part {
        BACKEND, FRONTEND, DESIGN;

        public static boolean isValid(String part) {
            try {
                Part.valueOf(part.toUpperCase());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
