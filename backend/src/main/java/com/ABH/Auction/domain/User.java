package com.ABH.Auction.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "app_user")
@ToString
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "app_user_user_id_seq",
            sequenceName = "app_user_user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_user_id_seq"
    )
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name", nullable = false, length=20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length=20)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "initial_login")
    private LocalDateTime initialLogin;

    @Column(unique = true)
    private String email;

    @Column(name = "reactivation_email")
    private String reactivationEmail;

    private String password;
    private String image;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private PhoneNumber phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_assignment",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CreditCard creditCard;

    @Column(name = "is_enabled")
    private Boolean isEnabled=false;

    public User(String firstName,
                String lastName,
                String email,
                String password) {
        this(firstName, lastName, email);
        this.password = password;
    }

    public User(String firstName,
                String lastName,
                String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole().name())).collect(Collectors.toCollection(HashSet::new));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFullName() {return firstName + " " + lastName;}
}
