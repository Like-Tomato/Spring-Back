package com.like_lion.tomato.domain.session.service;

import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.dto.response.SessionDetailRes;
import com.like_lion.tomato.domain.session.dto.response.SessionSimpleRes;
import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.entity.session.Session.Part;
import com.like_lion.tomato.domain.session.repository.SessionRepository;
import com.like_lion.tomato.domain.session.exception.SessionErrorCode;
import com.like_lion.tomato.domain.session.exception.SessionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public SessionListRes readAllSessions(String part) {
        Part partEnum = null;
        if (part != null && !part.isBlank()) {
            if (!Part.isValid(part)) {
                throw new SessionException(SessionErrorCode.INVALID_PART);
            }
            partEnum = Part.valueOf(part.toUpperCase());
        }
        List<Session> sessionEntities = sessionRepository.findAllByPartOrAll(partEnum);

        List<SessionSimpleRes> simpleResList = sessionEntities.stream()
                .map(SessionSimpleRes::from)
                .toList();

        return SessionListRes.from(simpleResList);
    }
    /**
     * 세션 상세 + 해당 멤버의 과제 제출 내역 조회
     * @param sessionId 세션 ID
     * @param memberId  현재 로그인한 멤버 ID (JWT에서 추출)
     * @return SessionDetailRes
     */
    @Transactional(readOnly = true)
    public SessionDetailRes getSessionWithAssignment(String sessionId, String memberId) {
        // 1. 세션 조회 (없으면 404)
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionException(SessionErrorCode.PART_NOT_FOUND));

        // 2. 해당 멤버의 과제 제출 내역 조회 (없을 수도 있음)
//        AssignmentSubmission submission = assignmentSubmissionRepository
//                .findByMemberIdAndSessionId(memberId, sessionId)
//                .orElse(null); // 예외 처리!
//
//        // 3. DTO 변환 후 반환
//        return SessionDetailRes.of(session, submission);

        return SessionDetailRes.of(session);
    }
}
