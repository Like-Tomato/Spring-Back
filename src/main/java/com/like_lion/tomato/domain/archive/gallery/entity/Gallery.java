package com.like_lion.tomato.domain.archive.gallery.entity;

import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Gallery extends BaseEntity {

    @DomainId(DomainType.GALLERY)
    @Id
    @Column(name = "gallery_id")
    private String id;

    @Enumerated(EnumType.STRING)
    GalleryType category;

    @Column(length = 50)
    private String title;

    @Column(length = 200)
    private String description;

    @Column
    private String thumbnailFileKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private Generation generation;

}
