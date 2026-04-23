package com.ankit.HealthCare_Backend.usermanagement.patient.dto;

import java.time.LocalDate;

// Using explicit getters/setters instead of Lombok if there are issues
public class PatientDTO {
    private Long  id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Long contactNumber;
    private LocalDate dob;
    private String email;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public Long getContactNumber() { return contactNumber; }
    public void setContactNumber(Long contactNumber) { this.contactNumber = contactNumber; }
    
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
