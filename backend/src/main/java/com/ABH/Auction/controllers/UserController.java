package com.ABH.Auction.controllers;

import com.ABH.Auction.domain.Shipment;
import com.ABH.Auction.requests.UserInfoUpdateRequest;
import com.ABH.Auction.responses.*;
import com.ABH.Auction.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Upload profile image",
            description = "If you have an account, you will be able to change your profile photo " +
                    "when logged in.", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded or message " +
                    "about occurred error returned.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping("/update-profile-photo")
    public MessageResponse updateProfilePhoto(@RequestHeader("Authorization") String token,
                            @RequestParam("file") MultipartFile file) {
        return userService.updateProfilePhoto(file, token);
    }


    @Operation(tags = "user", description = "All information about user profile. Review star values " +
            "are expressed in percentages.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping
    public UserDetailsResponse getUserInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserInfo(token);
    }


    @Operation(summary = "Update profile information", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "To update specific " +
            "information, send them as parameters. You can omit those that are of no interest to you. " +
            "In case you are filling in credit card or address info for the first time, " +
            "all fields must be included.",
            content = @Content(
                    schema=@Schema(implementation = UserInfoUpdateRequest.class))
    )
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping("/update")
    public MessageResponse updateUser(@RequestHeader("Authorization") String token,
                                              @RequestBody UserInfoUpdateRequest info) {
        return userService.updateUser(token, info);
    }


    @Operation(tags = "user", description = "Delete your account and all information connected to it.")
    //------------------------------------------------------------------------------------------------------------------
    @DeleteMapping
    public String deactivateUser(@RequestHeader("Authorization") String token) {
        userService.deactivateUser(token);
        return "User deactivated";
    }


    @Operation(tags = "user", description = "Default location and phone number for product shipment " +
            "extracted from user profile.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/shipment")
    public Shipment getShipmentInfo(@RequestHeader("Authorization") String token) {
        return userService.getShipmentInfo(token);
    }


    @Operation(tags = "user", description = "Get all users from DB.")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/users")
    public UsersResponse getUsers(@RequestParam("page") Integer page,
                                   @RequestParam("users") Integer users,
                                   @RequestParam(value = "sort", required = false,
                                               defaultValue = "firstName") String sort,
                                   @RequestParam(value = "search", required = false,
                                               defaultValue = "") String search) {
        return userService.getUsers(page, users, sort, search);
    }


    @Operation(tags = "user", description = "Reactivate user that is deactivated.")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @PutMapping("/admin/reactivate")
    public MessageResponse getUsers(@RequestParam("userId") Long id) {
        return userService.reactivateUser(id);
    }


    @Operation(tags = "user", description = "Delete your account and all information connected to it.")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/permanent")
    public String deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUserPermanently(id);
        return "User deactivated";
    }


    @Operation(tags = "user", description = "Send user information over email in CSV file.")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/export")
    public MessageResponse exportUser(@RequestParam("userId") Long id) {
        return userService.exportUser(id);
    }


    @Operation(tags = "user", description = "Block a specific user.")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @PutMapping("/admin/block")
    public MessageResponse blockUser(@RequestParam("userId") Long id) {
        return userService.blockUser(id);
    }


    @Operation(tags = "user", description = "Get users login activity over the period of time.")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/login-activity")
    public LoginActivityResponse getUsersLoginActivity() {
        return userService.getUsersLoginActivity();
    }


    @Operation(tags = "user", description = "Get users age structure (percentage).")
    //------------------------------------------------------------------------------------------------------------------
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/age")
    public AgeStructureResponse getUsersAgeStructure() {
        return userService.getUsersAgeStructure();
    }
}
