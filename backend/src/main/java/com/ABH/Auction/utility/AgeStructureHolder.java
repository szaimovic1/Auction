package com.ABH.Auction.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AgeStructureHolder {
    private Long teen;
    private Long adult;
    private Long middleAge;
    private Long senior;
    private Long unfilled;
    private Long numberOfUsers;
}
