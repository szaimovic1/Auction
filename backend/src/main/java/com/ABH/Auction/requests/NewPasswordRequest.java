package com.ABH.Auction.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NewPasswordRequest {
    private final String token;
    private final String password;
}
