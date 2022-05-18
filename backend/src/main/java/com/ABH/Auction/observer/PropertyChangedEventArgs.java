package com.ABH.Auction.observer;

import com.ABH.Auction.responses.WebSocketResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyChangedEventArgs<T> {
    private T source;
    private String userEmail;
    private WebSocketResponse webSocketResponse;
}
