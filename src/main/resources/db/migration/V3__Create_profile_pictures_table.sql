-- Create profile_pictures table for storing profile picture metadata
CREATE TABLE profile_pictures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL CHECK (user_type IN ('PATIENT', 'DOCTOR')),
    filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Ensure one profile picture per user
    UNIQUE KEY unique_user_profile (user_email, user_type),
    
    -- Index for faster lookups
    INDEX idx_user_email_type (user_email, user_type)
);

-- Add some sample data (optional)
-- INSERT INTO profile_pictures (user_email, user_type, filename, file_path) 
-- VALUES 
-- ('patient@example.com', 'PATIENT', 'sample-patient.jpg', 'uploads/profile-pictures/sample-patient.jpg'),
-- ('doctor@example.com', 'DOCTOR', 'sample-doctor.jpg', 'uploads/profile-pictures/sample-doctor.jpg');