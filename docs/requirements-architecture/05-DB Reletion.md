# Entity Definitions & Relationships
------

## User: Stores login credentials.

- id (Primary Key), email, password, role_id (Foreign Key to Role).

## Role: Defines user roles (PATIENT, DOCTOR, ADMIN).

- id (Primary Key), name.

## Patient: Stores patient-specific information.

- id (Primary Key), user_id (One-to-One with User), first_name, last_name, date_of_birth, contact_number.

## Doctor: Stores doctor-specific information.

- id (Primary Key), user_id (One-to-One with User), first_name, last_name, specialty, is_approved (by Admin).

## Appointment: Links a Patient and a Doctor for a specific time.

- id (Primary Key), patient_id (Many-to-One with Patient), doctor_id (Many-to-One with Doctor), appointment_date, status (e.g., SCHEDULED, COMPLETED, CANCELED).

## Prescription: Created by a Doctor for a Patient.

- id (Primary Key), appointment_id (Many-to-One with Appointment), medication_details, dosage.

## Billing: Records the financial transaction for an appointment.

- id (Primary Key), appointment_id (One-to-One with Appointment), amount, status (e.g., PAID, UNPAID).

## Relationships Summary
- A User has one Role.

- A User can be a Patient OR a Doctor (One-to-One).

- A Doctor can have many Appointments.

- A Patient can have many Appointments.

- An Appointment results in one or more Prescriptions.

- An Appointment has one Billing record.