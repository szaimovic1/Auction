package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Image;
import com.ABH.Auction.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i " +
            "FROM Image i " +
            "WHERE i.product = ?1")
    List<Image> findByProduct(Product p);
}
