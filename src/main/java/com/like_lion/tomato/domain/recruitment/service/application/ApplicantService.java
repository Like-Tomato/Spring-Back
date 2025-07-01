package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse;
import com.like_lion.tomato.domain.recruitment.repository.application.ApplicationRepository;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.common.enums.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public List<ApplicantResponse> getApplicants(Part part, String authorization) {
        String memberId = jwtTokenProvider.extractMemberIdFromToken(authorization);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!member.hasAdminRoleOrHigher()) {
            throw new AuthException((AuthErrorCode.ADMIN_REQUIRED));
        }

        if (part == null) {
            return applicationRepository.findAllBySubmittedIsNotNull().stream()
                    .map(application -> ApplicantResponse.from(application, member.getId()))
                    .toList();
        } else {
            return applicationRepository.findAllBySubmittedIsNotNullAndPart((part))
                    .stream()
                    .map(application -> ApplicantResponse.fromPart(application, part, member.getId()))
                    .toList();
        }
    }
}
