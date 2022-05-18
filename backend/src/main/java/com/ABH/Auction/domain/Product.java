package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_product_id_seq",
            sequenceName = "product_product_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_product_id_seq"
    )
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "seller_id"
    )
    private User seller;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "category_id"
    )
    private Category category;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Column(nullable = false, length=20)
    private String name;

    @Column
    private String details;

    @Column(name="initial_price", nullable = false)
    private Double price;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "size_assignment",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id")
    )
    private Collection<Size> sizes = new HashSet<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "color_assignment",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private Collection<Color> colors = new HashSet<>();

    public Product(User seller,
               Category category,
               String name,
               String details,
               Double price,
               LocalDateTime startDate,
               LocalDateTime endDate) {
        this(seller, category, name, price, startDate, endDate);
        this.details = details;
    }

    public Product(User seller,
                   Category category,
                   String name,
                   Double price,
                   LocalDateTime startDate,
                   LocalDateTime endDate) {
        this.seller = seller;
        this.category = category;
        this.name = name;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
