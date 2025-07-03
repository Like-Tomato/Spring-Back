package com.like_lion.tomato.domain.archive.project.service;

import com.like_lion.tomato.domain.archive.project.dto.ProjectDetailResponse;
import com.like_lion.tomato.domain.archive.project.dto.ProjectDetailResponse.ProjectImageInfo;
import com.like_lion.tomato.domain.archive.project.dto.ProjectListResponse;
import com.like_lion.tomato.domain.archive.project.entity.Project;
import com.like_lion.tomato.domain.archive.project.entity.ProjectImage;
import com.like_lion.tomato.domain.archive.project.entity.constant.ProjectCategory;
import com.like_lion.tomato.domain.archive.project.exception.ProjectErrorCode;
import com.like_lion.tomato.domain.archive.project.exception.ProjectException;
import com.like_lion.tomato.domain.archive.project.repository.ProjectImageRepository;
import com.like_lion.tomato.domain.archive.project.repository.ProjectRepository;
import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.infra.s3.service.S3PresignedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.like_lion.tomato.domain.archive.project.dto.ProjectDto.*;
import static com.like_lion.tomato.domain.archive.project.dto.ProjectListResponse.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MemberRepository memberRepository;
    private final S3PresignedService s3PresignedService;

    public Response uploadProject(WriteRequest request, String authorization) {
        return null;
    }

    private void validateRequest(WriteRequest request) {
        request.validateDateRange();
        request.validateFileFormat();
    }

    private void validateTeamMembers(List<String> teamMembers) {
        for (String memberName : teamMembers) {
            if (!memberRepository.existsByUsername((memberName))) {
                throw new ProjectException(ProjectErrorCode.INVALID_TEAM_MEMBER);
            }
        }
    }

    private Project createProject(WriteRequest request, Member member, Generation generation) {
        return Project.builder()
                .title(request.title())
                .subtitle(request.subtitle())
                .description(request.description())
                .isBest(request.isExcellent())
                .projectUrl(request.projectUrl())
                .startedAt(request.startedAt())
                .finishedAt(request.finishedAt())
                .teamMembers(request.teamMembers())
                .teamName(request.teamName())
                .category(request.category())
                .platform(request.platform())
                .member(member)
                .generation(generation)
                .build();
    }

    private ProjectImage createProjectImage(WriteRequest request, Project project) {
        return ProjectImage.builder()
                .fileKey(request.getFileKey())
                .setOrder(1)
                .project(project)
                .build();
    }

    public ApiResponse.MessageData deleteProject(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        List<ProjectImage> projectImages = projectImageRepository.findByProjectIdOrderBySetOrderAsc(projectId);

        projectRepository.delete(project);

        s3PresignedService.deleteFiles(projectImages.stream()
            .map(ProjectImage::getFileKey)
            .toList());

        return new ApiResponse.MessageData("프로젝트 게시물이 삭제되었습니다.");
    }

    public ApiResponse.MessageData updateProject(WriteRequest request) {
        validateRequest(request);
        validateTeamMembers(request.extractMemberNames());

        return new ApiResponse.MessageData("프로젝트 게시물이 성공적으로 수정되었습니다.");
    }

    public ProjectListResponse getProjects(Integer year, String category, int page, int size) {
        validateProjectListParams(category, page, size);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Project> projectPage = getProjectsByFilters(year, category, pageable);

        List<ProjectSummary> projectSummaries = projectPage.getContent().stream()
                .map(this::convertToProjectSummary)
                .toList();

        return new ProjectListResponse(
                projectSummaries,
                page,
                projectPage.getTotalPages()
        );
    }

    private void validateProjectListParams(String category, int page, int size) {
        if (category != null && !isValidCategory(category)) {
            throw new ProjectException(ProjectErrorCode.INVALID_CATEGORY);
        }

        if (page < 1) {
            throw new ProjectException(ProjectErrorCode.INVALID_PAGE);
        }

        if (size < 1 || size > 100) {
            throw new ProjectException(ProjectErrorCode.INVALID_SIZE);
        }
    }

    private boolean isValidCategory(String category) {
        try {
            ProjectCategory.valueOf(category.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Page<Project> getProjectsByFilters(Integer year, String category, Pageable pageable) {
        if (year != null && category != null) {
            ProjectCategory projectCategory = ProjectCategory.valueOf(category.toUpperCase());
            return projectRepository.findByYearAndCategory(year, projectCategory, pageable);
        } else if (year != null) {
            return projectRepository.findByYear(year, pageable);
        } else if (category != null) {
            ProjectCategory projectCategory = ProjectCategory.valueOf(category.toUpperCase());
            return projectRepository.findByCategory(projectCategory, pageable);
        } else {
            return projectRepository.findAll(pageable);
        }
    }

    private ProjectSummary convertToProjectSummary(Project project) {
        ThumbnailInfo thumbnail = createThumbnailInfo(project);

        return new ProjectSummary(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getCategory(),
                project.getTeamName(),
                thumbnail
        );
    }

    private ThumbnailInfo createThumbnailInfo(Project project) {
        Optional<ProjectImage> firstImage = project.getProjectImages().stream()
                .min(Comparator.comparing(ProjectImage::getSetOrder));

        if (firstImage.isPresent()) {
            ProjectImage image = firstImage.get();

            String presignedUrl = s3PresignedService.generateDownloadUrl(image.getFileKey());

            String fileName = extractFileName(image.getFileKey());
            String mimeType = determineMimeType(fileName);
            Long fileSize = getFileSize(image.getFileKey()); // S3에서 파일 크기 조회 필요
            Long expireAt = System.currentTimeMillis() + (3600 * 1000); // 1시간 후 만료

            return new ThumbnailInfo(
                    image.getFileKey(),
                    fileName,
                    mimeType,
                    fileSize,
                    presignedUrl,
                    expireAt
            );
        }

        return new ThumbnailInfo(null, null, null, null, null, null);
    }

    private String extractFileName(String fileKey) {
        if (fileKey == null) return null;
        String[] parts = fileKey.split("/");
        return parts[parts.length - 1];
    }

    private String determineMimeType(String fileName) {
        if (fileName == null) return null;

        String extension = fileName.toLowerCase().substring(fileName.lastIndexOf(".") + 1);
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

    private Long getFileSize(String fileKey) {
        // S3에서 파일 크기를 조회하는 로직
        try {
            return s3PresignedService.getFileSize(fileKey);
        } catch (Exception e) {
            return 0L; // 기본값
        }
    }

    public ProjectDetailResponse getProjectDetail(String projectId) {
        Project project = projectRepository.findByIdWithImages(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        return convertToProjectDetailResponse(project);
    }

    private ProjectDetailResponse convertToProjectDetailResponse(Project project) {
        List<ProjectImageInfo> images = project.getProjectImages().stream()
                .sorted(Comparator.comparing(ProjectImage::getSetOrder))
                .map(this::convertToProjectImageInfo)
                .toList();

        LocalDateTime uploadedAt = project.getCreatedAt();
        Integer years = project.getGeneration() != null ? project.getGeneration().getYear() : null;

        return new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getSubtitle(),
                project.getProjectUrl(),
                project.getStartedAt(),
                project.getFinishedAt(),
                images,
                project.getTeamMembers(),
                project.getTeamName(),
                project.getDescription(),
                years,
                project.getCategory(),
                project.getPlatform(),
                project.isBest(),
                uploadedAt
        );
    }

    private ProjectImageInfo convertToProjectImageInfo(ProjectImage image) {
        String presignedUrl = s3PresignedService.generateDownloadUrl(image.getFileKey());
        String mimeType = determineMimeType(image.getFileKey());
        Long fileSize = getFileSize(image.getFileKey());
        Long expireAt = System.currentTimeMillis() + (3600 * 1000); // 1시간 후 만료

        return new ProjectImageInfo(
                image.getFileKey(),
                mimeType,
                fileSize,
                presignedUrl,
                expireAt
        );
    }
}
