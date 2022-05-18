package com.ABH.Auction.jobs;

import com.ABH.Auction.services.EmailService;
import lombok.AllArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailJob extends QuartzJobBean {
    private final EmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String email = jobDataMap.getString("email");
        String body = jobDataMap.getString("body");
        String subject = jobDataMap.getString("subject");
        emailService.sendEmailWithSubject(email, body, subject);
    }
}
