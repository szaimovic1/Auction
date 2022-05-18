package com.ABH.Auction.domain;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @SequenceGenerator(
            name = "image_image_id_seq",
            sequenceName = "image_image_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_image_id_seq"
    )
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "product_id"
    )
    private Product product;

    @Column(name = "image_url", nullable = false)
    private String image;

    public Image(Product product,
                String image) {
        this.product = product;
        this.image = image;
    }
}
