package com.ABH.Auction.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "address")
@ToString
public class Address {
    @Id
    @SequenceGenerator(
            name = "address_address_id_seq",
            sequenceName = "address_address_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_address_id_seq"
    )
    @Column(name = "address_id")
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private Integer zipCode;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    public Address(String street, String city, Integer zipCode, String state, String country) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.country = country;
    }

    public Address(String street, String city, Integer zipCode, String country) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }
}
