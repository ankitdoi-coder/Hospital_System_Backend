package com.ankit.HealthCare_Backend.Audits;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || 
            !authentication.isAuthenticated() || 
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("SYSTEM"); // For operations done without logging in (e.g., self-registration)
        }

        // Returns the email/username of the currently logged-in user
        return Optional.of(authentication.getName()); 
    }
}