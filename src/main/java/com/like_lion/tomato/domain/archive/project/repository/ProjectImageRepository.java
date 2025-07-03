package com.like_lion.tomato.domain.archive.project.repository;

import com.like_lion.tomato.domain.archive.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, String> {
    List<ProjectImage> findByProjectIdOrderBySetOrderAsc(String projectId);
}
