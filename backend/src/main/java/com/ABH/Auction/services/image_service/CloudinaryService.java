package com.ABH.Auction.services.image_service;

import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CloudinaryService extends ImageService<MultipartFile[]> {
    private final Cloudinary cloudinaryConfig;

    @Override
    public List<String> postImages(MultipartFile[] images) {
        List<String> links = new ArrayList<>();
        try {
            for(MultipartFile file : images) {
                File uploadedFile = new File(file.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(uploadedFile);
                fos.write(file.getBytes());
                fos.close();
                Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                //configuring secure: true did not work..
                links.add(uploadResult.get("url").toString()
                        .replace("http", "https"));
                uploadedFile.delete();
            }
        } catch (Exception e) {
            return null;
        }
        return links;
    }
}
