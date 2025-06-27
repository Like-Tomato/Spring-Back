package com.like_lion.tomato.domain.archieve.entity;

import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class GalleryImage {
    @DomainId(DomainType.GALLERY_IMAGE)
    @Id
    @Column(name = "gallery_image_id")
    private String id;

    private String imageUrl;

    private int setOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery")
    private Gallery gallery;
}
