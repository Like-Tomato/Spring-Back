package com.like_lion.tomato.domain.recruitment.repository.common;

import com.like_lion.tomato.domain.recruitment.entity.common.RecruitmentCommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentCommonQuestionRepository extends JpaRepository<RecruitmentCommonQuestion, String> {
    List<RecruitmentCommonQuestion> findAllByOrderBySortOrderAsc();
}
