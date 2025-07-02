package com.like_lion.tomato.domain.recruitment.repository.application;

import com.like_lion.tomato.domain.recruitment.entity.application.Application;
import com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus;
import com.like_lion.tomato.global.common.enums.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, String> {
    Optional<Application> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    boolean existsByMemberIdAndSubmittedAtIsNotNull(String memberId);

    void deleteByMemberIdAndSubmittedAtIsNull(String memberId);

    Optional<Application> findByMemberIdAndSubmittedAtIsNull(String memberId);

    List<Application> findAllByStatusAndPart(ApplicationStatus status, Part part);

    List<Application> findAllByStatus(ApplicationStatus status);
}
