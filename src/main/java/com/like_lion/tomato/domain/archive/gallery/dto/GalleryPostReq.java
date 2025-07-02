package com.like_lion.tomato.domain.archive.gallery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.archive.gallery.entity.Gallery;
import com.like_lion.tomato.domain.archive.gallery.entity.GalleryType;
import com.like_lion.tomato.domain.member.entity.Generation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GalleryPostReq {
    @JsonProperty("title")
    private String title;

    @JsonProperty("years")
    private int years; // 기수

    @JsonProperty("category")
    private String category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("filekey")
    private String fileKey;

    @JsonProperty("uploaded_at")
    private String uploadedAt; // ISO8601 문자열 (옵션)

    // --- 엔티티 변환 메서드 ---
    public Gallery to(Generation generation) {
        return Gallery.builder()
                .title(this.title)
                .description(this.description)
                .category(GalleryType.valueOf(this.category.toUpperCase()))
                .generation(generation)
                .thumbnailFileKey(this.fileKey)
                .build();
    }
}
