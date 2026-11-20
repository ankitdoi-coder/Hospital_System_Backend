### Feature Scope

#### MVP Features (Must-Haves for October 20th)

*   **Authentication & Authorization**: Secure user registration and login system using email/password. Implement JSON Web Tokens (JWT) for authenticating API requests and role-based access control (e.g., a patient cannot access the doctor's dashboard).
    
*   **Appointment Booking**: Patients can view doctors' available time slots and book an appointment. The system must prevent double booking. Doctors can view and manage their scheduled appointments.
    
*   **Patient Medical Records**: A basic system for doctors to write and save prescriptions linked to a patient's profile. Patients can view their own prescription history.
    
*   **Doctor Dashboard**: A central view for doctors to see their daily/weekly appointments and access patient information for consultations.
    
*   **Admin Dashboard**: A control panel for administrators to manage doctor and patient accounts and oversee billing entries.
    

#### Bonus Features (Nice-to-Haves)

*   **File Uploads**: Patients can upload medical reports (e.g., lab results, scans) to their profile.
    
*   **Analytics**: Simple charts on the Admin dashboard showing metrics like average patient wait time, number of appointments per doctor, or most common diagnoses.
    
*   **Payment Integration**: Integrate a payment gateway like Razorpay or Stripe to handle consultation fees.