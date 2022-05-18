package com.ABH.Auction.services;

import com.ABH.Auction.domain.Bid;
import com.ABH.Auction.domain.Payment;
import com.ABH.Auction.domain.Product;
import com.ABH.Auction.domain.User;
import com.ABH.Auction.repositories.PaymentRepository;
import com.ABH.Auction.requests.PaymentRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.responses.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.apiKey}")
    private String secretKey;

    @Value("${frontend.path}")
    private String frontend;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    private final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private final UserService userService;
    private final BidService bidService;

    public PaymentResponse createPaymentIntent(PaymentRequest paymentRequest, String token) {
        Product product = productService.getProductById(paymentRequest.getProductId());
        User user = userService.getUserFromToken(token);
        MessageResponse msg = checkPayment(product, user, paymentRequest.getProductPrice());
        if(!msg.getIsSuccess()) {
            return new PaymentResponse(msg.getMessage(), false, null);
        }
        Optional<Payment> optPayment = paymentRepository.findByProduct(product);
        if(optPayment.isPresent()) {
            PaymentResponse response = new PaymentResponse(null);
            if(optPayment.get().isFinalized()) {
                response.setMessage("Payment already processed!");
                response.setIsSuccess(false);
            }
            else {
                response.setClientSecret(optPayment.get().getClientSecret());
            }
            return response;
        }
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(((Double) (paymentRequest.getProductPrice() * 100)).longValue())
                        .setCurrency("usd")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.create(params);
            Payment payment = new Payment(
                    paymentIntent.getId(),
                    paymentIntent.getClientSecret(),
                    user,
                    product,
                    paymentRequest.getProductPrice(),
                    false
            );
            paymentRepository.save(payment);
        } catch (StripeException e) {
            logger.log(Level.INFO, e.getMessage(), e);
            return new PaymentResponse("Payment not successful!", false, null);
        }
        return new PaymentResponse(null, true, paymentIntent.getClientSecret());
    }

    @Transactional
    public String updatePayment(String payment) {
        Optional<Payment> optionalPayment = paymentRepository.findByPaymentIntentId(payment);
        if(optionalPayment.isPresent()) {
            optionalPayment.get().setFinalized(true);
            logger.info("Payment " + payment + " recorded!");
            return (frontend + "/product/" + productService
                    .getProductIdByPaymentIntent(payment) + "?review=true");
        }
        else {
            return frontend;
        }
    }

    public MessageResponse checkPayment(Product product, User user, Double price) {
        MessageResponse msg = new MessageResponse(false);
        if(product == null) {
            msg.setMessage("No product found!");
            return msg;
        }
        if(product.getEndDate().compareTo(LocalDateTime.now()) > 0) {
            msg.setMessage("Ongoing auction!");
            return msg;
        }
        Bid highest = bidService.getHighestBid(product);
        if(highest == null || !highest.getValue().equals(price)) {
            msg.setMessage("Bad bid value!");
            return msg;
        }
        if(!highest.getCustomer().getId().equals(user.getId())) {
            msg.setMessage("Product is not sold to you!");
            return msg;
        }
        msg.setIsSuccess(true);
        return msg;
    }
}
