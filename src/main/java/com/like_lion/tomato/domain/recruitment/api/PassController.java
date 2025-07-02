package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.applicant.PassResponse;
import com.like_lion.tomato.domain.recruitment.service.application.PassService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class PassController {
    private final PassService passService;

    @PostMapping("/pass/{round}")
    public ApiResponse<PassResponse> passApplicants(
            @PathVariable int round,
            @RequestParam Part part,
            @RequestHeader("Authorization") String authorization
    ) {
        PassResponse response = passService.passApplicants(round, part, authorization);
        return ApiResponse.success(response);
    }

    @PostMapping("/notifications/pass/{round}")
    public ApiResponse<PassResponse> passApplicants(
            @PathVariable int round,
            @RequestHeader("Authorization") String authorization
    ) {
        PassResponse response = passService.sendPassNotifications(round, authorization);
        return ApiResponse.success(response);
    }
}
