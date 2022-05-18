package com.ABH.Auction.services;

import com.ABH.Auction.domain.Size;
import com.ABH.Auction.repositories.ProductRepository;
import com.ABH.Auction.repositories.SizeRepository;
import com.ABH.Auction.responses.SizeResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SizeService {

    private  final SizeRepository sizeRepository;
    private final ProductRepository productRepository;

    public List<SizeResponse> getSizes() {
        List<Size> sizes = sizeRepository.findAll();
        return sizes.stream().map(s -> new SizeResponse(s.getSize(),
                productRepository.getProductsForSize(s.getId(), LocalDateTime.now()),
                s.getId())).collect(Collectors.toList());
    }
}
