package com.ABH.Auction.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductRequest {
    private final String name;
    private final Long categoryId;
    private final String description;
    private final Double price;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String street;
    private final String city;
    private final Integer zipCode;
    private final String country;
    private final String email;
    private final String phone;

    public ProductRequest() {
        name = null;
        categoryId = null;
        description = null;
        price = null;
        startDate = null;
        endDate = null;
        street = null;
        city = null;
        zipCode = null;
        country = null;
        email = null;
        phone = null;
    }
}
