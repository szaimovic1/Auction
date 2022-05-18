package com.ABH.Auction.services;

import com.ABH.Auction.domain.Token;
import com.ABH.Auction.repositories.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Optional<Token> getTokenByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Optional<Token> getTokenByUser(String userMail) {
        return tokenRepository.findByUser(userMail);
    }

    public void setConfirmationTime(String token) {
        tokenRepository.updateConfirmationTime(token, LocalDateTime.now());
    }

    public void setToken(LocalDateTime start, LocalDateTime end, String newToken, String token) {
        tokenRepository.updateToken(start, end, newToken, token);
    }

    public Optional<Token> getActiveTokenByUser(String userMail) {
        return tokenRepository.getActiveToken(userMail, LocalDateTime.now());
    }

    public void deleteToken(String token) {
        tokenRepository.delete(getTokenByToken(token).get());
    }
}
