package com.ABH.Auction.services;

import com.ABH.Auction.domain.Token;
import com.ABH.Auction.domain.User;
import com.ABH.Auction.requests.RegistrationRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.utility.EmailValidator;
import com.ABH.Auction.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${frontend.path}")
    private String frontend;

    @Value("${backend.path}")
    private String backend;

    public MessageResponse register(RegistrationRequest request) {
        MessageResponse mr = checkRegistration(request);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        mr = userService.signUpUser(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if(!mr.getIsSuccess()) {
            return mr;
        }
        String link = backend + "/api/v1/registration/confirm?token=" + mr.getMessage();
        try {
            emailService.sendVerificationEmail(request.getEmail(), request.getFirstName(), link);
            return new MessageResponse("Go check your email!", true);
        }
        catch (Exception e) {
            tokenService.deleteToken(mr.getMessage());
            userService.deleteUser(request.getEmail());
            return new MessageResponse("Oops, something went wrong. Please try again!", false);
        }
    }

    public MessageResponse checkRegistration(RegistrationRequest request) {
        if (!emailValidator.test(request.getEmail())) {
            return new MessageResponse("Incorrect email form!", false);
        }
        else if(Validator.isNameInvalid(request.getFirstName())) {
            return new MessageResponse("Bad first name value!", false);
        }
        else if(Validator.isNameInvalid(request.getLastName())) {
            return new MessageResponse("Bad last name value!", false);
        }
        return Validator.checkPassword(request.getPassword());
    }

    @Transactional
    public MessageResponse confirmToken(String token) {
        Optional<Token> confToken = tokenService
                .getTokenByToken(token);
        if(confToken.isEmpty()) {
            return new MessageResponse("Registration not found!", false);
        }
        Token confirmationToken = confToken.get();
        if (confirmationToken.getConfirmationTime() != null) {
            return new MessageResponse("Email already confirmed!", false);
        }
        LocalDateTime expiredAt = confirmationToken.getExpirationTime();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return new MessageResponse("Request expired! Register again!", false);
        }
        tokenService.setConfirmationTime(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());
        userService.updateInitialLogin(confirmationToken.getUser().getEmail());
        //add token to link
        return new MessageResponse(frontend + "/login", true);
    }

    public MessageResponse forgotPass(String email) {
        Optional<User> potentialUser = userService.getUser(email);
        if(potentialUser.isEmpty() || potentialUser.get().getPassword() == null) {
            return new MessageResponse("Nonexistent user!", false);
        }
        User user = potentialUser.get();
        Optional<Token> existingToken = tokenService.getActiveTokenByUser(email);
        if(existingToken.isPresent()) {
            return new MessageResponse("Email already sent!", false);
        }
        MessageResponse mr = userService.createAndSaveToken(user);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        String link = backend + "/api/v1/registration/forgot?token=" + mr.getMessage();
        try {
            emailService.sendPassRecoveryEmail(user.getEmail(), user.getFirstName(), link);
            return new MessageResponse("Go check your email!", true);
        }
        catch (Exception e) {
            tokenService.deleteToken(mr.getMessage());
            return new MessageResponse("Oops, something went wrong. Please try again!", false);
        }
    }

    @Transactional
    public MessageResponse confirmForgot(String token) {
        Optional<Token> confToken = tokenService
                .getTokenByToken(token);
        if(confToken.isEmpty()) {
            return new MessageResponse("Password recovery not started!", false);
        }
        Token confirmationToken = confToken.get();
        if (confirmationToken.getConfirmationTime() != null) {
            return new MessageResponse("Password recovery already started!", false);
        }
        LocalDateTime expiredAt = confirmationToken.getExpirationTime();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return new MessageResponse("Request expired! Try again!", false);
        }
        tokenService.setConfirmationTime(token);
        return new MessageResponse(frontend + "/password/" + token, true);
    }

    @Transactional
    public MessageResponse newPass(String token, String pass) {
        Optional<Token> userTokenOptional = tokenService.getTokenByToken(token);
        if(userTokenOptional.isEmpty()) {
            return new MessageResponse("Password recovery not started!", false);
        }
        Token userToken = userTokenOptional.get();
        if (userToken.getConfirmationTime() == null) {
            return new MessageResponse("Password recovery not confirmed!", false);
        }
        if (userToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return new MessageResponse("Request expired! Try again!", false);
        }
        MessageResponse mr = Validator.checkPassword(pass);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        String encodedPassword = bCryptPasswordEncoder.encode(pass);
        userToken.getUser().setPassword(encodedPassword);
        return new MessageResponse("Password changed", true);
    }
}
