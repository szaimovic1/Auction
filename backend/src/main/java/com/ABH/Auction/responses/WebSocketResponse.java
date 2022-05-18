package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WebSocketResponse {
    private String message;
    private Boolean startPayment;
    private final Long productId;
    private String image;

    public WebSocketResponse(Long productId, String image) {
        this.productId = productId;
        this.image = image;
        message = null;
        startPayment = false;
    }
}
