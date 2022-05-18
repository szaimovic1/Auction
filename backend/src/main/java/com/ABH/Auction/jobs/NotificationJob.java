package com.ABH.Auction.jobs;

import com.ABH.Auction.services.ProductService;
import lombok.AllArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationJob extends QuartzJobBean {
    private final ProductService productService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Long productId = jobDataMap.getLong("product");
        Boolean startPayment = jobDataMap.getBoolean("startPayment");
        productService.handleNotification(productId, startPayment);
    }
}
