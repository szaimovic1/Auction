package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LoginActivityResponse extends MessageResponse {
    private Long lastWeek;
    private Long lastMonth;
    private Long beforeLastMonth;
    private Long justOnce;
}
