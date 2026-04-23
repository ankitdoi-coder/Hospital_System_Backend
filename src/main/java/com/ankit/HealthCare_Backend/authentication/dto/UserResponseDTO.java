package com.ankit.HealthCare_Backend.authentication.dto;
import java.time.LocalDate; 
import lombok.Data;
@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String roleName;


     // New fields for Doctor/Patient profiles
    private String firstName;
    private String lastName;
    
    // Doctor-specific field (can be null for patients)
    private String specialty;
    
    // Patient-specific fields (can be null for doctors)
    private Long contactNumber;
    private LocalDate dob; 

    //message for the user that A Doctor will be login after approved by the admin
    //and for the patient it will be always approved
    private String message;
}
