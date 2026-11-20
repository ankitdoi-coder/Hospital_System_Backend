package com.ankit.HealthCare_Backend.authentication.security;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2Config {

    //this repo is used to find all Oauth registeration provider in our app
    //like if we have meta login, google,github toh ye ffind karegi ki kahan hai apna client
    private final ClientRegistrationRepository clientRegistrationRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void checkOAuth2Configuration() {
        try {
            var googleRegistration = clientRegistrationRepository.findByRegistrationId("google");
            if (googleRegistration != null) {
                log.info("✅ Google OAuth2 client registration found");
                log.info("Client ID: {}", googleRegistration.getClientId());
                log.info("Redirect URI: {}", googleRegistration.getRedirectUri());
            } else {
                log.error("❌ Google OAuth2 client registration NOT found");
            }
        } catch (Exception e) {
            log.error("❌ Error checking OAuth2 configuration: {}", e.getMessage());
        }
    }
}