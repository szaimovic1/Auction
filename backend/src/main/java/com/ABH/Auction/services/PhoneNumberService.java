package com.ABH.Auction.services;

import com.ABH.Auction.domain.PhoneNumber;
import com.ABH.Auction.repositories.PhoneNumberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumber addPhoneNumber(PhoneNumber number) {
        return phoneNumberRepository.save(number);
    }
}
