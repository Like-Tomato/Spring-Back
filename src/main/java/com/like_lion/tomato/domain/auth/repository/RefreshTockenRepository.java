package com.like_lion.tomato.domain.auth.repository;

import com.like_lion.tomato.domain.auth.entity.RefreshTocken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTockenRepository extends JpaRepository<RefreshTocken, String> {
    Optional<RefreshTocken> findByPayload(String payload);

    void deleteAllByUsername(String username);

    Optional<RefreshTocken> findByUsername(String username);


}
