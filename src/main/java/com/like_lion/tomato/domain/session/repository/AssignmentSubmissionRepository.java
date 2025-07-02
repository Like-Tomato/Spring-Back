package com.like_lion.tomato.domain.session.repository;

import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, String> {
    // memberId와 sessionId로 과제 제출 내역 조회
    Optional<AssignmentSubmission> findByMemberIdAndSessionId(String memberId, String sessionId);
    boolean existsByMemberIdAndSessionId(String memberId, String sessionId);
}