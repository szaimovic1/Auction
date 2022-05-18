package com.ABH.Auction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PaymentResponse extends MessageResponse {
    private String clientSecret;

    public PaymentResponse(String message, Boolean isSuccess, String clientSecret) {
        super(message, isSuccess);
        this.clientSecret = clientSecret;
    }
}
