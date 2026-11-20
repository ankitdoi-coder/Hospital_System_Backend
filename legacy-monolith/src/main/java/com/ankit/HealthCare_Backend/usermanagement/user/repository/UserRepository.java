package com.ankit.HealthCare_Backend.usermanagement.user.repository;

import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String Email);
    boolean existsByEmail(String  email);
}
