package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse;
import com.like_lion.tomato.domain.recruitment.dto.applicant.StatusResponse;
import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentErrorCode;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentException;
import com.like_lion.tomato.domain.recruitment.repository.application.ApplicationRepository;
import com.like_lion.tomato.global.common.enums.Part;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse.*;
import static com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus.*;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicationRepository applicationRepository;
    private final MemberService memberService;

    public Detail getApplicantDetail(String applicationId, String authorization) {
        Member reviewer = memberService.getValidateAdmin(authorization);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RecruitmentException(RecruitmentErrorCode.APPLICATION_NOT_FOUND));

        return Detail.from(application, reviewer.getId());
    }

    public StatusResponse getApplicants(Part part, @NotNull Integer round, String authorization) {
        Member reviewer = memberService.getValidateAdmin(authorization);

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
