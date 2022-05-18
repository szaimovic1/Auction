package com.ABH.Auction.controllers;

import com.ABH.Auction.domain.Color;
import com.ABH.Auction.services.ColorService;
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
@RequestMapping(path = "api/v1/color")
@AllArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @Operation(summary = "Get colors",
            description = "Get a list of objects of all existing colors in database. " +
                    "It includes names of colors that are the main piece of information " +
                    "that will be needed for any operations with colors", tags = "color")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found colors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Color.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping
    public List<Color> getColors() {
        return colorService.getColors();
    }
}
