package com.like_lion.tomato.domain.member.repository;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(String id);
    Optional<Member> findByUsername(String username);

    /** 페이지네이션 및 정렬(CreatedAt 기준 내림차순)
     * 1. 권한이 MEMBER 이상인 회원(아기사자, 운영진, 마스터)만 조회
     * 2. 기수가 중복되는 경우, MemberService에서 stream으로 필터링
     */
    @Query("""
        SELECT DISTINCT m FROM Member m
        JOIN m.memberGenerations mg
        JOIN mg.generation g
        WHERE (:part IS NULL OR g.part = :part)
        AND (:year IS NULL OR g.year = :year)
        AND m.role IN (
            com.like_lion.tomato.global.auth.model.Role.MEMBER,
            com.like_lion.tomato.global.auth.model.Role.ADMIN,
            com.like_lion.tomato.global.auth.model.Role.MASTER
        )
    """)
    Page<Member> findAllByPartAndYearAndRoleIn(
            @Param("part") Part part,
            @Param("year") Integer year,
            Pageable pageable
    );


}
