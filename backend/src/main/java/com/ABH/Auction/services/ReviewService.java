package com.ABH.Auction.services;

import com.ABH.Auction.domain.Review;
import com.ABH.Auction.domain.User;
import com.ABH.Auction.repositories.ReviewRepository;
import com.ABH.Auction.requests.ReviewRequest;
import com.ABH.Auction.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public Integer getUserRating(String token, Long id) {
        Optional<Review> optReview = reviewRepository
                .getSingularRating(userService.getUserFromToken(token).getId(), id);
        return optReview.map(Review::getRating).orElse(null);
    }

    @Transactional
    public MessageResponse rateUser(String token, ReviewRequest request) {
        MessageResponse msg = new MessageResponse(false);
        User user = userService.getUserFromToken(token);
        if(user.getId().equals(request.getSellerId())) {
            msg.setMessage("Cannot rate yourself!");
            return msg;
        }
        if(request.getRating() < 0 || request.getRating() > 5) {
            msg.setMessage("Bad rating value!");
            return msg;
        }
        Optional<Review> optionalReview =  reviewRepository
                .getSingularRating(userService.getUserFromToken(token).getId(), request.getSellerId());
        if(optionalReview.isPresent()) {
            optionalReview.get().setRating(request.getRating());
        }
        else {
            Review review = new Review(
                    userService.getUserById(request.getSellerId()).get(),
                    user,
                    request.getRating()
            );
            reviewRepository.save(review);
        }
        return new MessageResponse();
    }
}
