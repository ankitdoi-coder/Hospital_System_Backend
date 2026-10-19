package com.healthcare.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * User & Authentication Microservice Application
 * 
 * Manages user accounts, authentication (JWT), roles (Doctor, Patient, Admin),
 * and exposes REST endpoints for other services (e.g. Appointment Service via OpenFeign).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
