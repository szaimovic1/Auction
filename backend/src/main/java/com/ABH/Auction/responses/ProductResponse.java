package com.ABH.Auction.responses;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductResponse extends MessageResponse {
    private String name = null;
    private String image = null;
    private Long id = null;
    private Double price = null;
    private String description = null;
    private LocalDateTime endDate = null;
    private Boolean isOnWishlist = null;

    public ProductResponse(String message, Boolean isSuccess) {
        super(message, isSuccess);
    }
}
