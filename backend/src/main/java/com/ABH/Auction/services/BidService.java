package com.ABH.Auction.services;

import com.ABH.Auction.domain.Bid;
import com.ABH.Auction.domain.Product;
import com.ABH.Auction.repositories.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;

    public Bid getHighestBid(Product product) {
        Optional<Bid> optionalBid = bidRepository.getHighestBid(product.getId());
        return optionalBid.orElse(null);
    }
}
