package com.like_lion.tomato.domain.session.service;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.dto.request.SessionPostReq;
import com.like_lion.tomato.domain.session.dto.response.SessionDetailRes;
import com.like_lion.tomato.domain.session.dto.response.SessionListWithStateRes;
import com.like_lion.tomato.domain.session.dto.response.SessionSimpleRes;
import com.like_lion.tomato.domain.session.dto.response.SessionWithState;
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

    /**
     * 파트/주차별 전체 세션 + 멤버별 과제 제출여부 포함 조회
     */
    @Transactional(readOnly = true)
    public SessionListWithStateRes readAllSessionsWithSubmissionState(String memberId, String part, Integer week) {
        Part partEnum = null;
        if (part != null && !part.isBlank()) {
            if (!Part.isValid(part)) throw new IllegalArgumentException("유효하지 않은 파트입니다.");
            partEnum = Part.valueOf(part.toUpperCase());
        }

        List<Session> sessionEntities = sessionRepository.findAllByPartOrAll(partEnum, week);

        List<SessionWithState> sessionWithStates = sessionEntities.stream()
                .map(session -> {
                    boolean submitted = assignmentSubmissionRepository.existsByMemberIdAndSessionId(memberId, session.getId());
                    return SessionWithState.builder()
                            .sessionId(session.getId())
                            .sessionTitle(session.getTitle())
                            .week(session.getWeek())
                            .submitted(submitted)
                            .build();
                })
                .toList();

        return SessionListWithStateRes.from(sessionWithStates);
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

        // 해당 멤버의 과제 제출 내역 조회 (없을 수도 있음) -> 데이터 정합성만 보장되면 Session에서 과제 리스트로 조회할 수 있도록 리팩텅링!
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
