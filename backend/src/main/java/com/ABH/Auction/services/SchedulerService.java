package com.ABH.Auction.services;

import com.ABH.Auction.domain.User;
import com.ABH.Auction.jobs.EmailJob;
import com.ABH.Auction.jobs.NotificationJob;
import com.ABH.Auction.responses.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final EmailService emailService;
    private final UserService userService;
    private final Scheduler scheduler;

    @Value("${frontend.path}")
    private String frontend;

    public MessageResponse scheduleEmail(String token, Long id) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(LocalDateTime.now()
                    .plusDays(10), TimeZone.getDefault().toZoneId());
            JobDetail jobDetail = buildEmailJD(token, id);
            Trigger trigger = buildTrigger(jobDetail, dateTime, "email-triggers",
                    "Send email trigger.");
            scheduler.scheduleJob(jobDetail, trigger);
        }
        catch (SchedulerException ex) {
            log.info("Email not recorded!", ex);
            return new MessageResponse("Email not scheduled!", false);
        }
        return new MessageResponse();
    }

    public void scheduleNotification(Long productId, LocalDateTime dt) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(dt.minusMinutes(15),
                    TimeZone.getDefault().toZoneId());
            JobDetail jobDetail = buildNotificationJD(productId, false);
            Trigger trigger = buildTrigger(jobDetail, dateTime, "notification-triggers",
                    "Send notification trigger.");
            scheduler.scheduleJob(jobDetail, trigger);
            dateTime = ZonedDateTime.of(dt, TimeZone.getDefault().toZoneId());
            jobDetail = buildNotificationJD(productId, true);
            trigger = buildTrigger(jobDetail, dateTime, "notification-triggers",
                    "Send notification trigger.");
            scheduler.scheduleJob(jobDetail, trigger);
        }
        catch (SchedulerException ex) {
            log.info("Notification not recorded!", ex);
        }
        log.info("Notification recorded!");
    }

    public JobDetail buildEmailJD(String token, Long productId) {
        JobDataMap jobDataMap = new JobDataMap();
        User user = userService.getUserFromToken(token);
        jobDataMap.put("email", user.getEmail());
        jobDataMap.put("subject", "Rate seller.");
        String link = frontend + "/product/" + productId + "?review=true";
        jobDataMap.put("body", emailService.rateSellerTemplate(user.getFirstName(), link));
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send email job.")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public JobDetail buildNotificationJD(Long productId, boolean startPayment) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("product", productId);
        jobDataMap.put("startPayment", startPayment);
        return JobBuilder.newJob(NotificationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "notification-jobs")
                .withDescription("Send notification job.")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt, String group, String description) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), group)
                .withDescription(description)
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
