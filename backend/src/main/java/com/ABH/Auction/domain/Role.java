package com.ABH.Auction.domain;

import com.ABH.Auction.domain.enums.UserRole;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role{
    @Id
    @SequenceGenerator(
            name = "role_role_id_seq",
            sequenceName = "role_role_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_role_id_seq"
    )
    @Column(name = "role_id")
    protected Long id;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public Role(UserRole role) {
        this.role = role;
    }

    public Role(Long id, UserRole role) {
        this.id = id;
        this.role = role;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(id, role1.id) &&
                role == role1.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
