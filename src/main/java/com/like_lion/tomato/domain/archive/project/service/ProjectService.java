package com.like_lion.tomato.domain.archive.project.service;

import com.like_lion.tomato.domain.archive.project.dto.ProjectDto;
import com.like_lion.tomato.domain.archive.project.entity.Project;
import com.like_lion.tomato.domain.archive.project.entity.ProjectImage;
import com.like_lion.tomato.domain.archive.project.exception.ProjectErrorCode;
import com.like_lion.tomato.domain.archive.project.exception.ProjectException;
import com.like_lion.tomato.domain.archive.project.repository.ProjectImageRepository;
import com.like_lion.tomato.domain.archive.project.repository.ProjectRepository;
import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.exception.GenerationErrorCode;
import com.like_lion.tomato.domain.member.exception.GenerationException;
import com.like_lion.tomato.domain.member.repository.GenerationRepository;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.infra.s3.service.S3PresignedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.like_lion.tomato.domain.archive.project.dto.ProjectDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final GenerationRepository generationRepository;
    private final S3PresignedService preSignedService;

    public Response uploadProject(UploadRequest request, String authorization) {

        validateRequest(request);
        validateTeamMembers(request.teamMembers());

        Member member = memberService.extractMemberFromToken(authorization);

        Generation generation = generationRepository.findByYear(request.years())
                .orElseThrow(() -> new GenerationException(GenerationErrorCode.GENERATION_NOT_FOUND));

        Project project = createProject(request, member, generation);
        projectRepository.save(project);

        ProjectImage projectImage = createProjectImage(request, project);
        projectImageRepository.save(projectImage);

        String preSignedUrl = preSignedService.generateUploadUrl(
                request.getFileKey(),
                "image/*",
                3600 // 1시간
        );

        log.info("프로젝트 업로드 완료: projectId={}, title={}, generation={}",
                project.getId(), project.getTitle(), generation.getYear());

        return new ProjectDto.Response(preSignedUrl, request.getFileKey());
    }

    private void validateRequest(UploadRequest request) {
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

    private Project createProject(UploadRequest request, Member member, Generation generation) {
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

    private ProjectImage createProjectImage(UploadRequest request, Project project) {
        return ProjectImage.builder()
                .fileKey(request.getFileKey())
                .setOrder(1)
                .project(project)
                .build();
    }
}
