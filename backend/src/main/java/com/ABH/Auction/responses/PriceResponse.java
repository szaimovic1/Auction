package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PriceResponse {
    private final Double minPrice;
    private final Double maxPrice;
}
