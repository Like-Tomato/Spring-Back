package com.like_lion.tomato.domain.archive.project.repository;

import com.like_lion.tomato.domain.archive.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
