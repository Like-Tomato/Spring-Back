package com.like_lion.tomato.domain.recruitment.repository.part;

import com.like_lion.tomato.domain.recruitment.entity.part.RecruitmentPartQuestion;
import com.like_lion.tomato.global.common.enums.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentPartQuestionRepository extends JpaRepository<RecruitmentPartQuestion, String> {
    List<RecruitmentPartQuestion> findByPartOrderBySortOrderAsc(Part part);
}
