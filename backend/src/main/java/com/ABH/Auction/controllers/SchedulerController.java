package com.ABH.Auction.controllers;

import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.services.SchedulerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/scheduler")
@AllArgsConstructor
public class SchedulerController {
    private final SchedulerService schedulerService;

    @PostMapping(path = "/schedule-email")
    public MessageResponse scheduleEmail(@RequestHeader("Authorization") String token,
                                                     @RequestParam("productId") Long id) {
        return schedulerService.scheduleEmail(token, id);

    }
}
