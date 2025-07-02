package com.like_lion.tomato.domain.session.service;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.session.dto.request.AssignmentLinksSubmissionReq;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.exception.SessionErrorCode;
import com.like_lion.tomato.domain.session.exception.SessionException;
import com.like_lion.tomato.domain.session.repository.AssignmentSubmissionRepository;
import com.like_lion.tomato.domain.session.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssignmentSubmissionService {

    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final SessionRepository sessionRepository;
    private final MemberReader memberReader;

    @Transactional
    public void submitAssignmentLinks(String sessionId, String memberId, AssignmentLinksSubmissionReq req) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionException(SessionErrorCode.SESSION_NOT_FOUND));
        Member member = memberReader.findOptionById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        assignmentSubmissionRepository.save(req.to(session, member));
    }
}