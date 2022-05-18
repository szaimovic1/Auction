package com.ABH.Auction.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UsersResponse extends MessageResponse {
    private final List<UserResponse> users;
    private final Long numberOfUsers;
}
