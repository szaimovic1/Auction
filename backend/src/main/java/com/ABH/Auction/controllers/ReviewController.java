package com.ABH.Auction.controllers;

import com.ABH.Auction.requests.ReviewRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/review")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(tags = "review", description = "Access the review you left for a particular seller!")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping()
    public Integer getUserReview(@RequestHeader("Authorization") String token,
                                              @RequestParam("seller_id") Long id) {
        return reviewService.getUserRating(token, id);
    }

    @Operation(tags = "review", description = "Rate a user!")
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping()
    public MessageResponse rateUser(@RequestBody ReviewRequest request,
                                     @RequestHeader("Authorization") String token) {
        return reviewService.rateUser(token, request);
    }
}
