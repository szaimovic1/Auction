package com.ABH.Auction.services;

import com.ABH.Auction.domain.User;
import com.ABH.Auction.responses.LoginResponse;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.utility.EmailValidator;
import com.ABH.Auction.utility.JWTUtility;
import com.ABH.Auction.utility.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final JWTUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EmailValidator emailValidator;

    public LoginResponse login(String email, String password) {
        LoginResponse response = new LoginResponse(null, null);
        MessageResponse mr = checkCredentials(email, password);
        if(!mr.getIsSuccess()) {
            response.setMessage(mr.getMessage());
            response.setIsSuccess(false);
            return response;
        }
        //what to do with blocked user's data?
        //check for deactivation..
        User user = userService.getUser(email).get();
        if(user.getIsBlocked()) {
            response.setMessage("User is blocked!");
            response.setIsSuccess(false);
            return response;
        }
        final UserDetails userDetails = userService.loadUserByUsername(email);
        final String token = jwtUtility.generateToken(userDetails);
        response.setFullName(userService.getUser(email).get().getFullName());
        response.setJwt(token);
        userService.updateLoginTime(email);
        return response;
    }

    public MessageResponse checkCredentials(String email, String password) {
        if(!emailValidator.test(email)) {
            return new MessageResponse("Email not valid!", false);
        }
        MessageResponse mr = Validator.checkPassword(password);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );
        }
        catch (BadCredentialsException m) {
            return new MessageResponse("Invalid credentials!", false);
        }
        return new MessageResponse();
    }
}
