package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "size")
public class Size {
    @Id
    @SequenceGenerator(
            name = "size_size_id_seq",
            sequenceName = "size_size_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "size_size_id_seq"
    )
    @Column(name = "size_id")
    protected Long id;

    private String size;
}
