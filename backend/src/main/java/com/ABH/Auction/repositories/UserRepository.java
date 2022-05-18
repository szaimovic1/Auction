package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Product;
import com.ABH.Auction.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isEnabled = TRUE WHERE a.email = ?1")
    void enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.initialLogin = ?2 WHERE a.email = ?1")
    void updateInitialLogin(String email, LocalDateTime now);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.loginTime = ?2 WHERE a.email = ?1")
    void updateLoginTime(String email, LocalDateTime now);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE ((:search) IS NULL " +
              "OR lower(u.firstName || u.lastName) LIKE :search " +
              "OR u.email LIKE :search)")
    Page<User> getUsers(@Param("search") String search, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.seller.id = ?1")
    List<Product> getProductsForUser(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM wishlist_assignment wa " +
            "WHERE wa.wishlist_id = ?1", nativeQuery = true)
    void deleteWishlistAsgForUser(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM role_assignment ra " +
            "WHERE ra.user_id = ?1", nativeQuery = true)
    void deleteRoleAsgForUser(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p " +
            "WHERE p.id = ?1")
    void deleteProductForUser(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t " +
            "WHERE t.user.id = ?1")
    void deleteTokensForUser(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p " +
            "WHERE p.payer.id = ?1")
    void deletePaymentsForUser(Long id);

    //does not work if it's not native questionmark :(
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM review r " +
            "WHERE r.seller_id = ?1 OR r.customer_id = ?1", nativeQuery = true)
    void deleteReviewsForUser(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bid b " +
            "WHERE b.customer.id = ?1")
    void deleteUserBids(Long id);

    @Query(value = "SELECT COUNT(u.login_time) filter (WHERE u.login_time >= ?1) as LastWeek, " +
            "COUNT(u.login_time) filter (WHERE u.login_time >= ?2) as LastMonth, " +
            "COUNT(u.login_time) filter (WHERE u.login_time < ?2 AND u.login_time != u.initial_login) as MoreThanOnce, " +
            "COUNT(u.login_time) filter (WHERE u.initial_login = u.login_time) as JustOnce " +
            "FROM app_user u", nativeQuery = true)
    List<Object[]> getUsersLoginActivity(LocalDateTime lastWeek, LocalDateTime lastMont);

    @Query(value = "SELECT COUNT(u) filter (" +
             "WHERE EXTRACT(years FROM AGE(?1, u.date_of_birth)) BETWEEN 18 AND 19" +
            ") as Teen, " +
            "COUNT(u) filter (" +
             "WHERE EXTRACT(years FROM AGE(?1, u.date_of_birth)) BETWEEN 20 AND 39" +
            ") as Adult, " +
            "COUNT(u) filter (" +
             "WHERE EXTRACT(years FROM AGE(?1, u.date_of_birth)) BETWEEN 40 AND 59" +
            ") as MiddleAge, " +
            "COUNT(u) filter (" +
             "WHERE EXTRACT(years FROM AGE(?1, u.date_of_birth)) >= 60" +
            ") as Senior, " +
            "COUNT(u) filter (WHERE u.date_of_birth IS NULL) as Unfilled, " +
            "COUNT(u) as All " +
            "FROM app_user u", nativeQuery = true)
    List<Object[]> getUsersAgeStructure(LocalDateTime now);
}
