package com.like_lion.tomato.domain.archive.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.archive.project.entity.constant.ProjectCategory;

import java.util.List;

public record ProjectListResponse(
        @JsonProperty("projects")
        List<ProjectSummary> projects,
        @JsonProperty("page")
        int page,
        @JsonProperty("total_pages")
        int totalPages
) {
        public record ProjectSummary(
                String id,
                String title,
                String description,
                ProjectCategory category,
                @JsonProperty("team_name")
                String teamName,
                ThumbnailInfo thumbnail
        ) {}

        public record ThumbnailInfo(
                @JsonProperty("fileKey")
                String fileKey,
                String name,
                @JsonProperty("mimeType")
                String mimeType,
                Long size,
                @JsonProperty("presignedUrl")
                String presignedUrl,
                @JsonProperty("expireAt")
                Long expireAt
        ) {}

}
