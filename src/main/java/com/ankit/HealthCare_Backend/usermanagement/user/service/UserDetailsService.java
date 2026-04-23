package com.ankit.HealthCare_Backend.usermanagement.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.core.entity.Role;
import com.ankit.HealthCare_Backend.usermanagement.admin.entity.Admin;
import com.ankit.HealthCare_Backend.usermanagement.admin.repository.AdminRepository;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private AdminRepository adminRepo;
   

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // --- Step 1: Check if the login is for an admin from database ---
        Admin admin = adminRepo.findByEmail(email);
        if (admin != null && admin.isActive()) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(admin.getEmail())
                    .password(admin.getPassword()) // Use the hashed password from database
                    .roles("ADMIN")
                    .build();
        }

        // --- Step 2: Check for regular user (Doctor or Patient) ---
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        
        // Step 1: Get the entire Role object from the user.
        Role roleObject = user.getRole();

        // Step 2: Get the role name (a String) from the Role object.
        // We assume your Role entity has a field named 'name' or 'roleName'. Let's use
        // 'name'.
        String roleName = roleObject.getName();

        // Null check for safety
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalStateException("User '" + email + "' has a role with no name.");
        }

        // Step 3: Use the roleName string to create the authority.
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}