package com.ankit.HealthCare_Backend.usermanagement.profile.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ankit.HealthCare_Backend.Cloudinary.CloudinaryService;
import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.doctor.repository.DoctorRepository;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.usermanagement.patient.repository.PatientRepository;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;



@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;

    @Transactional
    public String uploadProfilePicture(MultipartFile file, String userEmail) throws IOException {

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        log.info("validating file Content Type");
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (5MB max)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        //Save to Cloudinary
        String url=cloudinaryService.uploadImage(file);
        User user=userRepo.findByEmail(userEmail);

        if("PATIENT".equalsIgnoreCase(user.getRole().getName())){
            Patient patient=patientRepo.findByUserId(user.getId());
            
            patient.setProfilePicture(url);
            patientRepo.save(patient);
        } else if ("DOCTOR".equalsIgnoreCase(user.getRole().getName())) {
            Doctor doctor = doctorRepo.findByUserId(user.getId());
            doctor.setProfilePicture(url);
            doctorRepo.save(doctor);
        }

        // Return URL that can be used to access the image
        return url;
    }



    //delete Service
    @Transactional
    public void deleteProfilePicture(String userEmail) throws IOException {
        User user = userRepo.findByEmail(userEmail);
        if (user == null) {
            log.warn("Attempted to delete profile picture for non-existent user: {}", userEmail);
            return;
        }

        String imageUrl = null;
        String resolvedUserType = user.getRole() != null ? user.getRole().getName() : null;

        if ("PATIENT".equalsIgnoreCase(resolvedUserType)) {
            Patient patient = patientRepo.findByUserId(user.getId());
            if (patient != null) {
                imageUrl = patient.getProfilePicture();
                patient.setProfilePicture(null);
                patientRepo.save(patient);
            }
        } else if ("DOCTOR".equalsIgnoreCase(resolvedUserType)) {
            Doctor doctor = doctorRepo.findByUserId(user.getId());
            if (doctor != null) {
                imageUrl = doctor.getProfilePicture();
                doctor.setProfilePicture(null);
                doctorRepo.save(doctor);
            }
         } else {
            log.warn("Unsupported role for profile picture deletion: {}", resolvedUserType);
            return;
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null) {
                log.info("Deleting image from Cloudinary with public_id: {}", publicId);
                cloudinaryService.deleteImage(publicId);
            } else {
                log.warn("Could not extract public_id from URL: {}", imageUrl);
            }
        }
    }

    private String extractPublicIdFromUrl(String url) {
        try {
            // Example URL: http://res.cloudinary.com/cloud/image/upload/v123/healthcare/profiles/sample.jpg
            // We want to extract "healthcare/profiles/sample"
            int uploadIndex = url.indexOf("/upload/");
            String afterUpload = url.substring(uploadIndex + "/upload/".length());
            String publicIdWithVersionAndExtension = afterUpload.substring(afterUpload.indexOf("/") + 1);
            return publicIdWithVersionAndExtension.substring(0, publicIdWithVersionAndExtension.lastIndexOf('.'));
        } catch (Exception e) {
            log.error("Could not extract public_id from url: {}", url, e);
            return null;
        }
    }
}