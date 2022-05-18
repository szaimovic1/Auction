package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AgeStructureResponse extends MessageResponse {
    private float teen;
    private float adult;
    private float middleAge;
    private float senior;
    private float unfilled;
}
