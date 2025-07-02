package com.like_lion.tomato.domain.archive.project.api;

import com.like_lion.tomato.domain.archive.project.dto.ProjectDto;
import com.like_lion.tomato.domain.archive.project.service.ProjectService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/archive/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<ProjectDto.Response> uploadProject(
            @RequestBody ProjectDto.UploadRequest request,
            @RequestHeader("Authorization") String authorization
    ) {
        ProjectDto.Response response = projectService.uploadProject(request, authorization);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ApiResponse.MessageData> deleteProject(
            @PathVariable String projectId
    ) {
        ApiResponse.MessageData message = projectService.deleteProject(projectId);
        return ApiResponse.success(message);
    }
}
