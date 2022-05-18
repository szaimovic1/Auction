package com.ABH.Auction.controllers;

import com.ABH.Auction.requests.WishlistRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.services.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/wishlist")
@AllArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @Operation(tags = "wishlist", description = "Add/remove product on/from your wishlist.")
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping()
    public MessageResponse removeOrAddProduct(@RequestHeader("Authorization") String token,
                                   @RequestBody WishlistRequest request) {
        //reminds me of a pattern..
        MessageResponse msg;
        if(request.getAdd()) {
            msg = wishlistService.postProduct(token, request.getProductId());
        }
        else {
            msg = wishlistService.removeProduct(token, request.getProductId());
        }
        return msg;
    }
}
