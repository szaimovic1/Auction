package com.ABH.Auction.responses;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class CategoryResponse {
    private final String name;
    private final Integer products;
    private final List<CategoryResponse> subcategories;
    private final Long id;
}
