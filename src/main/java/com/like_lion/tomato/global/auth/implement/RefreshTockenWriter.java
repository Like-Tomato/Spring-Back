package com.like_lion.tomato.global.auth.implement;


import com.like_lion.tomato.domain.auth.entity.RefreshTocken;
import com.like_lion.tomato.domain.auth.repository.RefreshTockenRepository;
import com.like_lion.tomato.global.auth.dto.TockenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTockenWriter {

    private final RefreshTockenRepository refreshTockenRepository;
    private final RefreshTockenReader refreshTockenReader;

    public void upsetRefreshTocken(TockenDto tockenDto) {
        refreshTockenReader.findOptionalByUsername(tockenDto.getUsername())
                .ifPresentOrElse(
                        tocken -> tocken.updatePayload(tockenDto.getRefreshTocken()),
                        () -> this.create(tockenDto)
                );
    }

    private void create(TockenDto tockenDto) {
        refreshTockenRepository.save(new RefreshTocken(tockenDto.getRefreshTocken(), tockenDto.getUsername()));
    }

    public void removeAllByUsername(String username) {
        refreshTockenRepository.deleteAllByUsername(username);
    }
}

