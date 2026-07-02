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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ankit.HealthCare_Backend.usermanagement.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Files", description = "Public file serving — no authentication required")
@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ProfileService profileService;

    @Operation(
        summary = "Get profile picture by filename",
        description = "Serves a profile picture file by its filename. No authentication required — used by frontend to display images. Cached for 1 hour"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Image file returned"),
        @ApiResponse(responseCode = "403", description = "Directory traversal attempt detected"),
        @ApiResponse(responseCode = "404", description = "File not found"),
        @ApiResponse(responseCode = "500", description = "Error reading file")
    })
    @GetMapping("/profile-pictures/{filename}")
    public ResponseEntity<byte[]> getProfilePicture(
            @Parameter(description = "Profile picture filename", required = true, example = "patient_1_profile.jpg")
            @PathVariable String filename) {
        try {
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                logger.warn("Attempted directory traversal attack with filename: {}", filename);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            byte[] imageData = profileService.getProfilePictureFile(filename);

            if (imageData != null) {
                HttpHeaders headers = new HttpHeaders();
                String contentType = "image/jpeg";
                if (filename.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (filename.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                }
                headers.setContentType(MediaType.parseMediaType(contentType));
                headers.setContentLength(imageData.length);
                headers.setCacheControl("max-age=3600");
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
