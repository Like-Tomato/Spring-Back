package com.like_lion.tomato.domain.recruitment.service.application;

import com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse;
import com.like_lion.tomato.domain.recruitment.dto.applicant.PassResponse;
import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentErrorCode;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentException;
import com.like_lion.tomato.domain.recruitment.repository.application.ApplicationRepository;
import com.like_lion.tomato.domain.recruitment.service.notification.EmailService;
import com.like_lion.tomato.global.common.enums.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassService {

    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    public PassResponse passApplicants(int round, Part part) {
        StatusTransition transition = getApplicationStatusTransition(round);
        List<Application> applications = getApplicationsByStatusAndPart(transition.fromStatus(), part);

        updateApplicationsStatus(applications, transition.toStatus());

        return buildPassResponse(round, applications);
    }

    public PassResponse sendPassNotifications(int round) {
        ApplicationStatus targetStatus = getTargetStatusByRound(round);
        List<Application> passApplications = applicationRepository.findAllByStatus(targetStatus);

        sendEmailNotifications(round, passApplications);

        return buildPassResponse(round, passApplications);
    }

    private ApplicationStatus getTargetStatusByRound(int round) {
        return switch (round) {
            case 1 -> ApplicationStatus.FIRST_PASS;
            case 2 -> ApplicationStatus.FINAL_PASS;
            default -> throw new RecruitmentException(RecruitmentErrorCode.INVALID_ROUND);
        };
    }

    private void sendEmailNotifications(int round, List<Application> applications) {
        if (applications.isEmpty()) {
            log.info("발송할 합격자가 없습니다.");
            return;
        }

        String roundName = round == 1 ? "1차" : "최종";
        log.info("{} 합격자 이메일 발송 시작: {} 명", roundName, applications.size());

        if (round == 1) {
            emailService.sendFirstPassNotificationBatch(applications);
        } else {
            emailService.sendFinalPassNotificationBatch(applications);
        }

        log.info("{} 합격자 이메일 발송 완료", roundName);
    }

    private PassResponse buildPassResponse(int round, List<Application> applications) {
        Map<String, List<ApplicantResponse.Simple>> applicantsByPart = applications.stream()
                .collect(Collectors.groupingBy(
                        application -> application.getPart().name(),
                        Collectors.mapping(
                                application -> new ApplicantResponse.Simple(
                                        application.getId(),
                                        application.getUsername()
                                ),
                                Collectors.toList()
                        )
                ));

        return new PassResponse(round, applications.size(), applicantsByPart);
    }

    private record StatusTransition(ApplicationStatus fromStatus, ApplicationStatus toStatus) {}

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
}
