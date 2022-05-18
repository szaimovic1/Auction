package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "color")
public class Color {
    @Id
    @SequenceGenerator(
            name = "color_color_id_seq",
            sequenceName = "color_color_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "color_color_id_seq"
    )
    @Column(name = "color_id")
    protected Long id;

    private String color;
}
