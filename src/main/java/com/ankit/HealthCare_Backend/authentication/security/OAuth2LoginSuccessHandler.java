package com.ankit.HealthCare_Backend.authentication.security;

import com.ankit.HealthCare_Backend.authentication.security.JwtService;
import com.ankit.HealthCare_Backend.core.entity.Role;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.usermanagement.patient.repository.PatientRepository;
import com.ankit.HealthCare_Backend.core.repository.RoleRepository;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository; // Inject PatientRepository

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            // Pass oauth2User to get potential name details
            user = createNewOAuthUserAndPatientProfile(email, oauth2User);
        }

        // --- JWT Generation and Redirect (remains the same) ---
         org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    "", // Password is not relevant for OAuth2 JWT generation
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()))
                );

        String jwt = jwtService.generateToken(userDetails);

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/api/auth/oauth2/callback")
                .queryParam("token", jwt)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User createNewOAuthUserAndPatientProfile(String email, OAuth2User oauth2User) {
        // --- Create User ---
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(""); // Consider hashing an empty string or a random secure value if your constraints require non-null/non-empty
        newUser.setApproved(true);

        // Fetch role by name for robustness
        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Error: PATIENT Role not found."));
        newUser.setRole(patientRole);
        User savedUser = userRepository.save(newUser); // Save User first to get ID

        // --- Create Patient Profile ---
        Patient newPatient = new Patient();
        newPatient.setUser(savedUser); // Link to the saved User

        // Get names from Google profile (attributes might vary slightly by provider)
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        // String pictureUrl = oauth2User.getAttribute("picture"); // Example: if you store picture

        newPatient.setFirstName(firstName != null ? firstName : "Unknown"); // Provide defaults if null
        newPatient.setLastName(lastName != null ? lastName : "User");

        // Set mandatory non-null fields for Patient (if any)
        // These might need default values or you might need a step
        // in your frontend for the user to complete their profile.
        newPatient.setContactNumber(0L); // Default/Placeholder - User should update this
        newPatient.setDob(LocalDate.now()); // Default/Placeholder - User should update this

        patientRepository.save(newPatient); // Save the Patient profile

        return savedUser; // Return the User entity
    }


}