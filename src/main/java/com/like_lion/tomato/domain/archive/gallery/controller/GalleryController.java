package com.like_lion.tomato.domain.archive.gallery.controller;



import com.like_lion.tomato.domain.archive.gallery.dto.GalleryListRes;
import com.like_lion.tomato.domain.archive.gallery.service.GalleryService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class GalleryController {

    private final GalleryService galleryService;

    @Operation(
            summary = "갤러리 전체 목록 조회",
            description = "페이지, 사이즈, 카테고리, 연도별로 갤러리 목록을 조회합니다."
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
