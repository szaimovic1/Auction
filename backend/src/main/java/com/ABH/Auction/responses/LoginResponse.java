package com.ABH.Auction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LoginResponse extends MessageResponse {
    private String jwt;
    private String fullName;
}
