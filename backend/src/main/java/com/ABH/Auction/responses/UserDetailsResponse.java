package com.ABH.Auction.responses;

import com.ABH.Auction.domain.Address;
import com.ABH.Auction.domain.CreditCard;
import com.ABH.Auction.domain.PhoneNumber;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDetailsResponse extends MessageResponse {
    private final UserResponse user;
    private final LocalDateTime dateOfBirth;
    private final PhoneNumber phoneNumber;
    private final CreditCard card;
    private final Address address;
}
