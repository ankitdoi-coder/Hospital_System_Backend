package com.healthcare.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Appointment Service Application
 * 
 * @EnableDiscoveryClient: Registers this service with Eureka Server as 'APPOINTMENT-SERVICE'.
 * @EnableFeignClients: Enables declarative REST client scanning so we can call user-service.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AppointmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }
}
