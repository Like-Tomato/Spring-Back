package com.like_lion.tomato.domain.session.service;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.dto.request.SessionPostReq;
import com.like_lion.tomato.domain.session.dto.response.SessionDetailRes;
import com.like_lion.tomato.domain.session.dto.response.SessionSimpleRes;
import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.entity.session.SessionFIle;
import com.like_lion.tomato.domain.session.repository.AssignmentSubmissionRepository;
import com.like_lion.tomato.domain.session.repository.SessionFileRepository;
import com.like_lion.tomato.domain.session.repository.SessionRepository;
import com.like_lion.tomato.domain.session.exception.SessionErrorCode;
import com.like_lion.tomato.domain.session.exception.SessionException;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import com.like_lion.tomato.infra.s3.service.S3PresignedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionFileRepository sessionFileRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final MemberReader memberReader;
    private final S3PresignedService s3PresignedService;

    @Value("${cloud.s3.download.expTime}")
    private Long downloadExpTime;

    @Transactional(readOnly = true)
    public SessionListRes readAllSessions(String part, Integer week) {

        if(!part.isBlank() && !Part.isValid(part)) throw new SessionException(SessionErrorCode.INVALID_PART);
        Part partEnum = Part.valueOf(part.toUpperCase());

        List<Session> sessionEntities = sessionRepository.findAllByPartOrAll(partEnum, week);

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
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionException(SessionErrorCode.SESSION_NOT_FOUND));

        // 해당 멤버의 과제 제출 내역 조회 (없을 수도 있음)
        AssignmentSubmission submissions = assignmentSubmissionRepository
                .findByMemberIdAndSessionId(memberId, sessionId)
                .orElse(null);

        // 세션 파일 모두 조회
        SessionFIle sessionFile = sessionFileRepository.findBySessionId(sessionId)
                .orElse(null);

        PresignedUrlRes presignedUrlRes = null;
        if (sessionFile != null) {
            String presignedUrl = s3PresignedService.getPresignedUrlForGet(sessionFile.getFileKey()).url();
            presignedUrlRes = PresignedUrlRes.of(
                    presignedUrl,
                    sessionFile.getFileKey(),
                    System.currentTimeMillis() + downloadExpTime,
                    sessionFile.getMimeType()
            );
        }

        // 3. DTO 변환 후 반환
        return SessionDetailRes.from(session, presignedUrlRes);

    }

    /**
     * 세션 파일 등록 (ADMIN만 가능)
     * @param sessionId 세션 ID
     * @param memberId  등록자(관리자) 멤버 ID
     * @param req       파일 등록 요청 DTO(fileKey, mimeType)
     */
    @Transactional
    public void create(String sessionId, String memberId, SessionPostReq req){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionException(SessionErrorCode.SESSION_NOT_FOUND));
        Member member = memberReader.findOptionById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // DTO에서 엔티티로 변환 후 저장
        sessionFileRepository.save(req.to(session, member));
    }
}
