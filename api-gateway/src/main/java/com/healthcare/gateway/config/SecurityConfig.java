package com.healthcare.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                // Public: auth endpoints, health checks, AI (optional: can be secured too)
                .pathMatchers(
                    "/api/auth/**",
                    "/actuator/**",
                    "/api/ai/**"
                ).permitAll()
                // Everything else requires a valid JWT (enforced by JwtAuthFilter on routes)
                .anyExchange().permitAll()  // Route-level filter handles JWT; Spring Security stays stateless
            )
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        return http.build();
    }
}
