package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductsResponse extends MessageResponse {
    private List<ProductResponse> products = new ArrayList<>();
    private Long numberOfProducts = null;
    private Double startPrice = null;
    private Double endPrice = null;

    public ProductsResponse() {
    }
}
