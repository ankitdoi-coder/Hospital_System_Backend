package com.ankit.HealthCare_Backend.filemanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankit.HealthCare_Backend.filemanagement.entity.ProfilePicture;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    
    Optional<ProfilePicture> findByUserEmailAndUserType(String userEmail, String userType);
    
    void deleteByUserEmailAndUserType(String userEmail, String userType);
}