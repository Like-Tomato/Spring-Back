package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse;
import com.like_lion.tomato.domain.recruitment.dto.applicant.PassResponse;
import com.like_lion.tomato.domain.recruitment.dto.applicant.StatusResponse;
import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentErrorCode;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentException;
import com.like_lion.tomato.domain.recruitment.repository.application.ApplicationRepository;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.common.enums.Part;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse.*;
import static com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus.*;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Detail getApplicantDetail(String applicationId, String authorization) {
        String reviewerId = jwtTokenProvider.extractMemberIdFromToken(authorization);

        Member reviewer = memberRepository.findById(reviewerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!reviewer.hasAdminRoleOrHigher()) {
            throw new AuthException((AuthErrorCode.ADMIN_REQUIRED));
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RecruitmentException(RecruitmentErrorCode.APPLICATION_NOT_FOUND));

        return Detail.from(application, reviewer.getId());
    }

    public StatusResponse getApplicants(Part part, @NotNull Integer round, String authorization) {
        String reviewerId = jwtTokenProvider.extractMemberIdFromToken(authorization);

        Member reviewer = memberRepository.findById(reviewerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!reviewer.hasAdminRoleOrHigher()) {
            throw new AuthException((AuthErrorCode.ADMIN_REQUIRED));
        }

        ApplicationStatus status = getStatusByRound(round);
        List<Application> applications = getApplications(status, part);

        List<ApplicantResponse> applicantsInfo = applications.stream()
                .map(application -> from(application, reviewer.getId()))
                .toList();

        if (part == null) {
            return new StatusResponse(round, "ALL", applicantsInfo);
        } else {
            return new StatusResponse(round, part.toString(), applicantsInfo);
        }
    }

    public PassResponse passApplicants(int round, Part part, String authorization) {
        String reviewerId = jwtTokenProvider.extractMemberIdFromToken(authorization);

        Member reviewer = memberRepository.findById(reviewerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!reviewer.hasAdminRoleOrHigher()) {
            throw new AuthException((AuthErrorCode.ADMIN_REQUIRED));
        }

        StatusTransition transition = getApplicationStatusTransition(round);
        List<Application> applications = getApplicationsByStatusAndPart(transition.fromStatus(), part);

        updateApplicationsStatus(applications, transition.toStatus());

        Map<String, List<ApplicantResponse.Simple>> applicantsByPart;

        if (part == null) {
            applicantsByPart = applications.stream()
                    .collect(Collectors.groupingBy(
                            application -> application.getPart().name(),
                            Collectors.mapping(
                                    application -> new ApplicantResponse.Simple(application.getId(), application.getUsername()),
                                    Collectors.toList()
                            )
                    ));
        } else {
            List<ApplicantResponse.Simple> applicantInfos = applications.stream()
                    .map(application -> new ApplicantResponse.Simple(application.getId(), application.getUsername()))
                    .toList();

            applicantsByPart = Map.of(part.name(), applicantInfos);
        }

        return new PassResponse(round, applications.size(), applicantsByPart);
    }

    private StatusTransition getApplicationStatusTransition(int round) {
        return switch (round) {
            case 1 -> new StatusTransition(SUBMITTED, FIRST_PASS);
            case 2 -> new StatusTransition(FIRST_PASS, FINAL_PASS);
            default -> throw new RecruitmentException(RecruitmentErrorCode.INVALID_ROUND);
        };
    }

    private List<Application> getApplicationsByStatusAndPart(ApplicationStatus status, Part part) {
        return (part == null)
                ? applicationRepository.findAllByStatus(status)
                : applicationRepository.findAllByStatusAndPart(status, part);
    }

    private void updateApplicationsStatus(List<Application> applications, ApplicationStatus newStatus) {
        applications.forEach(application -> application.updateStatus(newStatus));
        applicationRepository.saveAll(applications);
    }

    private record StatusTransition(ApplicationStatus fromStatus, ApplicationStatus toStatus) {}

    private ApplicationStatus getStatusByRound(int round) {
        return switch (round) {
            case 1 -> SUBMITTED;
            case 2 -> FIRST_PASS;
            default -> throw new RecruitmentException(RecruitmentErrorCode.INVALID_ROUND);
        };
    }

    private List<Application> getApplications(ApplicationStatus status, Part part) {
        return (part == null)
                ? applicationRepository.findAllByStatus(status)
                : applicationRepository.findAllByStatusAndPart(status, part);
    }
}
