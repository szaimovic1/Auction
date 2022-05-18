package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "phone_number")
public class PhoneNumber {
    @Id
    @SequenceGenerator(
            name = "phone_number_phone_id_seq",
            sequenceName = "phone_number_phone_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "phone_number_phone_id_seq"
    )
    @Column(name = "phone_id")
    private Long id;

    @Column
    private String number;

    @Column(name = "is_valid")
    private Boolean isValid = false;

    public PhoneNumber(String number, Boolean isValid) {
        this.number = number;
        this.isValid = isValid;
    }
}
