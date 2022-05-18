package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query("SELECT t " +
            "FROM Token t, User u " +
            "WHERE t.user = u.id AND u.email = ?1")
    Optional<Token> findByUser(String userMail);

    @Transactional
    @Modifying
    @Query("UPDATE Token c " +
            "SET c.confirmationTime = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmationTime(String token, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query("UPDATE Token c " +
            "SET c.creationTime = ?1, c.expirationTime = ?2, c.token = ?3 " +
            "WHERE c.token = ?4")
    void updateToken(LocalDateTime creationTime, LocalDateTime expirationTime, String newToken, String token);

    @Query("SELECT t " +
            "FROM Token t, User u " +
            "WHERE t.user = u.id " +
            "AND u.email = ?1 " +
            "AND t.expirationTime > ?2")
    Optional<Token> getActiveToken(String userMail, LocalDateTime now);
}
