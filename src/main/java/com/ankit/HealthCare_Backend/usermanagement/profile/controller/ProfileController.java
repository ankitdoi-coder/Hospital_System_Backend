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
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Operation(summary = "Upload patient profile picture", description = "Uploads a profile picture for the logged-in patient. Accepts multipart/form-data with key 'profilePicture'")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded, returns imageUrl"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or size exceeded")
    })
    @PostMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> uploadPatientProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) throws java.io.IOException {
        String imageUrl = profileService.uploadProfilePicture(file, principal.getName(), "PATIENT");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture uploaded successfully", "imageUrl", imageUrl));
    }

    @Operation(summary = "Get patient profile picture URL", description = "Returns the URL of the logged-in patient's profile picture")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Image URL returned"),
        @ApiResponse(responseCode = "404", description = "No profile picture found")
    })
    @GetMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> getPatientProfilePicture(Principal principal) {
        String imageUrl = profileService.getProfilePictureUrl(principal.getName(), "PATIENT");
        return imageUrl != null ? ResponseEntity.ok(imageUrl) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete patient profile picture", description = "Deletes the logged-in patient's profile picture")
    @ApiResponse(responseCode = "200", description = "Profile picture deleted successfully")
    @DeleteMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> deletePatientProfilePicture(Principal principal) throws java.io.IOException {
        profileService.deleteProfilePicture(principal.getName(), "PATIENT");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture deleted successfully"));
    }

    @Operation(summary = "Upload doctor profile picture", description = "Uploads a profile picture for the logged-in doctor. Accepts multipart/form-data with key 'profilePicture'")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded, returns imageUrl"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or size exceeded")
    })
    @PostMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> uploadDoctorProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) throws java.io.IOException {
        String imageUrl = profileService.uploadProfilePicture(file, principal.getName(), "DOCTOR");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture uploaded successfully", "imageUrl", imageUrl));
    }

    @Operation(summary = "Get doctor profile picture URL", description = "Returns the URL of the logged-in doctor's profile picture")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Image URL returned"),
        @ApiResponse(responseCode = "404", description = "No profile picture found")
    })
    @GetMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> getDoctorProfilePicture(Principal principal) {
        String imageUrl = profileService.getProfilePictureUrl(principal.getName(), "DOCTOR");
        return imageUrl != null ? ResponseEntity.ok(imageUrl) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete doctor profile picture", description = "Deletes the logged-in doctor's profile picture")
    @ApiResponse(responseCode = "200", description = "Profile picture deleted successfully")
    @DeleteMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> deleteDoctorProfilePicture(Principal principal) throws java.io.IOException {
        profileService.deleteProfilePicture(principal.getName(), "DOCTOR");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture deleted successfully"));
    }
}
