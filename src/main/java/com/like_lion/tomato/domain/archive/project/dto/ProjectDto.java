package com.like_lion.tomato.domain.archive.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.like_lion.tomato.domain.archive.exception.ArchiveErrorCode;
import com.like_lion.tomato.domain.archive.exception.ArchiveException;
import com.like_lion.tomato.domain.archive.project.entity.constant.Platform;
import com.like_lion.tomato.domain.archive.project.entity.constant.ProjectCategory;
import com.like_lion.tomato.domain.archive.project.exception.ProjectErrorCode;
import com.like_lion.tomato.domain.archive.project.exception.ProjectException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ProjectDto(
) {
    public record WriteRequest(
            String title,
            String subtitle,
            @JsonProperty("project_url")
            String projectUrl,
            @JsonFormat(pattern = "yyyy-MM-dd")
            @JsonProperty("started_at")
            LocalDate startedAt,
            @JsonFormat(pattern = "yyyy-MM-dd")
            @JsonProperty("finished_at")
            LocalDate finishedAt,
            String prefix,
            String filename,
            @JsonProperty("team_members")
            List<String> teamMembers,
            @JsonProperty("team_name")
            String teamName,
            String description,
            Integer years,
            ProjectCategory category,
            @JsonProperty("platform")
            Platform platform,
            @JsonProperty("is_excellent")
            Boolean isExcellent
    ) {

        public void validateDateRange() {
            if (startedAt.isAfter(finishedAt)) {
                throw new ArchiveException(ArchiveErrorCode.INVALID_DATE_RANGE);
            }
        }

        public void validateFileFormat() {
            String extension = filename.toLowerCase();
            List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

            boolean isValidFormat = allowedExtensions.stream()
                    .anyMatch(extension::endsWith);

            if (!isValidFormat) {
                throw new ProjectException(ProjectErrorCode.INVALID_FILE_FORMAT);
            }
        }

        public void validateTeamMemberFormat() {
            for (String teamMember : teamMembers) {
                if (!teamMember.contains("_") || teamMember.split("_").length != 2) {
                    throw new ProjectException(ProjectErrorCode.INVALID_TEAM_MEMBER_FORMAT);
                }

                String[] parts = teamMember.split("_");
                String part = parts[0];
                String name = parts[1];

                if (!isValidPart(part)) {
                    throw new ProjectException(ProjectErrorCode.INVALID_PART);
                }

                if (name.trim().isEmpty()) {
                    throw new ProjectException(ProjectErrorCode.INVALID_MEMBER_NAME);
                }
            }
        }

        private boolean isValidPart(String part) {
            List<String> validParts = List.of("FE", "BE", "PM", "DESIGN", "AI");
            return validParts.contains(part.toUpperCase());
        }

        public String getFileKey() {
            return prefix + "/" + filename;
        }

        public List<String> extractMemberNames() {
            return teamMembers.stream()
                    .map(member -> member.split("_")[1])
                    .toList();
        }

        public Map<String, List<String>> groupMembersByPart() {
            return teamMembers.stream()
                    .collect(Collectors.groupingBy(
                            member -> member.split("_")[0],
                            Collectors.mapping(
                                    member -> member.split("_")[1],
                                    Collectors.toList()
                            )
                    ));
        }


    }
    public record Response(
            String presignedUrl,
            String filekey
    ) {
    }

}
