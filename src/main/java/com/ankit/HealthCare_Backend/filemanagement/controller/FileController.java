package com.ankit.HealthCare_Backend.filemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.usermanagement.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ProfileService profileService;

    /**
     * Serves profile pictures from the application directory
     * Anyone can download without authentication (for displaying in frontend)
     * 
     * @param filename The name of the profile picture file
     * @return ResponseEntity containing the file bytes
     */
    @GetMapping("/profile-pictures/{filename}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String filename) {
        try {
            // Security: Prevent directory traversal attacks
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                logger.warn("Attempted directory traversal attack with filename: {}", filename);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            byte[] imageData = profileService.getProfilePictureFile(filename);
            
            if (imageData != null) {
                HttpHeaders headers = new HttpHeaders();
                
                // Determine content type based on file extension
                String contentType = "image/jpeg"; // default
                if (filename.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (filename.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                }
                
                headers.setContentType(MediaType.parseMediaType(contentType));
                headers.setContentLength(imageData.length);
                headers.setCacheControl("max-age=3600"); // Cache for 1 hour
                
                logger.info("Successfully served profile picture: {}", filename);
                return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
            } else {
                logger.warn("Profile picture not found: {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error serving profile picture: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}