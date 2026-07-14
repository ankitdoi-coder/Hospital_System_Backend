package com.ankit.HealthCare_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ankit.HealthCare_Backend.authentication.security.AppProperties;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class HealthCareBackendApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Starting Application... Checking for .env file");
        
        try {
            // ignoreIfMissing() hata diya taaki real error samne aaye
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            System.out.println("✅ SUCCESS: .env file properly load ho gayi hai!");
            
            dotenv.entries().forEach(e -> {
                if (System.getenv(e.getKey()) == null) {
                    System.setProperty(e.getKey(), e.getValue());
                }
            });
        } catch (Exception e) {
            System.out.println("❌ WARNING/ERROR: .env file NAHI MILI! Reason: " + e.getMessage());
        }

        SpringApplication.run(HealthCareBackendApplication.class, args);
    }
}