package com.like_lion.tomato.domain.recruitment.repository.application;

import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, String> {
    Optional<Application> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    boolean existsByMemberIdAndSubmittedAtIsNotNull(String memberId);

    void deleteByMemberIdAndSubmittedAtIsNull(String memberId);

    Optional<Application> findByMemberIdAndSubmittedAtIsNull(String memberId);
}
