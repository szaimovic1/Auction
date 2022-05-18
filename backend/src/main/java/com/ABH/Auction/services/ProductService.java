package com.ABH.Auction.services;

import com.ABH.Auction.domain.*;
import com.ABH.Auction.observer.Observable;
import com.ABH.Auction.repositories.BidRepository;
import com.ABH.Auction.repositories.ImageRepository;
import com.ABH.Auction.repositories.ProductRepository;
import com.ABH.Auction.requests.ProductRequest;
import com.ABH.Auction.responses.*;
import com.ABH.Auction.services.image_service.ImageService;
import com.ABH.Auction.services.image_service.ImageServiceFactory;
import com.ABH.Auction.utility.EmailValidator;
import com.ABH.Auction.utility.LevenshteinDistance;
import com.ABH.Auction.utility.LocalStorageIImgsArg;
import com.ABH.Auction.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@DependsOn({"factory"})
@RequiredArgsConstructor
public class ProductService extends Observable<ProductService> {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final BidRepository bidRepository;
    private final CategoryService categoryService;
    private final EmailValidator emailValidator;
    private final ShipmentService shipmentService;
    private final PhoneNumberService phoneNumberService;
    private final AddressService addressService;
    private ImageService localImageService;
    private ImageService cloudinaryService;
    private final WishlistService wishlistService;
    private final SchedulerService schedulerService;

    @PostConstruct
    private void setImgService() {
        cloudinaryService = ImageServiceFactory.getImageService("cloudinary");
        localImageService = ImageServiceFactory.getImageService("local");
    }

    public ProductsResponse getProducts(
            List<Long> categories,
            List<String> sizes,
            List<String> colors,
            Double startPrice,
            Double endPrice,
            Integer page,
            Integer prods,
            String field,
            Boolean asc,
            String search,
            String token){

        ProductsResponse response = new ProductsResponse();
        PriceResponse pr = getCurrentPriceRange(categories, sizes, colors, search);
        if(!checkPriceRange(pr.getMinPrice(), pr.getMaxPrice(), startPrice, endPrice)) {
            response.setIsSuccess(false);
            response.setMessage("Bad price value!");
            response.setNumberOfProducts(-1L);
            return response;
        }
        response.setStartPrice(pr.getMinPrice());
        response.setEndPrice(pr.getMaxPrice());
        Pageable pageable = PageRequest.of(page, prods);
        if(!field.isEmpty()) {
            pageable = PageRequest.of(page, prods).withSort(
                    Sort.by(asc ? Sort.Direction.ASC
                    : Sort.Direction.DESC, field));
        }
        if(categories.isEmpty()) {
            categories = null;
        }
        if(sizes.isEmpty()) {
            sizes = null;
        }
        if(colors.isEmpty()) {
            colors = null;
        }
        if(search.isEmpty()) {
            search = null;
        }
        if(search != null) {
            search = "%" + String.join("%", search.split(" "))
                    .toLowerCase() + "%";
        }
        Page<Product> pageResponse = productRepository.getProducts(categories,
                LocalDateTime.now(),
                sizes,
                colors,
                startPrice,
                endPrice,
                search,
                pageable);
        Long userId = token.isEmpty() ? null : userService.getUserFromToken(token).getId();
        List<ProductResponse> listProds = pageResponse.getContent().stream()
                .map(e -> createResponse(e, userId)).collect(Collectors.toList());
        response.getProducts().addAll(listProds);
        response.setNumberOfProducts(pageResponse.getTotalElements());
        return response;
    }

    public PriceResponse getCurrentPriceRange(List<Long> categories,
                                             List<String> sizes,
                                             List<String> colors,
                                             String search) {
        if(categories.isEmpty()) {
            categories = null;
        }
        if(sizes.isEmpty()) {
            sizes = null;
        }
        if(colors.isEmpty()) {
            colors = null;
        }
        if(search.isEmpty()) {
            search = null;
        }
        if(search != null) {
            search = "%" + String.join("%", search.split(" "))
                    .toLowerCase() + "%";
        }
        return createPriceResponse(productRepository
                .getCurrentPriceRange(
                        categories,
                        LocalDateTime.now(),
                        sizes,
                        colors,
                        search
                )
        );
    }

    public String getOneImg(Product p) {
        return imageRepository.findByProduct(p).get(0).getImage();
    }

    public ProductResponse getHighlight() {
        List<Product> product = productRepository
                .getHighlightedProduct(LocalDateTime.now(), PageRequest.of(0, 1));
        if(product.isEmpty()) {
            return new ProductResponse("No products!", false);
        }
        return createResponse(product.get(0), null);
    }

    public ProductResponse createResponse(Product p, Long userId) {
        Boolean isOnWishlist = null;
        if(userId != null) {
            isOnWishlist = wishlistService.isProductOnWishlist(p.getId(), userId);
        }
        return new ProductResponse(
                p.getName(),
                getOneImg(p),
                p.getId(),
                p.getPrice(),
                p.getDetails(),
                p.getEndDate(),
                isOnWishlist
        );
    }

    public ProductInfoResponse getAllAboutProduct(String token, Long id) {
        Optional<Product> optionalP = productRepository.findById(id);
        ProductInfoResponse response = new ProductInfoResponse();
        UserResponse user = userService.getUserResponseFromToken(token);
        //should be generated as not mapped field in Product class as
        //to not be fetched manually all the time?
        Optional<Bid> optHighest = bidRepository.getHighestBid(id);
        if(optionalP.isEmpty() || optionalP.get().getSeller().getEmail() == null) {
            response.setMessage("Product not found!");
            response.setIsSuccess(false);
            return response;
        }
        Product p = optionalP.get();
        boolean ifAuctionNotStarted = (p.getStartDate().compareTo(LocalDateTime.now()) > 0);
        boolean isUserSeller = user != null &&
                user.getId().equals(p.getSeller().getId());
        boolean ifUserNotSeller = (user == null || !isUserSeller);
        boolean ifAuctionEnded = (p.getEndDate().compareTo(LocalDateTime.now()) < 0);
        boolean isUserBuyer = (optHighest.isPresent() && user != null &&
                user.getId().equals(optHighest.get().getCustomer().getId()));
        boolean ifUserNotSellerOrBuyer = (user == null || !(isUserSeller || isUserBuyer));
        if((ifAuctionNotStarted && ifUserNotSeller) ||
                (ifAuctionEnded && ifUserNotSellerOrBuyer)) {
            response.setMessage("Product not found!");
            response.setIsSuccess(false);
            return response;
        }
        Optional<Payment> optionalPayment= productRepository.getPaymentForProduct(p);
        boolean isPaidFor =  true;
        if(optionalPayment.isEmpty() || !optionalPayment.get().isFinalized()) {
            isPaidFor = false;
        }
        response.setIsPaidFor(isPaidFor);
        Optional<Bid> optionalBid = bidRepository.getHighestBid(p.getId());
        response.setHighestBid(null);
        optionalBid.ifPresent(bid -> response.setHighestBid(bid.getValue()));
        response.setProduct(createResponse(p, null));
        response.setNumberOfBids(bidRepository.getNumberOfBids(p.getId()));
        response.setImages(imageRepository.findByProduct(p).stream()
                .map(Image::getImage).collect(Collectors.toList()));
        response.setCategoryId(p.getCategory().getId());
        if(user != null && p.getSeller().getId().equals(user.getId())) {
            response.setSeller(user);
        }
        else {
            response.setSeller(userService.getUserResponseFromUser(p.getSeller()));
            response.setRelatedProducts(productRepository
                    .getProductsByCategoryId(p.getCategory().getId(),
                            LocalDateTime.now(),
                            PageRequest.of(0, 3))
                    .stream().map(e -> createResponse(e, null))
                    .collect(Collectors.toList()));
            response.setBidders(false);
        }
        return response;
    }

    public List<BidResponse> getBidsForProduct(String token, Long id, Integer page, Integer amount) {
        UserResponse user = userService.getUserResponseFromToken(token);
        Optional<Product> optionalP = productRepository.findById(id);
        if(optionalP.isEmpty() ||
                !user.getId().equals(optionalP.get().getSeller().getId())) {
            return null;
        }
        return getBids(id, page, amount);
    }

    public List<BidResponse> getBids(Long id, Integer page, Integer amount) {
        return bidRepository.getBidsForProduct(id, PageRequest.of(page, amount)
                .withSort(Sort.by(Sort.Direction.DESC, "bidTime"))).stream()
                .map(b -> new BidResponse(userService.getUserResponseFromUser(b.getCustomer()),
                        b.getValue(), b.getBidTime())).collect(Collectors.toList());
    }

    public MessageResponse postBidForProduct(String token, Long id, Double value) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        User user = userService.getUserFromToken(token);
        if(optionalProduct.isEmpty() || optionalProduct.get().getSeller().getEmail() == null
                || user.getId().equals(optionalProduct.get().getSeller().getId())
                || optionalProduct.get().getStartDate().isAfter(LocalDateTime.now())) {
            return new MessageResponse("Product not found!", false);
        }
        Product product = optionalProduct.get();
        MessageResponse mr = checkBidValidity(user, product, id, value);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        Optional<Bid> highest = bidRepository.getHighestBid(id);
        Optional<Bid> bid = bidRepository.checkBidByCustomer(product.getId(), user.getId());
        if(bid.isPresent()) {
            bidRepository.updateBid(bid.get().getId(), value, LocalDateTime.now());
        }
        else {
            bidRepository.save(new Bid(product,
                    userService.getUserById(user.getId()).get(), value, LocalDateTime.now()));
        }
        if(highest.isPresent() && !highest.get().getCustomer().getEmail().equals(user.getEmail())) {
            WebSocketResponse wsr = new WebSocketResponse(id, getOneImg(product));
            wsr.setMessage("You are not the highest bidder for " + product.getName() + " anymore!");
            notifyObservers(this, highest.get().getCustomer().getEmail(), wsr);
        }
        return new MessageResponse( "Congrats! You are the highest bidder.", true);
    }

    public MessageResponse checkBidValidity(User user, Product product,
                                            Long id, Double value) {
        Optional<Bid> highest = bidRepository.getHighestBid(id);
        DecimalFormat df = new DecimalFormat("0.00");
        Bid highestBid = null;
        if(user.getId().equals(product.getSeller().getId())) {
            return new MessageResponse("Cannot bid for your products!", false);
        }
        if(product.getEndDate().isBefore(LocalDateTime.now())) {
            return new MessageResponse("Bidding time over!", false);
        }
        if(product.getStartDate().isAfter(LocalDateTime.now())) {
            return new MessageResponse("Come back later! Bidding not started yet.", false);
        }
        if(highest.isPresent())
            highestBid = highest.get();
        if(highestBid != null && highestBid.getValue() >= value) {
            return new MessageResponse("There are higher bids than yours. You could give a second try."
                    , false);
        }
        else if(value < 0 || value < product.getPrice()) {
            return new MessageResponse("Price should start from $" + df.format(product.getPrice()) + "!"
                    , false);
        }
        return new MessageResponse();
    }

    public Boolean checkPriceRange(Double minPrice, Double maxPrice,
                                Double startPrice, Double endPrice) {
        if(minPrice == null || maxPrice == null) {
            return true;
        }
        if(startPrice != 0 && (startPrice < minPrice ||
                (endPrice != 0 && startPrice > endPrice) ||
                (endPrice == 0 && startPrice > maxPrice))) {
            return false;
        }
        return endPrice == 0 || (endPrice <= maxPrice &&
                (startPrice != 0 || endPrice >= minPrice));
    }

    public ProductBidResponse getProductsWithBids(String token, Boolean isBidder, Boolean active,
                                                  Integer page, Integer prods) {
        User user = userService.getUserFromToken(token);
        List<Product> products;
        List<ProductBidResponses> response = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, prods);
        long numberOfProds;
        Page<Product> pageProds;
        if(isBidder == null) {
            pageProds = wishlistService.getWishlistProducts(user.getId(), pageable);
        }
        else if(isBidder) {
            pageProds = bidRepository.getBidsForUser(user.getId(), pageable);
        }
        else {
            pageProds = productRepository.getProductsForUser(user.getId(), active,
                    LocalDateTime.now(), pageable);
        }
        products = pageProds.getContent();
        numberOfProds = pageProds.getTotalElements();
        for(Product p : products) {
            Bid highest = bidRepository.getHighestBid(p.getId()).orElse(null);
            Bid highestPersonal = bidRepository.getHighestPersonalBid(
                    p.getId(),
                    user.getId()).orElse(null);
            int numOfBids = bidRepository.getNumberOfBids(p.getId());
            boolean isHighestBidMine = highest != null && highest.getCustomer()
                    .getId().equals(user.getId());
            response.add(
                    new ProductBidResponses(
                            createResponse(p, null),
                            numOfBids,
                            highest == null ? 0 : highest.getValue(),
                            isHighestBidMine,
                            highestPersonal == null ? 0 : highestPersonal.getValue()
                    )
            );
        }
        return new ProductBidResponse(response, numberOfProds);
    }

    @Transactional
    public MessageResponse postProduct(String token, ProductRequest product, MultipartFile[] files) {
        Address adr = new Address(product.getStreet(), product.getCity(),
                product.getZipCode(), product.getCountry());
        MessageResponse mr = AddressService.checkAddressForProduct(adr);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        mr = checkAdditionalProductInfo(product, files);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        Category c = categoryService.checkCategory(product.getCategoryId());
        if(c == null) {
            return new MessageResponse("Invalid category!", false);
        }
        User user = userService.getUserFromToken(token);
        Product productToAdd = new Product(user, c, product.getName(), product.getDescription(),
                product.getPrice(), product.getStartDate(), product.getEndDate());
        mr = checkBasicProductInfo(productToAdd);
        if(!mr.getIsSuccess()) {
            return mr;
        }
        productToAdd = productRepository.save(productToAdd);
        PhoneNumber pn = new PhoneNumber(product.getPhone(), false);
        pn = phoneNumberService.addPhoneNumber(pn);
        adr = addressService.addAddress(adr);
        Shipment s = new Shipment(adr, pn, product.getEmail());
        s = shipmentService.addShipment(s);
        productToAdd.setShipment(s);
        List<String> images = cloudinaryService.postImages(files);
        if(images == null) {
            return new MessageResponse("Cannot upload images!", false);
        }
        LocalStorageIImgsArg args = new LocalStorageIImgsArg(images, productToAdd);
        localImageService.postImages(args);
        schedulerService.scheduleNotification(productToAdd.getId(), productToAdd.getEndDate());
        return new MessageResponse("Item added", true);
    }

    public MessageResponse checkBasicProductInfo(Product p) {
        if(p.getPrice() == null || p.getPrice() <= 0) {
            return new MessageResponse("Bad price value!", false);
        }
        if(p.getName() == null || p.getName().isEmpty()
                || p.getName().length() < 3 || p.getName().length() > 100) {
            return new MessageResponse("Product name not valid!", false);
        }
        if(p.getDetails() == null || p.getDetails().isEmpty()
                || p.getDetails().length() < 3 || p.getDetails().length() > 700) {
            return new MessageResponse("Product description too long!", false);
        }
        if(p.getStartDate() == null || p.getEndDate() == null
                || (p.getStartDate().compareTo(LocalDateTime.now()) < 0)
                || (p.getEndDate().compareTo(p.getStartDate()) <= 0)) {
            return new MessageResponse("Invalid date format!", false);
        }
        return new MessageResponse("Success", true);
    }

    public MessageResponse checkAdditionalProductInfo(ProductRequest product, MultipartFile[] files) {
        if(Validator.isPhoneNumberInvalid(product.getPhone())) {
            return new MessageResponse("Wrong phone number!", false);
        }
        if(files.length < 3) {
            return new MessageResponse("Include at least 3 photos!", false);
        }
        if(!emailValidator.test(product.getEmail())) {
            return new MessageResponse("Incorrect email format!", false);
        }
        return new MessageResponse("Success", true);
    }

    public ProductsResponse getRecommendedProducts(String token,
                                                   Integer page,
                                                   Integer prods,
                                                   String field) {
        User user = userService.getUserFromToken(token);
        //Showing users products? But if I don't want to I'll have to regulate it
        //in getProducts (not a good idea bcs of its general usage
        //Or have query not dependant on getProducts for this case)
        List<Long> categories = recommendedCategories(user);
        PriceResponse pr = recommendedPriceRange(categories, user);
        Double startPrice = 0D;
        Double endPrice = 0D;
        if(pr.getMinPrice() != null) {
            startPrice = pr.getMinPrice();
        }
        if(pr.getMaxPrice() != null) {
            endPrice = pr.getMaxPrice();
        }
        return getProducts(
                categories,
                new ArrayList<>(),
                new ArrayList<>(),
                startPrice,
                endPrice,
                page,
                prods,
                field,
                true,
                "",
                token
        );
    }

    public List<Long> recommendedCategories(User user) {
        List<Long> categories = productRepository
                .getRecommendedProdsCategoriesIntersect(user.getId());
        if(categories.isEmpty()) {
            categories = productRepository
                    .getRecommendedProdsCategoriesBids(user.getId());
            categories.addAll(productRepository
                    .getRecommendedProdsCategoriesProds(user.getId()));
        }
        return categories;
    }

    public PriceResponse userPriceRange(User user) {
        PriceResponse userPriceRangeProds = createPriceResponse(productRepository
                .getRecommendedPriceRangeProds(user.getId(), LocalDateTime.now()));
        PriceResponse userPriceRangeBids = createPriceResponse(productRepository
                .getRecommendedPriceRangeBids(user.getId(), LocalDateTime.now()));
        PriceResponse userPriceRange;
        if(userPriceRangeBids.getMinPrice() == null) {
            userPriceRange = userPriceRangeProds;
        }
        else if(userPriceRangeProds.getMinPrice() == null) {
            userPriceRange = userPriceRangeBids;
        }
        else {
            Double minPrice = userPriceRangeBids.getMinPrice();
            Double maxPrice = userPriceRangeBids.getMaxPrice();
            if(userPriceRangeProds.getMinPrice() < userPriceRangeBids.getMinPrice()) {
                minPrice = userPriceRangeProds.getMinPrice();
            }
            if(userPriceRangeProds.getMaxPrice() > userPriceRangeBids.getMaxPrice()) {
                maxPrice = userPriceRangeProds.getMaxPrice();
            }
            userPriceRange = new PriceResponse(minPrice, maxPrice);
        }
        return userPriceRange;
    }

    public PriceResponse recommendedPriceRange(List<Long> categories, User user) {
        PriceResponse priceBorders = getCurrentPriceRange(categories, new ArrayList<>(),
                new ArrayList<>(), "");
        PriceResponse userPriceRange = userPriceRange(user);
        Double minPrice = userPriceRange.getMinPrice();
        Double maxPrice = userPriceRange.getMaxPrice();
        if(userPriceRange.getMinPrice() == null || priceBorders.getMinPrice() == null) {
            minPrice = null;
            maxPrice = null;
        }
        else {
            if (userPriceRange.getMinPrice() < priceBorders.getMinPrice()) {
                minPrice = priceBorders.getMinPrice();
            }
            if (userPriceRange.getMaxPrice() > priceBorders.getMaxPrice()) {
                maxPrice = priceBorders.getMaxPrice();
            }
        }
        return new PriceResponse(minPrice, maxPrice);
    }

    public PriceResponse createPriceResponse(List<Object[]> o) {
        return o.stream().map(e -> new PriceResponse((Double) e[0], (Double) e[1]))
                .collect(Collectors.toList()).get(0);
    }

    public String searchCorrection(final String search) {
        if(search.isEmpty() || search.replace(" ", "").length() < 3) {
            return null;
        }
        Set<String> set = new TreeSet<>();
        List<String> prodsNames = productRepository.
                getProductsNames(LocalDateTime.now());
        List<String> productsNames = prodsNames
                .stream().map(p -> p.split(" "))
                .flatMap(Stream::of)
                .filter(e -> set.add(e)
                        && e.length() > 2)
                .collect(Collectors.toList());
        productsNames.addAll(prodsNames.stream()
                .filter(set::add)
                .collect(Collectors.toList()));
        Map<String, Integer> correction = new HashMap<>();
        productsNames.forEach(e -> correction.put(e, LevenshteinDistance
                .calculate(e.toLowerCase(), search.toLowerCase())));
        Optional<Map.Entry<String, Integer>> bestMatch = correction
                .entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue));
        return bestMatch.map(stringIntegerEntry ->
                stringIntegerEntry.getKey().toLowerCase()).orElse(null);
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public Long getProductIdByPaymentIntent(String payment) {
        return productRepository.getProductIdByPaymentIntent(payment);
    }

    public void handleNotification(Long productId, Boolean startPayment) {
        Optional<Bid> optHighest = bidRepository.getHighestBid(productId);
        Optional<Product> optProduct = productRepository.findById(productId);
        if(optHighest.isEmpty() || optProduct.isEmpty()) {
            return;
        }
        Product product = optProduct.get();
        Bid highest = optHighest.get();
        WebSocketResponse wsr = new WebSocketResponse(productId, getOneImg(product));
        wsr.setStartPayment(startPayment);
        if(startPayment) {
            wsr.setMessage("You outbid your competition for "
                    + product.getName() + ".");
            notifyObservers(this, highest.getCustomer().getEmail(), wsr);
        }
        else {
            List<String> users = wishlistService.getWishlistUserWithProduct(productId);
            users.forEach(u -> {
                if(!highest.getCustomer().getEmail().equals(u)) {
                    wsr.setMessage("Auction for " + product.getName() + " is closing soon.");
                    notifyObservers(this, u, wsr);
                }
            });
        }
    }
}
