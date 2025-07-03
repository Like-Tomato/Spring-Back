package com.like_lion.tomato.domain.archive.gallery.repository;

import com.like_lion.tomato.domain.archive.gallery.entity.Gallery;
import com.like_lion.tomato.domain.archive.gallery.entity.GalleryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GalleryRepository extends JpaRepository<Gallery, String> {
    @Query("""
        SELECT g FROM Gallery g
        WHERE (:category IS NULL OR g.category = :category)
          AND (:year IS NULL OR g.generation.year = :year)
        ORDER BY g.createdAt DESC
    """)
    Page<Gallery> findAllByCategoryAndYear(
            @Param("category") GalleryType category,
            @Param("year") Integer year,
            Pageable pageable
    );
}