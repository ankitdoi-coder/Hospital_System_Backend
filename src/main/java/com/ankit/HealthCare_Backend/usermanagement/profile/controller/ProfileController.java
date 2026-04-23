package com.ankit.HealthCare_Backend.usermanagement.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ankit.HealthCare_Backend.usermanagement.profile.service.ProfileService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Patient Profile Picture Endpoints
    @PostMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> uploadPatientProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) {
        try {
            String userEmail = principal.getName();
            String imageUrl = profileService.uploadProfilePicture(file, userEmail, "PATIENT");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile picture uploaded successfully");
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to upload profile picture: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> getPatientProfilePicture(Principal principal) {
        try {
            String userEmail = principal.getName();
            String imageUrl = profileService.getProfilePictureUrl(userEmail, "PATIENT");
            
            if (imageUrl != null) {
                return ResponseEntity.ok(imageUrl);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> deletePatientProfilePicture(Principal principal) {
        try {
            String userEmail = principal.getName();
            profileService.deleteProfilePicture(userEmail, "PATIENT");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile picture deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete profile picture: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Doctor Profile Picture Endpoints
    @PostMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> uploadDoctorProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) {
        try {
            String userEmail = principal.getName();
            String imageUrl = profileService.uploadProfilePicture(file, userEmail, "DOCTOR");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile picture uploaded successfully");
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to upload profile picture: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> getDoctorProfilePicture(Principal principal) {
        try {
            String userEmail = principal.getName();
            String imageUrl = profileService.getProfilePictureUrl(userEmail, "DOCTOR");
            
            if (imageUrl != null) {
                return ResponseEntity.ok(imageUrl);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> deleteDoctorProfilePicture(Principal principal) {
        try {
            String userEmail = principal.getName();
            profileService.deleteProfilePicture(userEmail, "DOCTOR");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile picture deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete profile picture: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}