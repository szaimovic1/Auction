package com.ABH.Auction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SizeResponse {
    private final String size;
    private final Integer products;
    private final Long id;
}
