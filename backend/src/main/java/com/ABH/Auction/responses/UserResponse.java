package com.ABH.Auction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserResponse extends MessageResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String profilePicture;
    private final ReviewResponse review;

    public UserResponse(Long id, String firstName, String lastName, String profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        review = null;
    }
}
