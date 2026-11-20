package com.ankit.HealthCare_Backend.shared.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Backend is running!";
    }
    
    @GetMapping("/oauth2/test")
    public String oauthTest() {
        return "OAuth2 endpoints are accessible!";
    }
    
    @GetMapping("/debug/endpoints")
    public String debugEndpoints() {
        return "Available endpoints: /test, /oauth2/test, /oauth2/authorization/google, /api/auth/**";
    }
}