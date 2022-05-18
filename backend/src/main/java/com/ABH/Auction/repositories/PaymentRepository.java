package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Payment;
import com.ABH.Auction.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p " +
            "FROM Payment p " +
            "WHERE p.product = ?1")
    Optional<Payment> findByProduct(Product product);

    @Query("SELECT p " +
            "FROM Payment p " +
            "WHERE p.payment = ?1")
    Optional<Payment> findByPaymentIntentId(String id);
}
