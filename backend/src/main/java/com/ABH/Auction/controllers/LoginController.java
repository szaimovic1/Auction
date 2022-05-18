package com.ABH.Auction.controllers;

import com.ABH.Auction.requests.LoginRequest;
import com.ABH.Auction.responses.LoginResponse;
import com.ABH.Auction.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Login",
            description = "Send email and password data to access the application", tags = "login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "If isSuccess is true, it means login process " +
                    "is done and requested data will be returned. If it is false, message in response body " +
                    "will explain what is wrong with sent data and why are requested info null.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginService.login(request.getEmail(), request.getPassword());
    }
}
