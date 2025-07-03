package com.like_lion.tomato.domain.archive.gallery.repository;


import com.like_lion.tomato.domain.archive.gallery.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, String> {
    List<GalleryImage> findByGalleryIdOrderBySetOrderAsc(String galleryId);
}