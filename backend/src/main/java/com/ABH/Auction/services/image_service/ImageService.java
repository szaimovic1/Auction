package com.ABH.Auction.services.image_service;

import com.ABH.Auction.domain.Image;

import java.util.List;

public abstract class ImageService<A> {
    //could instance arg be included here?

    public abstract List<String> postImages(A args);

    public List<Image> getImages(A args) {
        return null;
    }
}
