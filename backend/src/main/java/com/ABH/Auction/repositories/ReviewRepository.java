package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT COUNT(r.rating) " +
            "FROM Review r " +
            "WHERE r.seller.id = :user " +
            "AND ((:stars) IS NULL OR r.rating = (:stars)) " +
            "AND r.seller.email IS NOT NULL")
    long getStars(@Param("user") Long user,
                  @Param("stars") Integer stars);

    @Query("SELECT AVG(r.rating) " +
            "FROM Review r " +
            "WHERE r.seller.id = ?1 " +
            "AND r.seller.email IS NOT NULL")
    Float getRating(Long userId);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.seller.id = ?2 " +
            "AND r.customer.id = ?1 " +
            "AND r.seller.email IS NOT NULL")
    Optional<Review> getSingularRating(Long userId, Long sellerId);
}
