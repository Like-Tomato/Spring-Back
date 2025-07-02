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
        ORDER BY s.week ASC
    """)
    List<Session> findAllByPartOrAll(
            @Param("part") Part part,
            @Param("week") Integer week);

    /**
     * memberId와 sessionId로 제출한 과제 조회
     * AssignmentSubmission → Assignment → Session 구조라면 아래처럼 fetch join 사용
     */
//    @Query("""
//        SELECT s FROM AssignmentSubmission s
//        JOIN FETCH s.assignment a
//        WHERE s.member.id = :memberId
//        AND a.session.id = :sessionId
//    """)
    Optional<AssignmentSubmission> findByMemberIdAndSessionId(
            @Param("memberId") String memberId,
            @Param("sessionId") String sessionId
    );

}
