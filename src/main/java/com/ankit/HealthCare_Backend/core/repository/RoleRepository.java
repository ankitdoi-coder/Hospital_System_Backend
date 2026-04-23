package com.ankit.HealthCare_Backend.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankit.HealthCare_Backend.core.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    
    Optional<Role> findByName(String name);
}
