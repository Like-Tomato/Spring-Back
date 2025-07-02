package com.like_lion.tomato.domain.archive.gallery.service;

import com.like_lion.tomato.domain.archive.exception.ArchiveErrorCode;
import com.like_lion.tomato.domain.archive.exception.ArchiveException;
import com.like_lion.tomato.domain.archive.gallery.dto.GalleryListRes;
import com.like_lion.tomato.domain.archive.gallery.dto.GallerySimpleRes;
import com.like_lion.tomato.domain.archive.gallery.entity.Gallery;
import com.like_lion.tomato.domain.archive.gallery.entity.GalleryType;
import com.like_lion.tomato.domain.archive.gallery.repository.GalleryRepository;
import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import com.like_lion.tomato.infra.s3.service.S3PresignedService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final S3PresignedService s3PresignedService;

    @Transactional
    public GalleryListRes readAllGallery(int page, int size, String category, int year) {
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        if(!category.isBlank() && GalleryType.isValid(category)) throw new ArchiveException(ArchiveErrorCode.INVALID_CATEGORY);
        GalleryType categoryEnum = GalleryType.valueOf(category.toUpperCase());

        if(!Generation.isValidYear(year)) throw new MemberException(MemberErrorCode.INVALID_YEAR);
        Integer integerYear = year;

        Page<Gallery> galleryPage = galleryRepository.findAllByCategoryAndYear(categoryEnum, year, pageable);

        List<GallerySimpleRes> gallerySimpleResList = galleryPage.getContent().stream()
                .map(gallery -> {
                    // fileKey만 바로 추출해서 presigned url 발급
                    return GallerySimpleRes.builder()
                            .id(gallery.getId())
                            .year(gallery.getGeneration().getYear())
                            .category(gallery.getCategory())
                            .title(gallery.getTitle())
                            .description(gallery.getDescription())
                            .imageFile(s3PresignedService.getPresignedUrlForGet(gallery.getThumbnailFileKey()))
                            .uploadedAt(gallery.getCreatedAt())
                            .build();
                })
                .toList();

        return GalleryListRes.builder()
                .galleries(gallerySimpleResList)
                .page(page)
                .totalPages(galleryPage.getTotalPages())
                .build();
    }
}
