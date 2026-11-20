package com.ankit.HealthCare_Backend.usermanagement.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.ankit.HealthCare_Backend.usermanagement.profile.service.ProfileService;

import java.security.Principal;
import java.util.Map;

@Tag(name = "Profile Pictures", description = "Upload, view and delete profile pictures for patients and doctors")
@SecurityRequirement(name = "Bearer Auth")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Operation(summary = "Upload profile picture", description = "Uploads a profile picture for the logged-in user. Accepts multipart/form-data with key 'profilePicture'")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded, returns imageUrl"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or size exceeded")
    })
    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, Object>> uploadPatientProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) throws java.io.IOException {
        String imageUrl = profileService.uploadProfilePicture(file, principal.getName());
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture uploaded successfully", "imageUrl", imageUrl));
    }

    @Operation(summary = "Delete profile picture", description = "Deletes the logged-in user's profile picture")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture deleted successfully")
    })
    @DeleteMapping("/delete-image")
    public ResponseEntity<Map<String, Object>> deleteProfilePicture(
            Principal principal) throws java.io.IOException {
        profileService.deleteProfilePicture(principal.getName());
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture deleted successfully"));
    }
}
