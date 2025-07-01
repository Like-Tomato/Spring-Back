package com.like_lion.tomato.domain.session.repository;

import com.like_lion.tomato.domain.session.entity.session.SessionFIle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionFileRepository extends JpaRepository<SessionFIle, String> {
    // 필요하다면 세션별 파일 리스트 조회 등 추가 메서드 선언 가능
}