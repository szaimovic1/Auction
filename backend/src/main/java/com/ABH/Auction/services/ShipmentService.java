package com.ABH.Auction.services;

import com.ABH.Auction.domain.Shipment;
import com.ABH.Auction.repositories.ShipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public Shipment addShipment(Shipment s) {
        return shipmentRepository.save(s);
    }
}
