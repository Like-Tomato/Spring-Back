package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.question.QuestionInfo;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionUpdateRequest;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionUploadRequest;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse;
import com.like_lion.tomato.domain.recruitment.service.application.QuestionService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruit/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<QuestionResponse> uploadQuestions(
            @RequestParam Part part,
            @RequestBody QuestionUploadRequest request
    ) {
        QuestionResponse response = questionService.uploadQuestions(part, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/{part}")
    public ApiResponse<QuestionInfo> getQuestionsByPart(@PathVariable Part part) {
        QuestionInfo response = questionService.getQuestionsByPart(part);
        return ApiResponse.success(response);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<QuestionResponse> updateQuestions(
            @RequestParam Part part,
            @RequestBody QuestionUpdateRequest request
    ) {
        QuestionResponse response = questionService.updateQuestions(part, request);
        return ApiResponse.success(response);
    }


}
