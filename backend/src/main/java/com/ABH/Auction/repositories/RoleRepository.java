package com.ABH.Auction.repositories;

import com.ABH.Auction.domain.Role;
import com.ABH.Auction.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(UserRole role);
}
