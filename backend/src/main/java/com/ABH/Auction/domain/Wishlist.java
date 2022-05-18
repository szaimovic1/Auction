package com.ABH.Auction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @SequenceGenerator(
            name = "wishlist_wishlist_id_seq",
            sequenceName = "wishlist_wishlist_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wishlist_wishlist_id_seq"
    )
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "wishlist_assignment",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Collection<Product> products = new HashSet<>();

    public Wishlist(User user) {
        this.user = user;
    }
}
