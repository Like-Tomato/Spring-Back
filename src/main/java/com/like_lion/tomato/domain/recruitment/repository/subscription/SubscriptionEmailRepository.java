package com.like_lion.tomato.domain.recruitment.repository.subscription;

import com.like_lion.tomato.domain.recruitment.entity.subscription.EmailSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionEmailRepository extends JpaRepository<EmailSubscription, String> {
    Optional<EmailSubscription> findByEmail(String email);

    boolean existsByEmail(String email);
}

