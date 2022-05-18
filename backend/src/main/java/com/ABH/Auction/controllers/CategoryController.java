package com.ABH.Auction.controllers;

import com.ABH.Auction.responses.CategoryResponse;
import com.ABH.Auction.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get categories",
            description = "Get a list of objects categories depending on requested type. " +
                    "It includes names, products and subcategories, as well as ids " +
                    "that are the main piece of information " +
                    "that will be needed for any operations with categories", tags = "category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)))
    })
    @Parameter(description = "For getting just categories that have no parent category " +
            "send 'main'. For getting all categories with subcategories, send 'all'.",
            name = "type",
            schema = @Schema(allowableValues = {"all", "main"}, defaultValue = "all")
    )
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping
    public List<CategoryResponse> getCategories(@RequestParam(value = "type", required = false,
            defaultValue = "all") String type) {
        return categoryService.getCategories(type);
    }


    @Operation(summary = "Get categories names with subcategories",
            description = "Get a list of categories names in format - (category) " +
                    "subcategory, or just category if it has no parent category.",
            tags = "category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @Parameter(description = "Send list of ids for categories you want to get full name " +
            "structure for.",
            name = "categories"
    )
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/pathname")
    public List<String> getCategoriesNames(@RequestParam("categories") List<Long> categories) {
        return categoryService.getCategoriesNames(categories);
    }
}
