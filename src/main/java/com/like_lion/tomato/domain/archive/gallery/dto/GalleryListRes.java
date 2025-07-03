package com.like_lion.tomato.domain.archive.gallery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GalleryListRes {
    @JsonProperty("galleries")
    private final List<GallerySimpleRes> galleries;

    @JsonProperty("page")
    private final int page;

    @JsonProperty("total_pages")
    private final int totalPages;

    @Builder
    public GalleryListRes(List<GallerySimpleRes> galleries, int page, int totalPages) {
        this.galleries = galleries;
        this.page = page;
        this.totalPages = totalPages;
    }

    public static GalleryListRes from(List<GallerySimpleRes> galleries, int page, int totalPages) {
        return GalleryListRes.builder()
                .galleries(galleries)
                .page(page)
                .totalPages(totalPages)
                .build();
    }
}