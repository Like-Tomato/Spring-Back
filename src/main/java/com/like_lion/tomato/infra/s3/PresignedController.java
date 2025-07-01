package com.like_lion.tomato.infra.s3;

import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/session/file")
@RequiredArgsConstructor
public class PresignedController {

    private final S3PresignedService s3PresignedService;

    /**
     * S3 파일 업로드용 Presigned URL 발급
     * @param request prefix(폴더 경로), fileName(원본 파일명) 정보를 담은 요청 DTO
     * @return 업로드용 presigned URL, S3에 저장될 파일 key
     */
    @PostMapping("/presigned-upload")
    public ApiResponse<PresignedUrlRes> getPresignedUploadUrl(@RequestBody @Valid PresignedUrlReq request) {
        return ApiResponse.success(s3PresignedService.getPresignedUrlForPut(request));
    }

    /**
     * S3 파일 다운로드용 Presigned URL 발급
     * @param fileKey S3에 저장된 파일의 key(경로+파일명)
     * @return 다운로드용 presigned URL, 파일 key
     */
    @GetMapping("/presigned-download")
    public ApiResponse<PresignedUrlRes> getPresignedDownloadUrl(@RequestParam String fileKey) {
        return ApiResponse.success(s3PresignedService.getPresignedUrlForGet(fileKey));
    }
}
