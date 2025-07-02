package com.like_lion.tomato.infra.s3.api;

import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.infra.s3.dto.request.PresignedUrlReq;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import com.like_lion.tomato.infra.s3.service.S3PresignedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/session/file")
@RequiredArgsConstructor
public class PresignedController {

    private final S3PresignedService s3PresignedService;

    @Operation(
            summary = "S3 파일 업로드용 Presigned URL 발급",
            description = "프론트에서 UUID를 포함해 생성한 fileKey(예: session/1234/assignment_uuid.pdf)와 mimeType을 받아 Presigned URL과 S3 저장 키를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/presigned-upload")
    public ApiResponse<PresignedUrlRes> getPresignedUploadUrl(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "S3 presigned 업로드 요청 DTO",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PresignedUrlReq.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody @Valid PresignedUrlReq request
    ) {
        return ApiResponse.success(s3PresignedService.getPresignedUrlForPut(request));
    }

    @Operation(
            summary = "S3 파일 다운로드용 Presigned URL 발급",
            description = "S3에 저장된 파일의 key(경로+파일명, 예: session/1234/assignment_uuid.pdf)를 받아 presigned 다운로드 URL과 파일 key를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/presigned-download")
    public ApiResponse<PresignedUrlRes> getPresignedDownloadUrl(
            @Parameter(
                    description = "S3 파일의 key(경로+파일명, 예: session/1234/assignment_uuid.pdf)",
                    required = true,
                    example = "session/1234/assignment_2f8a1f6c-7a3b-4e0b-9e1c-123456789abc.pdf"
            )
            @RequestParam String fileKey
    ) {
        return ApiResponse.success(s3PresignedService.getPresignedUrlForGet(fileKey));
    }
}
