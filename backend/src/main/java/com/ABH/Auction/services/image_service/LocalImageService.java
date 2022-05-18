package com.ABH.Auction.services.image_service;

import com.ABH.Auction.domain.Image;
import com.ABH.Auction.repositories.ImageRepository;
import com.ABH.Auction.utility.LocalStorageIImgsArg;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
public class LocalImageService extends ImageService<LocalStorageIImgsArg> {
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public List<String> postImages(LocalStorageIImgsArg args) {
        for(String image : args.getImages()) {
            Image i = new Image(args.getProduct(), image);
            imageRepository.save(i);
        }
        return null;
    }

    @Override
    public List<Image> getImages(LocalStorageIImgsArg args) {
        return imageRepository.findAll();
    }
}
