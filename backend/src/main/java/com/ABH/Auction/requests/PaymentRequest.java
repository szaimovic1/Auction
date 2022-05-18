package com.ABH.Auction.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PaymentRequest {
    private final Long productId;
    private final Double productPrice;
}
