package com.ABH.Auction.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductBidResponse extends MessageResponse {
    private final List<ProductBidResponses> products;
    private final Long totalNumber;
}
