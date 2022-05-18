package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategory(Category category);

    @Query("SELECT CONCAT(COALESCE(( " +
             "SELECT s.name " +
             "FROM Category s " +
             "WHERE s.id = c.category.id " +
            ") || '/', ''), c.name) " +
            "FROM Category c " +
            "WHERE c.id = ?1")
    String getCategoryName(Long category);

    @Query("SELECT c " +
            "FROM Category c " +
            "WHERE c.id = ?1")
    Optional<Category> getCategoryById(Long category);
}
