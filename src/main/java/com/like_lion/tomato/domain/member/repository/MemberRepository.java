package com.like_lion.tomato.domain.member.repository;

import com.like_lion.tomato.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(String id);

    @Query("SELECT m FROM Member m " +
            "WHERE (:part IS NULL OR m.part = :part) " +
            "AND (:year IS NULL OR m.year = :year)")
    Page<Member> findAllByPartAndYear(
            @Param("part") String part,
            @Param("year") Integer year,
    Pageable pageable
    );

}
