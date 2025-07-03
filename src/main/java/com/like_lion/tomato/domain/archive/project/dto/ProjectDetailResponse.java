package com.like_lion.tomato.domain.archive.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.archive.project.entity.constant.Platform;
import com.like_lion.tomato.domain.archive.project.entity.constant.ProjectCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponse(
        String id,
        String title,
        String subtitle,

        @JsonProperty("project_url")
        String projectUrl,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @JsonProperty("started_at")
        LocalDate startedAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @JsonProperty("finished_at")
        LocalDate finishedAt,

        List<ProjectImageInfo> images,

        @JsonProperty("team_members")
        List<String> teamMembers,

        @JsonProperty("team_name")
        String teamName,

        String description,
        Integer years,
        ProjectCategory category,
        Platform platform,

        @JsonProperty("is_excellent")
        Boolean isExcellent,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @JsonProperty("uploaded_at")
        LocalDateTime uploadedAt
) {
        public record ProjectImageInfo(
                @JsonProperty("fileKey")
                String fileKey,

                @JsonProperty("mimeType")
                String mimeType,

                Long size,

                @JsonProperty("presignedUrl")
                String presignedUrl,

                @JsonProperty("expireAt")
                Long expireAt
        ) { }
}

