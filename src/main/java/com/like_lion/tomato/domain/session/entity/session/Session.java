package com.like_lion.tomato.domain.session.entity.session;

import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.global.common.BaseEntitiy;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private int week;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Part part;

    @Column(nullable = false, length = 100)
    private String assignmentDdescription;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private Generation generation;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentSubmission> assignmentSubmissions = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addAssignmentSubmission(AssignmentSubmission submission) {
        assignmentSubmissions.add(submission);
        submission.setSession(this);
    }

    public void removeAssignmentSubmission(AssignmentSubmission submission) {
        assignmentSubmissions.remove(submission);
        submission.setSession(null);
    }

}
