package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Payment;
import com.ABH.Auction.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.category.id = ?1 OR p.category.category.id = ?1 " +
            "AND p.endDate >= ?2 " +
            "AND p.startDate <= ?2 " +
            "AND p.seller.email IS NOT NULL")
    List<Product> getProductsByCategoryId(Long category, LocalDateTime now, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE ((:categories) IS NULL OR p.category.id IN :categories " +
             "OR p.category.category.id IN (:categories)) " +
            "AND p.endDate >= :now " +
            "AND p.startDate <= :now " +
            "AND ((:sizes) IS NULL OR p.id IN ( " +
             "SELECT p.id " +
             "FROM p.sizes s " +
             "WHERE s.size IN :sizes " +
            ")) " +
            "AND ((:colors) IS NULL OR p.id IN ( " +
             "SELECT p.id " +
             "FROM p.colors c " +
             "WHERE c.color IN :colors " +
            ")) " +
            "AND ((:startPrice = 0.0) OR p.price >= :startPrice) " +
            "AND ((:endPrice = 0.0) OR p.price <= :endPrice) " +
            "AND ((:search) IS NULL OR lower(p.name) LIKE :search) " +
            "AND p.seller.email IS NOT NULL")
    Page<Product> getProducts(@Param("categories") List<Long> categories,
                              @Param("now") LocalDateTime now,
                              @Param("sizes") List<String> sizes,
                              @Param("colors") List<String> colors,
                              @Param("startPrice") Double startPrice,
                              @Param("endPrice") Double endPrice,
                              @Param("search") String search,
                              Pageable pageable);

    @Query("SELECT MIN(p.price), MAX(p.price) " +
            "FROM Product p " +
            "WHERE ((:categories) IS NULL OR p.category.id IN :categories " +
            "OR p.category.category.id IN (:categories)) " +
            "AND p.endDate >= :now " +
            "AND p.startDate <= :now " +
            "AND ((:sizes) IS NULL OR p.id IN ( " +
            "SELECT p.id " +
            "FROM p.sizes s " +
            "WHERE s.size IN :sizes " +
            ")) " +
            "AND ((:colors) IS NULL OR p.id IN ( " +
            "SELECT p.id " +
            "FROM p.colors c " +
            "WHERE c.color IN :colors " +
            ")) " +
            "AND ((:search) IS NULL OR lower(p.name) LIKE :search) " +
            "AND p.seller.email IS NOT NULL")
    List<Object[]> getCurrentPriceRange(@Param("categories") List<Long> categories,
                                             @Param("now") LocalDateTime now,
                                             @Param("sizes") List<String> sizes,
                                             @Param("colors") List<String> colors,
                                             @Param("search") String search);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.endDate >= ?1 " +
            "AND p.startDate <= ?1 " +
            "AND p.seller.email IS NOT NULL")
    List<Product> getHighlightedProduct(LocalDateTime now, Pageable pageable);

    @Query("SELECT COUNT(p) " +
            "FROM Product p " +
            "WHERE p.id = ( " +
             "SELECT p.id " +
             "FROM p.sizes s " +
             "WHERE s.id = ?1 " +
            ") " +
            "AND p.endDate >= ?2 " +
            "AND p.startDate <= ?2 " +
            "AND p.seller.email IS NOT NULL")
    Integer getProductsForSize(Long id, LocalDateTime now);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE ((:active) = true OR p.endDate <= (:now)) " +
            "AND ((:active) = false OR p.endDate >= (:now)) " +
            "AND p.seller.id = :id")
    Page<Product> getProductsForUser(@Param("id") Long id,
                                     @Param("active") Boolean active,
                                     @Param("now") LocalDateTime now,
                                     Pageable pageable);

    @Query("SELECT DISTINCT p.category.id " +
            "FROM Product p " +
            "WHERE p.seller.id = :id " +
            "AND EXISTS " +
             "(SELECT b.product.category.id " +
             "FROM Bid b " +
             "WHERE b.customer.id = :id " +
             "AND b.product.category.id = p.category.id" +
            ")")
    List<Long> getRecommendedProdsCategoriesIntersect(@Param("id") Long id);

    @Query("SELECT DISTINCT p.category.id " +
            "FROM Product p " +
            "WHERE p.seller.id = :id")
    List<Long> getRecommendedProdsCategoriesProds(@Param("id") Long id);

    @Query("SELECT DISTINCT b.product.category.id " +
            "FROM Bid b " +
            "WHERE b.customer.id = :id")
    List<Long> getRecommendedProdsCategoriesBids(@Param("id") Long id);

    @Query("SELECT MIN(p.price), MAX(p.price) " +
            "FROM Product p " +
            "WHERE p.endDate >= :now " +
            "AND p.startDate <= :now " +
            "AND p.seller.id = :id")
    List<Object[]> getRecommendedPriceRangeProds(@Param("id") Long id,
                                                 @Param("now") LocalDateTime now);

    @Query("SELECT MIN(b.product.price), MAX(b.product.price) " +
            "FROM Bid b " +
            "WHERE b.product.endDate >= :now " +
            "AND b.product.startDate <= :now " +
            "AND b.customer.id = :id")
    List<Object[]> getRecommendedPriceRangeBids(@Param("id") Long id,
                                                @Param("now") LocalDateTime now);

    @Query("SELECT p.name " +
            "FROM Product p " +
            "WHERE p.endDate >= :now " +
            "AND p.startDate <= :now " +
            "AND p.seller.email IS NOT NULL")
    List<String> getProductsNames(@Param("now") LocalDateTime now);

    @Query("SELECT p.product.id " +
            "FROM Payment p " +
            "WHERE p.payment = ?1")
    Long getProductIdByPaymentIntent(String id);

    @Query("SELECT p " +
            "FROM Payment p " +
            "WHERE p.product = ?1")
    Optional<Payment> getPaymentForProduct(Product product);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM color_assignment ca " +
            "WHERE ca.product_id = ?1", nativeQuery = true)
    void deleteColorAsgForProduct(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM size_assignment sa " +
            "WHERE sa.product_id = ?1", nativeQuery = true)
    void deleteSizeAsgForProduct(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM wishlist_assignment wa " +
            "WHERE wa.product_id = ?1", nativeQuery = true)
    void deleteWishlistAsgForProduct(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i " +
            "WHERE i.product.id = ?1")
    void deleteImagesForProduct(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bid b " +
            "WHERE b.product.id = ?1")
    void deleteProductBids(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p " +
            "WHERE p.product.id = ?1")
    void deletePaymentsForProduct(Long id);
}
