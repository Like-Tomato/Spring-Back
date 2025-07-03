package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationRequest;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationResponse;
import com.like_lion.tomato.domain.recruitment.service.application.ApplicationService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/apply/draft")
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
}
