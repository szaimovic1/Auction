package com.ABH.Auction.controllers;

import com.ABH.Auction.responses.SizeResponse;
import com.ABH.Auction.services.SizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/size")
@AllArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @Operation(summary = "Get sizes",
            description = "Get a list of objects of all existing sizes and products " +
                    "belonging to them in database. " +
                    "It includes names of sizes that are the main piece of information " +
                    "that will be needed for any operations with sizes", tags = "size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found sizes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SizeResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping
    public List<SizeResponse> getSizes() {
        return sizeService.getSizes();
    }
}
