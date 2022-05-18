package com.ABH.Auction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SearchResponse extends MessageResponse {
    private final String name;
    private final Long id;
}
