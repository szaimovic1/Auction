package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @SequenceGenerator(
            name = "bid_bid_id_seq",
            sequenceName = "bid_bid_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bid_bid_id_seq"
    )
    @Column(name = "bid_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "product_id"
    )
    private Product product;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "customer_id"
    )
    private User customer;

    @Column(nullable = false)
    private Double value;

    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime;

    public Bid(Product product,
               User customer,
               Double value,
               LocalDateTime bidTime) {
        this.product = product;
        this.customer = customer;
        this.value = value;
        this.bidTime = bidTime;
    }
}
