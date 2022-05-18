package com.ABH.Auction.controllers;

import com.ABH.Auction.requests.NewPasswordRequest;
import com.ABH.Auction.requests.RegistrationRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.services.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "Register",
            description = "Send basic data for creating account so that you " +
                    "can start using full range of app's services.", tags = "registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmation email sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping
    public MessageResponse register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }


    @Operation(summary = "Confirm registration request",
            description = "Send token you received via email to confirm your " +
                    "registration request.", tags = "registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redirect to login",
                    content = @Content)
    })
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "confirm")
    public ResponseEntity<MessageResponse> confirm(@RequestParam("token") String token) {
        MessageResponse msg = registrationService.confirmToken(token);
        if(msg.getIsSuccess()) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(msg.getMessage())).build();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }


    @Operation(summary = "Forgot password",
            description = "Send email so that we can direct you how to get a new password.",
            tags = "registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/forgot")
    public MessageResponse forgotPass(@RequestParam("email") String email) {
        return registrationService.forgotPass(email);
    }


    @Operation(summary = "Confirm new password request",
            description = "Send token you received via email to confirm your " +
                    "password renewal request.", tags = "registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redirect to a page where " +
                    "you will create a new password",
                    content = @Content)
    })
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "forgot")
    public ResponseEntity<MessageResponse> confirmForgot(@RequestParam("token") String token) {
        MessageResponse msg = registrationService.confirmForgot(token);
        if(msg.getIsSuccess()) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(msg.getMessage())).build();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }


    @Operation(summary = "Send new password",
            description = "Send new password with confirmed token to finish " +
                    "password recovery process.", tags = "registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message about results " +
                    "of updating password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/new-password")
    public MessageResponse newPass(@RequestBody NewPasswordRequest request) {
        return registrationService.newPass(request.getToken(), request.getPassword());
    }
}
