package com.ABH.Auction.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "credit_card")
@ToString
public class CreditCard {
    @Id
    @SequenceGenerator(
            name = "card_sequence",
            sequenceName = "card_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "card_sequence"
    )
    @Column(name = "card_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String number;

    @Column(name = "expiration_month")
    private Integer expirationMonth;

    @Column(name = "expiration_year")
    private Integer expirationYear;

    @Column(name = "cvc_cvv")
    private Integer cvcCvv;

    public CreditCard(String name, String number, Integer expirationMonth, Integer expirationYear, Integer cvcCvv) {
        this.name = name;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cvcCvv = cvcCvv;
    }
}
