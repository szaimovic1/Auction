package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MessageResponse {
    private String message = null;
    private Boolean isSuccess = true;

    public MessageResponse() {
    }

    public MessageResponse(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
