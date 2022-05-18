package com.ABH.Auction.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserInfoUpdateRequest {
    private final String name;
    private final String surname;
    private final String email;
    private final String phone;
    private final LocalDateTime dateOfBirth;
    private final String nameCard;
    private final String numberCard;
    private final Integer monthExpCard;
    private final Integer yearExpCard;
    private final String cvcCvv;
    private final String street;
    private final String city;
    private final String zipCode;
    private final String state;
    private final String country;
}
