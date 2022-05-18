package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
