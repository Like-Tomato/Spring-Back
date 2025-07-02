package com.like_lion.tomato.domain.session.dto.request;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.domain.session.entity.session.Session;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssignmentLinksSubmissionReq {

    private String links; // "www.~.com, www.~.org, ..." 형태의 문자열

    // 엔티티 변환 메서드
    public AssignmentSubmission to(Session session, Member member) {
        return AssignmentSubmission.builder()
                .member(member)
                .session(session)
                .links(this.links)
                .build();
    }
}