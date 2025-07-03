package com.like_lion.tomato.domain.archive.project.repository;

import com.like_lion.tomato.domain.archive.project.entity.Project;
import com.like_lion.tomato.domain.archive.project.entity.constant.ProjectCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query("SELECT p FROM Project p JOIN p.generation g WHERE g.year = :year")
    Page<Project> findByYear(@Param("year") Integer year, Pageable pageable);

    Page<Project> findByCategory(ProjectCategory category, Pageable pageable);

    @Query("SELECT p FROM Project p JOIN p.generation g WHERE g.year = :year AND p.category = :category")
    Page<Project> findByYearAndCategory(@Param("year") Integer year,
                                        @Param("category") ProjectCategory category,
                                        Pageable pageable);

    @Query("SELECT p FROM Project p " +
                  "LEFT JOIN FETCH p.projectImages pi " +
                  "LEFT JOIN FETCH p.generation g " +
                  "WHERE p.id = :projectId")
    Optional<Project> findByIdWithImages(@Param("projectId") String projectId);
}
