package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment {
    @Id
    @SequenceGenerator(
            name = "shipment_shipment_id_seq",
            sequenceName = "shipment_shipment_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shipment_shipment_id_seq"
    )
    @Column(name = "shipment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private PhoneNumber phoneNumber;

    @Column
    private String email;

    public Shipment(Address address, PhoneNumber phoneNumber, String email) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
