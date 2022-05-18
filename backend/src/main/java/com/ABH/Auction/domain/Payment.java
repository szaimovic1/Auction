package com.ABH.Auction.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "payment")
@ToString
public class Payment {
    @Id
    @SequenceGenerator(
            name = "payment_payment_id_seq",
            sequenceName = "payment_payment_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_payment_id_seq"
    )
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_intent_id")
    private String payment;

    @Column(name = "payment_intent_client_secret")
    private String clientSecret;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "payer_id"
    )
    private User payer;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "product_id"
    )
    private Product product;

    @Column(nullable = false)
    private Double price;

    @Column(name = "is_finalized")
    private boolean isFinalized;

    public Payment(String payment, String clientSecret, User payer, Product product, Double price, boolean isFinalized) {
        this.payment = payment;
        this.clientSecret = clientSecret;
        this.payer = payer;
        this.product = product;
        this.price = price;
        this.isFinalized = isFinalized;
    }
}
