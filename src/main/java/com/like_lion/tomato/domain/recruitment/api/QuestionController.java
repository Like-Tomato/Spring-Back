package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationRequest;
import com.like_lion.tomato.domain.recruitment.dto.application.ApplicationResponse;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse;
import com.like_lion.tomato.domain.recruitment.service.application.ApplicationService;
import com.like_lion.tomato.domain.recruitment.service.application.RecruitmentQuestionService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class QuestionController {
    private final RecruitmentQuestionService questionService;
    private final ApplicationService applicationService;

    @GetMapping("/questions/{part}")
    public ApiResponse<QuestionResponse> getQuestionsByPart(@PathVariable Part part) {
        QuestionResponse response = questionService.getQuestionsByPart(part);
        return ApiResponse.success(response);
    }


}
