package com.ankit.HealthCare_Backend.authentication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.ankit.HealthCare_Backend.usermanagement.user.service.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // skip JWT validation for public endpoints
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // matches the authorization header with the token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Cuts off the "Bearer " part and keeps only the actual token.
        final String jwt = authHeader.substring(7);

        // The Bouncer checks the Banned List (Redis)
        if (jwtService.isTokenBlacklisted(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User is logged out / Token is blacklisted");
            return; // Stop the request right here. Do not pass to filterChain!
        }

        // Uses the JwtService to read the sub claim (username/email) from the token.
        final String userEmail = jwtService.extractUsername(jwt);

        // Two checks before authenticating
        // Is there a username inside the token?
        // Has Spring Security already authenticated this request? (avoid duplicate work)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Loads full user info (password hash, roles, authorities) from DB by email/username.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // User not found or token invalid - continue without authentication
                // This allows the request to proceed and be rejected by endpoint security
            }
        }
        
        filterChain.doFilter(request, response);
    }
}