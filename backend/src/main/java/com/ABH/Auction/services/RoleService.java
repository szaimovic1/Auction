package com.ABH.Auction.services;

import com.ABH.Auction.domain.Role;
import com.ABH.Auction.domain.enums.UserRole;
import com.ABH.Auction.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public void saveRole(Role role) {
        roleRepository.save(role);
    }
    public Optional<Role> getRole(UserRole role) {
        return roleRepository.findByRole(role);
    }
}
