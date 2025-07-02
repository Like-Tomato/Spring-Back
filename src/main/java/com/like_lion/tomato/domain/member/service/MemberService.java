package com.like_lion.tomato.domain.member.service;


import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.member.dto.request.UpdateMemberProfileReq;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileAssignRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileRes;
import com.like_lion.tomato.domain.member.dto.response.SessionWithState;
import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.repository.AssignmentSubmissionRepository;
import com.like_lion.tomato.domain.session.repository.SessionRepository;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.member.implement.MemberWriter;
import com.like_lion.tomato.infra.s3.service.S3PresignedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3PresignedService s3PresignedService;


    @Transactional
    public MemberProfileListRes readAllMemberProfiles(int page, int size, String part, int year) {

        // 유효성 검사(나중에 Valid로 리팩터링 예정)
        if(!part.isBlank() && !Part.isValid(part)) throw new MemberException(MemberErrorCode.INVALID_PART);
        Part partEnum = Part.valueOf(part.toUpperCase());

        if(!Generation.isValidYear(year)) throw new MemberException(MemberErrorCode.INVALID_YEAR);


        // MemberProfileRequest에서 Valid로 유효성 검사 구현!
        Integer yearInteger = year;

        // 페이지네이션 및 정렬(CreatedAt 기준 내림차순)
        PageRequest pageable = PageRequest.of(page - 1,
                size,
                Sort.by("CreatedAt").descending());

        Page<Member> memberPage = memberReader.findAllByPartAndYear(partEnum, yearInteger, pageable);

        // Member -> DTO
        return MemberProfileListRes.from(
                memberPage.getContent()
                        .stream()
                        .map(member -> MemberProfileRes.from(
                                member,
                                s3PresignedService.getPresignedUrlForGet(member.getFileKey())
                        ))
                        .toList()
        );
    }

    @Transactional
    public MemberProfileRes readMemberProfile(String memberId) {
        Member member =  memberReader.findOptionById(memberId)
                .orElseThrow(
                        () -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)
                );
        return MemberProfileRes.from(member, s3PresignedService.getPresignedUrlForGet(member.getFileKey()));
    }

    @Transactional
    public MemberProfileAssignRes getMemberProfileWithAssignments(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Part part = member.getPart();
        int year = member.getLatestYear();

        List<Session> sessions = sessionRepository.findByPartAndGeneration_Year(part, year);

        List<SessionWithState> sessionWithStates = sessions.stream()
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

        // 멤버 프로필 이미지 presigned URL 발급 후 저장
        return MemberProfileAssignRes.from(member, sessionWithStates, s3PresignedService.getPresignedUrlForGet(member.getFileKey()));
    }


    @Transactional
    public void update(String memberId, UpdateMemberProfileReq request) {
        Member member = memberReader.findOptionById(memberId)
                .orElseThrow(
                        () -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)
                );
        // 프로필 이미지 업로드 로직 구현!
        member.update(request);

        // JPA의 변경 감지로 트랜잭션 종료 시 자동 update (save 불필요)
        memberWriter.save(member);
    }

    public Member extractMemberFromToken(String token) {
        String memberId = jwtTokenProvider.extractMemberIdFromToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getValidateAdmin(String authorization) {
        Member member = extractMemberFromToken(authorization);
        if (!member.hasAdminRoleOrHigher()) {
            throw new AuthException(AuthErrorCode.ADMIN_REQUIRED);
        }

        return member;
    }
}
