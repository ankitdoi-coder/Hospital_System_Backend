package com.ankit.HealthCare_Backend.communication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.communication.entity.ContactUs;
import com.ankit.HealthCare_Backend.communication.repository.ContactUsRepository;

@RestController
@RequestMapping("/api/contact")
public class ContactUsController {

    @Autowired
    private ContactUsRepository contactUsRepository;

    @PostMapping
    public ResponseEntity<ContactUs> submitContactForm(@RequestBody ContactUs contactUs) {
        ContactUs saved = contactUsRepository.save(contactUs);
        return ResponseEntity.ok(saved);
    }
}
