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

    @Query("SELECT DISTINCT m FROM Member m " +
            "JOIN m.memberGenerations mg " +
            "JOIN mg.generation g " +
            "WHERE (:part IS NULL OR g.part = :part) " +
            "AND (:year IS NULL OR g.year = :year)")
    Page<Member> findAllByPartAndYear(
            @Param("part") Part part,
            @Param("year") Integer year,
            Pageable pageable
    );


}
