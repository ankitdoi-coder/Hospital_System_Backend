package com.ankit.HealthCare_Backend.usermanagement.admin.repository;

import com.ankit.HealthCare_Backend.usermanagement.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
    
}
