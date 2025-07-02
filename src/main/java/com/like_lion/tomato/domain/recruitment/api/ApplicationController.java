package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.applicant.ApplicantResponse;
import com.like_lion.tomato.domain.recruitment.dto.applicant.StatusResponse;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationRequest;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationResponse;
import com.like_lion.tomato.domain.recruitment.service.application.ApplicantService;
import com.like_lion.tomato.domain.recruitment.service.application.ApplicationService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicantService applicantService;

    @PostMapping("/application")
    public ApiResponse<ApplicationResponse> submitApplication(
            @RequestBody @Valid ApplicationRequest request,
            @RequestHeader("Authorization") String authorization
    ) {
        ApplicationResponse response = applicationService.submitApplication(request, authorization);
        return ApiResponse.success(response);
    }

    @GetMapping("/draft")
    public ApiResponse<ApplicationResponse> getApplication(@RequestHeader("Authorization") String authorization) {
        ApplicationResponse response = applicationService.getApplicationDetail(authorization);
        return ApiResponse.success(response);
    }

    @PutMapping("/draft")
    public ApiResponse<ApplicationResponse> updateApplication(
            @RequestBody ApplicationRequest request,
            @RequestHeader("Authorization") String authorization
    ) {
        ApplicationResponse response = applicationService.draftApplication(request, authorization);
        return ApiResponse.success(response);
    }

    @GetMapping("/applicants/{applicationId}")
    public ApiResponse<ApplicantResponse.Detail> getApplicantDetail(
            @PathVariable String applicationId,
            @RequestHeader("Authorization") String authorization
    ) {
        ApplicantResponse.Detail response = applicantService.getApplicantDetail(applicationId, authorization);
        return ApiResponse.success(response);
    }

    @GetMapping("/applicants")
    public ApiResponse<StatusResponse> getApplicants(
            @RequestParam Part part,
            @RequestParam @NotNull Integer round,
            @RequestHeader("Authorization") String authorization
    ) {
        StatusResponse response = applicantService.getApplicants(part, round, authorization);
        return ApiResponse.success(response);
    }
}
