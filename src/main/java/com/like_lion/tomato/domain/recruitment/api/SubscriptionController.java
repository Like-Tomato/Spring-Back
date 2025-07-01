package com.like_lion.tomato.domain.recruitment.api;

import com.like_lion.tomato.domain.recruitment.dto.subscription.SubscriptionRequest;
import com.like_lion.tomato.domain.recruitment.dto.subscription.SubscriptionResponse;
import com.like_lion.tomato.domain.recruitment.service.subscription.SubscriptionService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/recruit/subscribe")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ApiResponse<SubscriptionResponse> subscribeEmail(@RequestBody SubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.subscribeEmail(request);
        return ApiResponse.success(response);
    }
}
