package com.ABH.Auction.utility;

import com.ABH.Auction.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LocalStorageIImgsArg {
    private final List<String> images;
    private final Product product;
}
