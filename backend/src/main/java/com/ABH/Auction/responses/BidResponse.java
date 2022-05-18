package com.ABH.Auction.responses;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BidResponse {
    private final UserResponse bidder;
    private final Double value;
    private final LocalDateTime bidTime;
}
