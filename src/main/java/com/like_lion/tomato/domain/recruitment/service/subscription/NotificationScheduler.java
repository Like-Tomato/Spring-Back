package com.like_lion.tomato.domain.recruitment.service.subscription;

import com.like_lion.tomato.domain.recruitment.entity.subscription.RecruitmentSchedule;
import com.like_lion.tomato.domain.recruitment.repository.subscription.RecruitmentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {
    private final EmailService emailService;
    private final SubscriptionService subscriptionService;
    private final RecruitmentScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendDailyRecruitmentNotifications() {
        LocalDateTime today = LocalDateTime.now();
        log.info("모집 알림 스케줄러 실행: {}", today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        List<RecruitmentSchedule> todayRecruitments = scheduleRepository.findTodayRecruitmentToNotify(today);

        if (todayRecruitments.isEmpty()) { return; }

        List<String> subscriberEmails = subscriptionService.getSubscriberEmails();

        if (subscriberEmails.isEmpty()) { return; }

        for (RecruitmentSchedule schedule : todayRecruitments) {
            try {
                log.info("모집 알림 발송 시작: {} (구독자 {} 명)", schedule.getTitle(), subscriberEmails.size());

                emailService.sendRecruitmentNotificationBatch(subscriberEmails, schedule);
                schedule.markEmailSent();
                scheduleRepository.save(schedule);

                log.info("모집 알림 발송 완료: {}", schedule.getTitle());
            } catch (Exception e) {
                log.error("모집 알림 발송 실패: {}", schedule.getTitle(), e);
            }
        }
    }
}

