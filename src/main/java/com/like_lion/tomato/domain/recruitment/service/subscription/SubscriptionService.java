package com.like_lion.tomato.domain.recruitment.service.subscription;

import com.like_lion.tomato.domain.recruitment.dto.subscription.SubscriptionRequest;
import com.like_lion.tomato.domain.recruitment.dto.subscription.SubscriptionResponse;
import com.like_lion.tomato.domain.recruitment.entity.subscription.EmailSubscription;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentErrorCode;
import com.like_lion.tomato.domain.recruitment.exception.RecruitmentException;
import com.like_lion.tomato.domain.recruitment.repository.subscription.SubscriptionEmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionEmailRepository subscriptionRepository;

    public SubscriptionResponse subscribeEmail(SubscriptionRequest request) {
        if (subscriptionRepository.existsByEmail(request.email())) {
            throw new RecruitmentException(RecruitmentErrorCode.ALREADY_SUBSCRIBED);
        }

        EmailSubscription subscription = EmailSubscription.builder()
                .email(request.email())
                .build();

        subscriptionRepository.save(subscription);

        return new SubscriptionResponse(true);
    }

    public List<String> getSubscriberEmails() {
        return subscriptionRepository.findAll()
                .stream()
                .map(EmailSubscription::getEmail)
                .toList();
    }
}
