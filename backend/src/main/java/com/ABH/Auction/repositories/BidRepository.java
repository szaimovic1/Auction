package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Bid;
import com.ABH.Auction.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends PagingAndSortingRepository<Bid, Long> {

    @Query("SELECT COUNT(b) " +
            "FROM Bid b " +
            "WHERE b.product.id = ?1 " +
            "AND b.customer.email IS NOT NULL")
    int getNumberOfBids(Long id);

    @Query("SELECT b " +
            "FROM Bid b " +
            "WHERE b.bidTime = ( " +
             "SELECT MAX(a.bidTime) " +
              "FROM Bid a " +
              "WHERE a.product.id = ?1 " +
             ") " +
            "AND b.product.id = ?1 " +
            "AND b.customer.email IS NOT NULL")
    Optional<Bid> getHighestBid(Long id);

    @Query("SELECT b " +
            "FROM Bid b " +
            "WHERE b.bidTime = ( " +
            "SELECT MAX(a.bidTime) " +
            "FROM Bid a " +
            "WHERE a.product.id = ?1 AND a.customer.id = ?2 " +
            ") " +
            "AND b.product.id = ?1 AND b.customer.id = ?2")
    Optional<Bid> getHighestPersonalBid(Long product, Long user);

    @Query("SELECT b " +
            "FROM Bid b " +
            "WHERE b.product.id = ?1 " +
            "AND b.customer.email IS NOT NULL")
    List<Bid> getBidsForProduct(Long id, Pageable pageable);

    @Query("SELECT b " +
            "FROM Bid b " +
            "WHERE b.product.id = ?1 AND b.customer.id = ?2")
    Optional<Bid> checkBidByCustomer(Long id, Long customerId);

    @Transactional
    @Modifying
    @Query("UPDATE Bid b " +
            "SET b.value = ?2, b.bidTime = ?3 " +
            "WHERE b.id = ?1")
    void updateBid(Long id, Double value, LocalDateTime bidTime);

    //if auction ended and I'm not highest bidder, don't show the product
    @Query("SELECT b.product " +
            "FROM Bid b " +
            "WHERE b.customer.id = ?1 " +
            "AND b.product.seller.email IS NOT NULL")
    Page<Product> getBidsForUser(Long id, Pageable pageable);
}
