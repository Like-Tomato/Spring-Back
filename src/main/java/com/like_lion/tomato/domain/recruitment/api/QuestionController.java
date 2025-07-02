package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.question.QuestionInfo;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionUploadRequest;
import com.like_lion.tomato.domain.recruitment.dto.question.QuestionResponse;
import com.like_lion.tomato.domain.recruitment.service.application.QuestionService;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruit/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<QuestionResponse> createQuestions(
            @RequestParam Part part,
            @RequestBody QuestionUploadRequest requests,
            @RequestHeader("Authorization") String authorization
    ) {
        QuestionResponse response = questionService.uploadQuestions(part, requests, authorization);
        return ApiResponse.success(response);
    }

    @GetMapping("/{part}")
    public ApiResponse<QuestionInfo> getQuestionsByPart(@PathVariable Part part) {
        QuestionInfo response = questionService.getQuestionsByPart(part);
        return ApiResponse.success(response);
    }


}
