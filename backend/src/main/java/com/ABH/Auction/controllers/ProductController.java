package com.ABH.Auction.controllers;

import com.ABH.Auction.requests.BidRequest;
import com.ABH.Auction.requests.ProductRequest;
import com.ABH.Auction.responses.*;
import com.ABH.Auction.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get products",
            description = "All users are able to get products with active auctions. " +
                    "They can be selected by category (list of category ids), sizes " +
                    "(list of sizes names), colors (list of colors names), minimum and " +
                    "maximum price for products range. Additional parameters field and " +
                    "asc (order) do not affect number of products we get. They are just " +
                    "the way to sort products by. We can restrict number of products by the " +
                    "usage of paging (page and prods) parameters. In case we want to ignore " +
                    "specific criteria, for list we should send empty list. For numeric types " +
                    "ignored value is '0'. Search parameter looks for product name match.", tags = "product")
    @Parameter(description = "You can sort products by this value.",
            name = "field",
            schema = @Schema(allowableValues = {"name", "initialPrice", "startDate", "endDate"})
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "'numberOfProducts' describes number of " +
                    "all products included by sent parameters. If '-1' is returned it indicates " +
                    "wrong price setting, as the message shows.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductsResponse.class)))
    })
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping
    public ProductsResponse getProducts(
            @RequestHeader(value = "Authorization",
                    required = false, defaultValue = "") String token,
            @RequestParam("categories") List<Long> categories,
            @RequestParam("sizes") List<String> sizes,
            @RequestParam("colors") List<String> colors,
            @RequestParam("startPrice") Double startPrice,
            @RequestParam("endPrice") Double endPrice,
            @RequestParam("page") Integer page,
            @RequestParam("prods") Integer prods,
            @RequestParam(value = "field",
                    required = false, defaultValue = "") String field,
            @RequestParam(value = "asc",
                    required = false, defaultValue = "0") Boolean asc,
            @RequestParam(value = "search",
                    required = false, defaultValue = "") String search) {
        return productService.getProducts(categories,
                sizes,
                colors,
                startPrice,
                endPrice,
                page,
                prods,
                field,
                asc,
                search,
                token);
    }


    @Operation(tags = "product", description = "Get a random product from database.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/highlight")
    public ProductResponse getHighlight() {
        return productService.getHighlight();
    }


    @Operation(tags = "product", description = "Collect all information about a specific product. " +
            "If the id is wrong or an auction still did not start, 'Product not found!' message " +
            "will be returned.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/info")
    public ProductInfoResponse getProductInfo(@RequestHeader(value = "Authorization",
            required = false, defaultValue = "") String token, @RequestParam("id") Long id) {
        return productService.getAllAboutProduct(token, id);
    }


    @Operation(tags = "product", description = "If posting bid for product does not succeed, proper " +
            "message will be returned, followed by 'isSuccess' false. If bid is properly posted " +
            "'isSuccess' will be true.")
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/bid")
    public MessageResponse postBid(@RequestHeader("Authorization") String token,
                                   @RequestBody BidRequest requestBod) {
        return productService.postBidForProduct(token, requestBod.getId(), requestBod.getValue());
    }


    @Operation(tags = "product", description = "See who placed bids for your product.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/bidders")
    public List<BidResponse> getProductBidders(@RequestHeader("Authorization") String token,
                                               @RequestParam("id") Long id,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("amount") Integer amount) {
        return productService.getBidsForProduct(token, id, page, amount);
    }

    @Operation(tags = "product")
    @Parameters(value = {
            @Parameter(description = "To get products you placed bids for " +
                    "set this parameter as true. Otherwise it will be ignored " +
                    "and parameter active will dictate response. Unless you purposefully omit it, " +
                    "which would indicate getting wishlist products.",
                    name = "bidder"
            ),
            @Parameter(description = "If true, your products with active auction " +
                    "will be returned. If false, products you sold (expired auction) " +
                    "will be returned.",
                    name = "active"
            )
    })
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/product-bid")
    public ProductBidResponse getProductsWithBids(@RequestHeader("Authorization") String token,
                                                  @RequestParam(value = "bidder", required = false) Boolean isBidder,
                                                  @RequestParam(value = "active", required = false,
                                                                defaultValue = "true") Boolean active,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("prods") Integer prods) {
        return productService.getProductsWithBids(token, isBidder, active, page, prods);
    }


    @Operation(tags = "product")
    @Parameter(description = "When posting a new product, at least three " +
            "images are required.",
            name = "files"
    )
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping
    public MessageResponse postProduct(@RequestHeader("Authorization") String token,
                                       @RequestParam("product") String requestProd,
                                       @RequestParam("files") MultipartFile[] files) {
        ProductRequest pr;
        try {
            pr = new ObjectMapper().registerModule(new JavaTimeModule())
                                   .readValue(requestProd, ProductRequest.class);
        } catch (JsonProcessingException e) {
            return new MessageResponse("Invalid data.", false);
        }
        return productService.postProduct(token, pr, files);
    }


    @Operation(tags = "product", description = "Products that might be of " +
            "interest to the specific user based on his preferences.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/recommended")
    public ProductsResponse getRecommendedProducts(@RequestHeader("Authorization") String token,
                                                   @RequestParam("page") Integer page,
                                                   @RequestParam("prods") Integer prods,
                                                   @RequestParam(value = "field",
                                                           required = false, defaultValue = "price") String field) {
        return productService.getRecommendedProducts(token, page, prods, field);
    }


    @Operation(tags = "product", description = "In case search results " +
            "return no products we can check if similar words are available " +
            "as products names.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/search-correction")
    public String searchCorrection(@RequestParam("search") String search) {
        return productService.searchCorrection(search);
    }
}
