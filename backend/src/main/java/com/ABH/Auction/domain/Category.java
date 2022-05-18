package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @SequenceGenerator(
            name = "category_category_id_seq",
            sequenceName = "category_category_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_category_id_seq"
    )
    @Column(name = "category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "subcategory_id"
    )
    private Category category;

    @Column(nullable = false, length=20)
    private String name;

    public Category(Category category,
                   String name) {
        this.category = category;
        this.name = name;
    }
}
