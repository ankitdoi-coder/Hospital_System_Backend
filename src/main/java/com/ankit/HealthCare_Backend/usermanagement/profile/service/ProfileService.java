package com.ankit.HealthCare_Backend.usermanagement.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ankit.HealthCare_Backend.filemanagement.entity.ProfilePicture;
import com.ankit.HealthCare_Backend.filemanagement.repository.ProfilePictureRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    @Value("${app.file.upload.dir}")
    private String uploadDir;

    public String uploadProfilePicture(MultipartFile file, String userEmail, String userType) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (5MB max)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file to disk
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save or update database record
        Optional<ProfilePicture> existingPicture = profilePictureRepository
            .findByUserEmailAndUserType(userEmail, userType);

        ProfilePicture profilePicture;
        if (existingPicture.isPresent()) {
            profilePicture = existingPicture.get();
            // Delete old file if exists
            if (profilePicture.getFilePath() != null) {
                try {
                    Files.deleteIfExists(Paths.get(profilePicture.getFilePath()));
                } catch (IOException e) {
                    // Log error but continue
                    System.err.println("Failed to delete old profile picture: " + e.getMessage());
                }
            }
            profilePicture.setFilename(filename);
            profilePicture.setFilePath(filePath.toString());
        } else {
            profilePicture = new ProfilePicture();
            profilePicture.setUserEmail(userEmail);
            profilePicture.setUserType(userType);
            profilePicture.setFilename(filename);
            profilePicture.setFilePath(filePath.toString());
        }

        profilePictureRepository.save(profilePicture);

        // Return URL that can be used to access the image
        return "/api/files/profile-pictures/" + filename;
    }

    public String getProfilePictureUrl(String userEmail, String userType) {
        Optional<ProfilePicture> profilePicture = profilePictureRepository
            .findByUserEmailAndUserType(userEmail, userType);

        if (profilePicture.isPresent()) {
            return "/api/files/profile-pictures/" + profilePicture.get().getFilename();
        }

        return null;
    }

    public void deleteProfilePicture(String userEmail, String userType) throws IOException {
        Optional<ProfilePicture> profilePicture = profilePictureRepository
            .findByUserEmailAndUserType(userEmail, userType);

        if (profilePicture.isPresent()) {
            ProfilePicture picture = profilePicture.get();
            
            // Delete file from disk
            if (picture.getFilePath() != null) {
                Files.deleteIfExists(Paths.get(picture.getFilePath()));
            }

            // Delete database record
            profilePictureRepository.delete(picture);
        }
    }

    public byte[] getProfilePictureFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        }
        return null;
    }
}