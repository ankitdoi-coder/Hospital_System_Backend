package com.ankit.HealthCare_Backend.usermanagement.patient.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.appointment.entity.Appointment;
import com.ankit.HealthCare_Backend.appointment.repository.AppointmentRepository;
import com.ankit.HealthCare_Backend.billing.entity.Billing;
import com.ankit.HealthCare_Backend.billing.repository.BillingRepository;
import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.prescription.entity.Prescription;
import com.ankit.HealthCare_Backend.prescription.repository.PrescriptionRepository;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.doctor.repository.DoctorRepository;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.usermanagement.patient.repository.PatientRepository;
import com.ankit.HealthCare_Backend.Exception.ResourceNotFoundException;
import com.ankit.HealthCare_Backend.Exception.UnauthorizedException;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;
import com.ankit.HealthCare_Backend.notification.entity.NotificationEntity;


@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    DoctorRepository doctorRepo;

    @Autowired
    AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private BillingRepository billingRepo;





    //Get All Doctors
    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepo.findAll()
                .stream()
                .filter(Doctor::isApproved) // Only show approved doctors!
                .map(this::convertToDoctorDto)
                .collect(Collectors.toList());
    }

    // Convert Doctor Entity Data to DoctorDTO Data
    private DoctorDTO convertToDoctorDto(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setApproved(doctor.isApproved());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setExperience(doctor.getExperience());
        // Add other fields as needed
        return dto;
    }

    private AppointmentDTO convertToAppointmentDto(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorFirstName(appointment.getDoctor().getFirstName());
        dto.setDoctorLastName(appointment.getDoctor().getLastName());
        dto.setDoctorSpecialty(appointment.getDoctor().getSpecialty());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        
        // Get billing info
        Billing billing = billingRepo.findAll().stream()
                .filter(b -> b.getAppointment_id().getId().equals(appointment.getId()))
                .findFirst().orElse(null);
        
        if (billing != null) {
            dto.setBillingStatus(billing.getBilling_status());
            dto.setAmount(billing.getAmount());
        } else {
            dto.setBillingStatus(BillingStatus.UNPAID);
            dto.setAmount(500);
        }
        
        return dto;
    }





    // newAppointment
    @Override
    @Transactional
    public AppointmentDTO newAppointment(AppointmentDTO appointmentDTO, String patientEmail) {
        // 1. Find patient by user  email
        User user = userRepo.findByEmail(patientEmail);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + patientEmail);
        }
        
        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new ResourceNotFoundException("No patient profile found for user: " + patientEmail);
        }

        Doctor doctor = doctorRepo.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentDTO.getDoctorId()));

        // 2. Create the appointment entity
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setAppointmentDate(appointmentDTO.getAppointmentDate());

        //new Notification Entity to create new Notification
        NotificationEntity notification=new NotificationEntity();
        notification.setMessage("New appointment with "+ patient.getFirstName() + " "+patient.getLastName());
        notification.setSenderId(patient.getId());
        notification.setReceiverId(doctor.getId());
        notification.setRead(false);
        notification.setType(NotificationType.APPOINTMENT);

        newAppointment.setNotification(notification);
        newAppointment.setStatus(AppointmentStatusEnum.PENDING);

        // 3. Save the new appointment
        Appointment savedAppointment = appointmentRepo.save(newAppointment);

        // 4. Convert back to DTO
        return convertToAppointmentDto(savedAppointment);
    }




    // My appointments
    @Override
    public List<AppointmentDTO> getMyAppointments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();

        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + email);
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new ResourceNotFoundException("No patient profile found for user: " + email);
        }

        Long patientId = patient.getId();
        List<Appointment> appointments = appointmentRepo.findByPatientId(patientId);

        return appointments.stream()
                .map(appointment -> convertToAppointmentDto(appointment))
                .collect(Collectors.toList());
    }


    //get Prescription
    @Override
    public List<PrescriptionDTO> getMyPrescriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + email);
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new ResourceNotFoundException("No patient profile found for user: " + email);
        }

        List<Appointment> appointments = appointmentRepo.findByPatientId(patient.getId());
        return appointments.stream()
                .flatMap(appointment -> prescriptionRepo.findAll().stream()
                        .filter(prescription -> prescription.getAppointment().getId().equals(appointment.getId())))
                .map(this::convertToPrescriptionDto)
                .collect(Collectors.toList());
    }

    private PrescriptionDTO convertToPrescriptionDto(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setAppointmentId(prescription.getAppointment().getId());
        dto.setMedicationDetails(prescription.getMedicationDetails());
        dto.setDosages(prescription.getDosages());
        return dto;
    }



    //get profile info
    @Override
    public PatientDTO getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + email);
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new ResourceNotFoundException("No patient profile found for user: " + email);
        }

        return convertToPatientDto(patient);
    }




    //Make Payment 
    @Override
    @Transactional
    public void makePayment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        Billing billing = billingRepo.findAll().stream()
                .filter(b -> b.getAppointment_id().getId().equals(appointmentId))
                .findFirst().orElse(null);
        
        if (billing == null) {
            billing = new Billing();
            billing.setAppointment_id(appointment);
            billing.setAmount(500);
            billing.setBilling_status(BillingStatus.PAID);
        } else {
            billing.setBilling_status(BillingStatus.PAID);
        }
        
        billingRepo.save(billing);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        // Delete the appointment
        appointmentRepo.delete(appointment);
        
        // Also delete associated billing if exists
        billingRepo.findAll().stream()
                .filter(b -> b.getAppointment_id().getId().equals(appointmentId))
                .findFirst()
                .ifPresent(billingRepo::delete);
    }

    private PatientDTO convertToPatientDto(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setUserId(patient.getUser().getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setContactNumber(patient.getContactNumber());
        dto.setDob(patient.getDob());
        return dto;
    }

}
