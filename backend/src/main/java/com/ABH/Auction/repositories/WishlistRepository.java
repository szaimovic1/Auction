package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Product;
import com.ABH.Auction.domain.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends PagingAndSortingRepository<Wishlist, Long> {
    @Query("SELECT true " +
            "FROM Wishlist w " +
            "WHERE ?1 = ( " +
             "SELECT p.id " +
             "FROM w.products p " +
             "WHERE p.id = ?1 " +
            ") " +
            "AND w.user.id = ?2")
    Boolean isProductOnWishlist(Long productId, Long userId);

    @Query("SELECT w " +
            "FROM Wishlist w " +
            "WHERE w.user.id = ?1")
    Optional<Wishlist> getUserWishlist(Long userId);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.id = ?1 " +
            "AND p.seller.email IS NOT NULL")
    Optional<Product> getProductForWishlist(Long productId);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.id = " +
            "(SELECT p.id " +
              "FROM Wishlist w " +
              "WHERE p.id = ( " +
                "SELECT wp.id " +
                "FROM w.products wp " +
                "WHERE wp.id = p.id " +
              ") " +
              "AND w.user.id = ?1 " +
            ") " +
            "AND p.seller.email IS NOT NULL " +
            "AND p.endDate >= ?2 " +
            "AND p.startDate <= ?2")
    Page<Product> getWishlistProducts(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT u.email " +
            "FROM User u " +
            "WHERE u.id = " +
            "(SELECT u.id " +
              "FROM Wishlist w " +
              "WHERE ?1 = ( " +
              "SELECT wp.id " +
                "FROM w.products wp " +
                "WHERE wp.id = ?1 " +
              ") " +
              "AND w.user.id = u.id " +
            ") " +
            "AND u.email IS NOT NULL")
    List<String> getWishlistUserContainingProduct(Long productId);
}
