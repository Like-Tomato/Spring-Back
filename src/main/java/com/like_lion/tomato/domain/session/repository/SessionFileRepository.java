package com.like_lion.tomato.domain.session.repository;

import com.like_lion.tomato.domain.session.entity.session.SessionFIle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionFileRepository extends JpaRepository<SessionFIle, String> {
    Optional<SessionFIle> findBySessionId(String sessionId);
    // 필요하다면 세션별 파일 리스트 조회 등 추가 메서드 선언 가능
}