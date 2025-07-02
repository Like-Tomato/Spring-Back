package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse;
import com.like_lion.tomato.domain.recruitment.dto.applicant.StatusResponse;
import com.like_lion.tomato.domain.recruitment.service.application.ApplicantService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping("/applicants/{applicationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ApplicantResponse.Detail> getApplicantDetail(
            @PathVariable String applicationId,
            @RequestHeader("Authorization") String authorization
    ) {
        ApplicantResponse.Detail response = applicantService.getApplicantDetail(applicationId, authorization);
        return ApiResponse.success(response);
    }

    @GetMapping("/applicants")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<StatusResponse> getApplicants(
            @RequestParam Part part,
            @RequestParam @NotNull Integer round,
            @RequestHeader("Authorization") String authorization
    ) {
        StatusResponse response = applicantService.getApplicants(part, round, authorization);
        return ApiResponse.success(response);
    }
}
