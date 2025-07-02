package com.like_lion.tomato.domain.session.entity.assignment;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AssignmentSubmission extends BaseEntity {

    @DomainId(DomainType.ASSIGNMENT_SUBMISSION)
    @Id
    @Column(name = "assinment_submission_id")
    private String assinmentSubmissionId;

    private String links;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    // 연관관계 편의 메서드
    public void setSession(Session session) {
        this.session = session;
    }

    @Builder
    public AssignmentSubmission(Member member, Session session, String links) {
        this.member = member;
        this.session = session;
        this.links = links;
    }
}
