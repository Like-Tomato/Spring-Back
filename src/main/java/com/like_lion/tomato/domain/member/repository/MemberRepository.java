package com.like_lion.tomato.domain.member.repository;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.auth.model.Role;
import com.like_lion.tomato.global.common.enums.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(String id);
    Optional<Member> findByUsername(String username);

    @Query("SELECT DISTINCT m FROM Member m " +
            "JOIN m.memberGenerations mg " +
            "JOIN mg.generation g " +
            "WHERE m.part = :part AND g.year = :year AND m.role IN (:roles)")
    Page<Member> findAllByPartAndYearAndRoleIn(
            @Param("part") Part part,
            @Param("year") Integer year,
            @Param("roles") List<Role> roles,
            Pageable pageable
    );

    boolean existsByUsername(String username);
}
