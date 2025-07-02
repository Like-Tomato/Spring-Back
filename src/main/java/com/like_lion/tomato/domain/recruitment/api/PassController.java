package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.applicant.PassResponse;
import com.like_lion.tomato.domain.recruitment.service.application.PassService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class PassController {
    private final PassService passService;

    @PostMapping("/pass/{round}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PassResponse> passApplicants(@PathVariable int round, @RequestParam Part part) {
        PassResponse response = passService.passApplicants(round, part);
        return ApiResponse.success(response);
    }

    @PostMapping("/notifications/pass/{round}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PassResponse> passApplicants(@PathVariable int round) {
        PassResponse response = passService.sendPassNotifications(round);
        return ApiResponse.success(response);
    }
}
