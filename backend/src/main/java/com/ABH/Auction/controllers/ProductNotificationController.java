package com.ABH.Auction.controllers;

import com.ABH.Auction.observer.Observer;
import com.ABH.Auction.observer.PropertyChangedEventArgs;
import com.ABH.Auction.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@AllArgsConstructor
public class ProductNotificationController implements Observer<ProductService> {
    private final SimpMessagingTemplate template;
    private final ProductService productService;

    @PostConstruct
    private void postConstruct() {
        productService.subscribe(this);
    }

    @Override
    public void handle(PropertyChangedEventArgs<ProductService> args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(args.getWebSocketResponse());
            template.convertAndSend("/topic/message/" + args.getUserEmail(), json);
        } catch (JsonProcessingException e) {
            log.info("Value not converted to json string!", e);
        }
    }
}
