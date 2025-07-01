package com.like_lion.tomato.domain.recruitment.service.subscription;

import com.like_lion.tomato.domain.recruitment.entity.subscription.RecruitmentSchedule;
import com.like_lion.tomato.global.exception.GlobalErrorCode;
import com.like_lion.tomato.global.exception.GlobalException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Service
public class EmailService {
    private static final String RECRUITMENT_SUBSCRIPTION_MAIL_SUBJECT = "[멋쟁이 사자처럼 대학 서울과학기술대학교 모집 알림] 14기 모집을 시작합니다 🦁";
    private static final String RECRUITMENT_SUBSCRIPTION_MAIL_TEMPLATE = "RecruitmentMailTemplate";
    private static final String PERSONAL = "멋쟁이 사자처럼 대학 서울과학기술대학교";

    private final String from;
    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;

    public EmailService(
            @Value("${spring.mail.username}") String from,
            JavaMailSender mailSender,
            ITemplateEngine templateEngine
    ) {
        this.from = from;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRecruitmentNotification(
            String address,
            RecruitmentSchedule schedule
    ) {
        Context context = new Context();
        context.setVariable("title", schedule.getTitle());
        context.setVariable("description", schedule.getDescription());
        context.setVariable("startDate", schedule.getStartDate());
        context.setVariable("endDate", schedule.getEndDate());

        String htmlTemplate = templateEngine.process(RECRUITMENT_SUBSCRIPTION_MAIL_TEMPLATE, context);
        try {
            MimeMessage mimeMessage = generateMessage(address, htmlTemplate);
            mailSender.send((MimeMessagePreparator) mimeMessage);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode.BAD_REQUEST);
        }
    }

    public void sendRecruitmentNotificationBatch(List<String> addresses, RecruitmentSchedule schedule) {
        for (String address : addresses) {
            try {
                sendRecruitmentNotification(address, schedule);
            } catch (Exception e) {
                log.error("이메일 발송 실패: {}", address, e);
            }
        }
    }

    private MimeMessage generateMessage(
            String address,
            String htmlTemplate
    ) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo(address);
        mimeMessageHelper.setFrom(new InternetAddress(from, PERSONAL, "UTF-8"));
        mimeMessageHelper.setSubject(EmailService.RECRUITMENT_SUBSCRIPTION_MAIL_SUBJECT);
        mimeMessageHelper.setText(htmlTemplate, true);

        return mimeMessage;
    }
}
