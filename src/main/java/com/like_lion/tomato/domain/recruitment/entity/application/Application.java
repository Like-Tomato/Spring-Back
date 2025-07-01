package com.like_lion.tomato.domain.recruitment.entity.application;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.recruitment.entity.common.RecruitmentCommonAnswer;
import com.like_lion.tomato.domain.recruitment.entity.constant.ApplicationStatus;
import com.like_lion.tomato.domain.recruitment.entity.part.RecruitmentPartAnswer;
import com.like_lion.tomato.global.common.BaseEntity;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "application")
public class Application extends BaseEntity {

    @DomainId(DomainType.APPLICATION)
    @Id
    @Column(name = "application_id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "major")
    private String major;

    @Column(name = "student_id")
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "part")
    private Part part;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany
    private List<RecruitmentCommonAnswer> commonAnswers;

    @OneToMany
    private List<RecruitmentPartAnswer> partAnswers;

    @Builder
    private Application(String username, String phone, String studentId,
                        String major, Part part, String portfolioUrl) {
        this.username = username;
        this.phone = phone;
        this.studentId = studentId;
        this.major = major;
        this.part = part;
        this.portfolioUrl = portfolioUrl;
        this.submittedAt = LocalDateTime.now();
        this.status = ApplicationStatus.NOT_SUBMITTED;
    }

    public void updateStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }

    public boolean isSubmitted() { return submittedAt != null; }

    public void clearAnswers() {
        this.commonAnswers.clear();
        this.partAnswers.clear();
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void addCommonAnswers(List<RecruitmentCommonAnswer> answers) {
        this.commonAnswers.clear();
        answers.forEach(this::addCommonAnswer);
    }

    public void addCommonAnswer(RecruitmentCommonAnswer answer) {
        this.commonAnswers.add(answer);
    }

    public void addPartAnswers(List<RecruitmentPartAnswer> answers) {
        this.partAnswers.clear();
        answers.forEach(this::addPartAnswer);
    }

    public void addPartAnswer(RecruitmentPartAnswer answer) {
        this.partAnswers.add(answer);
    }

    public void updateMajor(String major) {
    }

    public void updatePart(Part part) {
    }

    public void updatePortfolioUrl(String portfolioUrl) {
    }
}
