package com.like_lion.tomato.domain.session.repository;

import com.like_lion.tomato.domain.session.entity.assignment.AssignmentSubmission;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.global.common.enums.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    @Query("""
        SELECT s FROM Session s
        WHERE (:part IS NULL OR s.part = :part)
          AND (:week IS NULL OR s.week = :week)
        ORDER BY s.startedAt DESC
    """)
    List<Session> findAllByPartOrAll(
            @Param("part") Part part,
            @Param("week") Integer week);

}
