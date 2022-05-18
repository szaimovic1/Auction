package com.ABH.Auction.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    @Column(name = "token_id")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "confirmation_time")
    private LocalDateTime confirmationTime;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    public Token(String token,
                 LocalDateTime creationTime,
                 LocalDateTime expirationTime,
                 User user) {
        this.token = token;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.user = user;
    }
}
