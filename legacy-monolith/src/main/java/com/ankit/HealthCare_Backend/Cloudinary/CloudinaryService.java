package com.ankit.HealthCare_Backend.Cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.ankit.HealthCare_Backend.Exception.ImageUploadException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    //upload image Service
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ImageUploadException("File is empty");
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
            ObjectUtils.asMap("folder", "healthcare/profiles"));
        return (String) uploadResult.get("secure_url");
    }

    //delete the image Service
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
    }
}
