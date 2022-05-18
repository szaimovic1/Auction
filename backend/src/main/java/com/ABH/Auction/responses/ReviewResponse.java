package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ReviewResponse {
    private float fiveStars = 0;
    private float fourStars = 0;
    private float threeStars = 0;
    private float twoStars = 0;
    private float oneStar = 0;
    private long numberOfVotes = 0;
    private float rating = 0;

    public ReviewResponse() {
    }
}
