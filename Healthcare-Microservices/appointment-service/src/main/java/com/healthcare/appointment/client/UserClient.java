package com.healthcare.appointment.client;

import com.healthcare.appointment.dto.DoctorDTO;
import com.healthcare.appointment.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign Declarative REST Client
 * 
 * name = "user-service": Eureka service identifier.
 * Feign automatically resolves instances through Eureka and load-balances using Spring Cloud LoadBalancer!
 * fallback = UserClientFallback.class: If user-service is DOWN, this fallback is triggered!
 */
@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/patients/{id}")
    PatientDTO getPatientById(@PathVariable("id") Long id);

    @GetMapping("/api/users/doctors/{id}")
    DoctorDTO getDoctorById(@PathVariable("id") Long id);
}
