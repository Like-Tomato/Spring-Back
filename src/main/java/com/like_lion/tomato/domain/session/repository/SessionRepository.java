package com.like_lion.tomato.domain.session.repository;

import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.global.common.enums.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    // 조건부 전체 조회 (part, week가 null이면 전체 조회)
    @Query("""
        SELECT s FROM Session s
        WHERE (:part IS NULL OR s.part = :part)
          AND (:week IS NULL OR s.week = :week)
        ORDER BY s.endedAt DESC
    """)
    List<Session> findAllByPartAndWeek(
            @Param("part") Part part,
            @Param("week") Integer week);
    // memberId와 sessionId로 Session 조회 (Session 엔티티 기준)
    Optional<Session> findByMember_IdAndId(String memberId, String sessionId);

    // part와 generation.year로 조회
    List<Session> findByPartAndGeneration_Year(Part part, int year);
}

