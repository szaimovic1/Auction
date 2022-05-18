package com.ABH.Auction.services;

import com.ABH.Auction.domain.Product;
import com.ABH.Auction.domain.User;
import com.ABH.Auction.domain.Wishlist;
import com.ABH.Auction.repositories.WishlistRepository;
import com.ABH.Auction.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    //use productRepo directly or go for wishlistRepo ???

    public boolean isProductOnWishlist(Long productId, Long userId) {
        return wishlistRepository.isProductOnWishlist(productId, userId) != null;
    }

    @Transactional
    public MessageResponse postProduct(String token, Long productId) {
        MessageResponse msg = new MessageResponse();
        User user = userService.getUserFromToken(token);
        Optional<Wishlist> optionalWishlist = wishlistRepository.getUserWishlist(user.getId());
        Wishlist wishlist;
        if(optionalWishlist.isPresent()) {
            wishlist = optionalWishlist.get();
            if(wishlist.getProducts().stream().anyMatch(p -> p.getId().equals(productId))) {
                msg.setIsSuccess(false);
                msg.setMessage("Product already added!");
                return msg;
            }
        }
        else {
            wishlist = new Wishlist(user);
            wishlist = wishlistRepository.save(wishlist);
        }
        Optional<Product> optionalProduct = wishlistRepository.getProductForWishlist(productId);
        if (optionalProduct.isPresent()) {
            wishlist.getProducts().add(optionalProduct.get());
        }
        else {
            msg.setMessage("There is no such product!");
            msg.setIsSuccess(false);
        }
        return msg;
    }

    @Transactional
    public MessageResponse removeProduct(String token, Long productId) {
        MessageResponse msg = new MessageResponse();
        User user = userService.getUserFromToken(token);
        Optional<Wishlist> optionalWishlist = wishlistRepository.getUserWishlist(user.getId());
        Wishlist wishlist;
        if(optionalWishlist.isPresent()) {
            wishlist = optionalWishlist.get();
            wishlist.getProducts().removeIf(p -> p.getId().equals(productId));

        }
        return msg;
    }

    public Page<Product> getWishlistProducts(Long userId, Pageable pageable) {
        return wishlistRepository.getWishlistProducts(userId, LocalDateTime.now(), pageable);
    }

    public List<String> getWishlistUserWithProduct(Long productId) {
        return wishlistRepository.getWishlistUserContainingProduct(productId);
    }
}
