package com.ankit.HealthCare_Backend.communication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ankit.HealthCare_Backend.communication.entity.ContactUs;
import com.ankit.HealthCare_Backend.communication.repository.ContactUsRepository;

@Tag(name = "Contact", description = "Contact Us form submission — no authentication required")
@RestController
@RequestMapping("/api/contact")
public class ContactUsController {

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Operation(summary = "Submit contact form", description = "Saves a contact form submission from the public contact page. No authentication required")
    @ApiResponse(responseCode = "200", description = "Contact form submitted successfully")
    @PostMapping
    public ResponseEntity<ContactUs> submitContactForm(@RequestBody ContactUs contactUs) {
        ContactUs saved = contactUsRepository.save(contactUs);
        return ResponseEntity.ok(saved);
    }
}
