package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductBidResponses {
    private final ProductResponse product;
    private final int numberOfBids;
    private final Double highestBid;
    private final Boolean isHighestBidMine;
    private final Double highestPersonalBid;
}
