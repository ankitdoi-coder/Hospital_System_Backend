// FileName: WebConfig.java CORS-Cross Origin Resource Sharing
package com.ankit.HealthCare_Backend.authentication.security; // Create a 'config' package if you don't have one

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Serve uploaded profile pictures from the uploads directory
        registry.addResourceHandler("/uploads/profile-pictures/**")
                .addResourceLocations("file:uploads/profile-pictures/")
                .setCachePeriod(3600); // Cache for 1 hour
    }
}