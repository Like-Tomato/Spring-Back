package com.like_lion.tomato.domain.recruitment.repository.subscription;

import com.like_lion.tomato.domain.recruitment.entity.subscription.RecruitmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecruitmentScheduleRepository extends JpaRepository<RecruitmentSchedule, String> {
    @Query("SELECT r FROM RecruitmentSchedule r " +
            "WHERE r.isActive = true " +
            "AND r.emailSent = false " +
            "AND DATE(r.startDate) = DATE(:today)")
    List<RecruitmentSchedule> findTodayRecruitmentToNotify(@Param("today") LocalDateTime today);
}
