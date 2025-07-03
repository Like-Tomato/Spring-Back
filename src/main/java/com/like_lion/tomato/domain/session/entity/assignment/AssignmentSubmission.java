package com.like_lion.tomato.domain.session.entity.assignment;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
public class AssignmentSubmission extends BaseEntity {

    @DomainId(DomainType.ASSIGNMENT_SUBMISSION)
    @Id
    @Column(name = "assignment_submission_id")
    private String assignmentSubmissionId;

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
}
