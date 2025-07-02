package com.like_lion.tomato.domain.member.repository;

import com.like_lion.tomato.domain.member.entity.Generation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenerationRepository extends JpaRepository<Generation, String> {
    @Query("SELECT g FROM Generation g WHERE g.isCurrent = true")
    Optional<Generation> findCurrentGeneration();

    Optional<Generation> findByYear(Integer year);
}
