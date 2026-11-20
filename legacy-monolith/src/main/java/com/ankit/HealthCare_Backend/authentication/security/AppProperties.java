//contains secret key its like a drower for application.properties
package com.ankit.HealthCare_Backend.authentication.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {

    private final Jwt jwt = new Jwt();
    private final Admin admin = new Admin();

    
    // Nested classes for organization
    @Data
    public static class Jwt {
        private String secret;
        private long expirationMs = 86400000; // default 24h if not set in properties
    }

    //for admin
    @Data
    public static class Admin {
        private String email;
        private String password;

        
    }
}
