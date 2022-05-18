package com.ABH.Auction.controllers;

import com.ABH.Auction.requests.PaymentRequest;
import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.responses.PaymentResponse;

import com.ABH.Auction.services.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(tags = "payment", description = "Create payment intent.")
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/create-payment-intent")
    public PaymentResponse paymentIntent(@RequestHeader("Authorization") String token,
                                         @RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPaymentIntent(paymentRequest, token);
    }

    @Operation(tags = "payment", description = "Payment successful. Redirecting to from Stripe.")
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/update-payment")
    public ResponseEntity<MessageResponse> updatePayment(@RequestParam("payment_intent") String payment,
                                                         @RequestParam("payment_intent_client_secret") String clientSecret,
                                                         @RequestParam("redirect_status") String status) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(paymentService.updatePayment(payment))).build();
    }
}
