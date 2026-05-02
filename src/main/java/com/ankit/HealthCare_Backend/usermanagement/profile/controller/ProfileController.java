package com.ankit.HealthCare_Backend.usermanagement.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ankit.HealthCare_Backend.usermanagement.profile.service.ProfileService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> uploadPatientProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) {
        String imageUrl = profileService.uploadProfilePicture(file, principal.getName(), "PATIENT");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture uploaded successfully", "imageUrl", imageUrl));
    }

    @GetMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> getPatientProfilePicture(Principal principal) {
        String imageUrl = profileService.getProfilePictureUrl(principal.getName(), "PATIENT");
        return imageUrl != null ? ResponseEntity.ok(imageUrl) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/patient/profile/picture")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> deletePatientProfilePicture(Principal principal) {
        profileService.deleteProfilePicture(principal.getName(), "PATIENT");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture deleted successfully"));
    }

    @PostMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> uploadDoctorProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Principal principal) {
        String imageUrl = profileService.uploadProfilePicture(file, principal.getName(), "DOCTOR");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture uploaded successfully", "imageUrl", imageUrl));
    }

    @GetMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> getDoctorProfilePicture(Principal principal) {
        String imageUrl = profileService.getProfilePictureUrl(principal.getName(), "DOCTOR");
        return imageUrl != null ? ResponseEntity.ok(imageUrl) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/doctor/profile/picture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> deleteDoctorProfilePicture(Principal principal) {
        profileService.deleteProfilePicture(principal.getName(), "DOCTOR");
        return ResponseEntity.ok(Map.of("success", true, "message", "Profile picture deleted successfully"));
    }
}
