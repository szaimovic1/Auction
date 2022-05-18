package com.ABH.Auction.observer;

import com.ABH.Auction.responses.WebSocketResponse;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        this.observers.add(observer);
    }

    protected void notifyObservers(T source, String userEmail, WebSocketResponse response) {
        for (Observer<T> o : observers) {
            o.handle(new PropertyChangedEventArgs<T>(source, userEmail, response));
        }
    }
}
