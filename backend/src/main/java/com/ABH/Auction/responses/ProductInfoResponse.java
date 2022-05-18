package com.ABH.Auction.responses;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductInfoResponse extends MessageResponse {
    private ProductResponse product = null;
    private UserResponse seller = null;
    private int numberOfBids = 0;
    private Double highestBid = null;
    private List<ProductResponse> relatedProducts = null;
    private Boolean bidders = true;
    private List<String> images = null;
    private Long categoryId = null;
    private Boolean isPaidFor = false;

    public ProductInfoResponse() {
    }
}
