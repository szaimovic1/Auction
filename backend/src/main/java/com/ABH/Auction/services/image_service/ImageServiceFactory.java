package com.ABH.Auction.services.image_service;

import com.ABH.Auction.repositories.ImageRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("factory")
public class ImageServiceFactory {
    private static ImageRepository imageRepository;
    private static Cloudinary cloudinary;

    @Autowired
    private ImageRepository imgRepository;
    @Autowired
    private Cloudinary cloud;

    @PostConstruct
    public void init() {
        System.out.println(imgRepository);
        ImageServiceFactory.imageRepository = imgRepository;
        ImageServiceFactory.cloudinary = cloud;
    }

    public static ImageService getImageService(String type) {
        switch (type) {
            case "cloudinary":
                return new CloudinaryService(cloudinary);
            case "local":
                return new LocalImageService(imageRepository);
            default:
                throw new IllegalArgumentException("Wrong type specified!");
        }
    }
}
