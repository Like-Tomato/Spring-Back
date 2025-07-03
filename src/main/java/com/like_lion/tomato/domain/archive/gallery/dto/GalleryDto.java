package com.like_lion.tomato.domain.archive.gallery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

public class GalleryDto {

    @Builder
    public record GalleryImageDto(
            @JsonProperty("fileKey") String fileKey,
            @JsonProperty("name") String name,
            @JsonProperty("mimeType") String mimeType,
            @JsonProperty("size") Long size,
            @JsonProperty("presignedUrl") String presignedUrl,
            @JsonProperty("expireAt") Long expireAt
    ) {}

    @Builder
    public record GalleryResponse(
            @JsonProperty("id") String id,
            @JsonProperty("year") Integer year,
            @JsonProperty("category") String category,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("image_file") GalleryImageDto imageFile,
            @JsonProperty("uploaded_at") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") ZonedDateTime uploadedAt
    ) {}

    @Builder
    public record GalleryListResponse(
            @JsonProperty("galleries") List<GalleryResponse> galleries,
            @JsonProperty("page") int page,
            @JsonProperty("total_pages") int totalPages
    ) {}

    public record GalleryFilterRequest(
            Integer year,
            String category,
            Integer page,
            Integer size
    ) {}
}
