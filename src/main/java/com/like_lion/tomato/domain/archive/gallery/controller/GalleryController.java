package com.like_lion.tomato.domain.archive.gallery.controller;

import com.like_lion.tomato.domain.archive.gallery.dto.GalleryListRes;
import com.like_lion.tomato.domain.archive.gallery.service.GalleryService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Tag(name = "Gallery", description = "갤러리(활동사진) 관련 API")
public class GalleryController {

    private final GalleryService galleryService;

    @Operation(
            summary = "갤러리 전체 목록 조회",
            description = """
                    페이지, 사이즈, 카테고리, 연도별로 갤러리 게시물 목록을 조회합니다.

                    - **category**: ALL, REGULAR_SESSION, CENTRAL_ACTIVITY, OWN_ACTIVITY, SOCIAL_ACTIVITY
                    - **year**: 13, 14 등 기수별 필터링 (없으면 전체)
                    - **page**: 1부터 시작 (기본값 1)
                    - **size**: 한 페이지당 게시물 수 (기본값 30)
                    """,
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (1부터 시작)", example = "1"),
                    @Parameter(name = "size", description = "페이지 당 게시물 수", example = "30"),
                    @Parameter(name = "category", description = "카테고리 필터 (ALL, REGULAR_SESSION 등)", example = "ALL"),
                    @Parameter(name = "year", description = "기수 필터 (13, 14 등)", example = "13")
            }
    )
    @GetMapping("/gallery")
    public ApiResponse<GalleryListRes> readAllByFilter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer year
    ) {
        return ApiResponse.success(galleryService.readAllGallery(page, size, category, year));
    }
}
