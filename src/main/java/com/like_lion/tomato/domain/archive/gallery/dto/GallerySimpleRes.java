package com.like_lion.tomato.domain.archive.gallery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.archive.gallery.entity.GalleryType;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GallerySimpleRes {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("year")
    private final int year;

    @JsonProperty("category")
    private final GalleryType category;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("image_file")
    private final PresignedUrlRes imageFile;

    @JsonProperty("uploaded_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private final LocalDateTime uploadedAt; // ISO8601 문자열

    @Builder
    public GallerySimpleRes(String id, int year, String title, String description, GalleryType category, PresignedUrlRes imageFile, LocalDateTime uploadedAt) {
        this.id = id;
        this.year = year;
        this.category = category;
        this.title = title;
        this.description = description;
        this.imageFile = imageFile;
        this.uploadedAt = uploadedAt;
    }
}
