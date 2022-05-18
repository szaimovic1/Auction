package com.ABH.Auction.services;

import com.ABH.Auction.domain.Color;
import com.ABH.Auction.repositories.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public List<Color> getColors() {
        return colorRepository.findAll();
    }
}
