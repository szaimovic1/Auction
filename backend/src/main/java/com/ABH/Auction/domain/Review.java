package com.ABH.Auction.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "review")
@ToString
public class Review {
    @Id
    @SequenceGenerator(
            name = "review_review_id_seq",
            sequenceName = "review_review_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_review_id_seq"
    )
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "customer")
    private User customer;

    @Column
    @Min(1)
    @Max(5)
    private Integer rating;

    public Review(User seller, User customer, @Min(1) @Max(5) Integer rating) {
        this.seller = seller;
        this.customer = customer;
        this.rating = rating;
    }
}
