package com.ankit.HealthCare_Backend.usermanagement.doctor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.appointment.dto.UpdateStatusDTO;
import com.ankit.HealthCare_Backend.appointment.entity.Appointment;
import com.ankit.HealthCare_Backend.appointment.repository.AppointmentRepository;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.prescription.entity.Prescription;
import com.ankit.HealthCare_Backend.prescription.repository.PrescriptionRepository;
import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.doctor.repository.DoctorRepository;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.Exception.ResourceNotFoundException;
import com.ankit.HealthCare_Backend.Exception.UnauthorizedException;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;
import com.ankit.HealthCare_Backend.Notification.NotificationService;
import com.ankit.HealthCare_Backend.Notification.NotificationType;


@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private AppointmentRepository appointmentRepo;
    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private NotificationService notiService;
    @Autowired
    private JavaMailSender mailSender;


    //Get upcoming appoinments of doctor
    @Override
    public DoctorDTO getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) throw new ResourceNotFoundException("User not found: " + email);

        Doctor doctor = doctorRepo.findByUserId(user.getId());
        if (doctor == null) throw new ResourceNotFoundException("Doctor profile not found for: " + email);

        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setExperience(doctor.getExperience());
        dto.setApproved(doctor.isApproved());
        dto.setEmail(email);
        return dto;
    }

    //Get upcoming appoinments of doctor
    @Override
    public List<AppointmentDTO> myUpcomingAppointments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();

        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with Email " + email);
        }

        Doctor doctor = doctorRepo.findByUserId(user.getId());
        if (doctor == null) {
            throw new ResourceNotFoundException("No doctor profile found for user: " + email);
        }

        Long doctorId = doctor.getId();
        List<Appointment> appointments = appointmentRepo.findByDoctorId(doctorId);

        return appointments.stream()
                .map(appointment -> convertToAppointmentDto(appointment))
                .collect(Collectors.toList());
    }

    //Get all patients who have appointments with this doctor
    @Override
    public List<PatientDTO> getMyPatients() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with Email " + email);
        }

        Doctor doctor = doctorRepo.findByUserId(user.getId());
        if (doctor == null) {
            throw new ResourceNotFoundException("No doctor profile found for user: " + email);
        }

        Long doctorId = doctor.getId();
        List<Appointment> appointments = appointmentRepo.findByDoctorId(doctorId);

        return appointments.stream()
                .map(appointment -> appointment.getPatient())
                .distinct()
                .map(this::convertToPatientDto)
                .collect(Collectors.toList());
    }

    

    // createPrescription
    @Override
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        Appointment appointment = appointmentRepo.findById(prescriptionDTO.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + prescriptionDTO.getAppointmentId()));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDosages(prescriptionDTO.getDosages());
        prescription.setMedicationDetails(prescriptionDTO.getMedicationDetails());
        Prescription savedPrescription = prescriptionRepo.save(prescription);
        return convertToPrescriptionDto(savedPrescription);
    }

    // Helper method to convert the entity to a DTO
    private AppointmentDTO convertToAppointmentDto(Appointment appointment) {
    AppointmentDTO dto = new AppointmentDTO();
    dto.setId(appointment.getId());
    dto.setPatientId(appointment.getPatient().getId());
    dto.setPatientFirstName(appointment.getPatient().getFirstName());
    dto.setPatientLastName(appointment.getPatient().getLastName());
    dto.setDoctorId(appointment.getDoctor().getId());
    dto.setAppointmentDate(appointment.getAppointmentDate());
    dto.setAppointmentTime(appointment.getAppointmentTime());
    dto.setReasonForVisit(appointment.getReasonForVisit());
    dto.setStatus(appointment.getStatus());
    return dto;
}

    private PatientDTO convertToPatientDto(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDob(patient.getDob());
        dto.setContactNumber(patient.getContactNumber());
        // Set email if user exists
        if (patient.getUser() != null) {
            dto.setEmail(patient.getUser().getEmail());
        }
        return dto;
    }


    private PrescriptionDTO convertToPrescriptionDto(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setAppointmentId(prescription.getAppointment().getId());
        dto.setDosages(prescription.getDosages());
        dto.setMedicationDetails(prescription.getMedicationDetails());
        dto.setDoctorFirstName(prescription.getAppointment().getDoctor().getFirstName());
        dto.setDoctorLastName(prescription.getAppointment().getDoctor().getLastName());
        return dto;
    }


    //UPDATE THE appointment status
    @Override
    @Transactional
    public AppointmentDTO updateAppointmentStatus(Long id, UpdateStatusDTO status)  {
        Appointment appointment=appointmentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointment.setStatus(status.getStatus());
        Appointment savedAppointment = appointmentRepo.save(appointment); 

        // in-app notification to patient
        notiService.createNotification(
            appointment.getPatient().getUser().getId(),
            "Your appointment status has been updated to: " + status.getStatus() + " by Dr. " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
            appointment.getDoctor().getUser().getId(),
            NotificationType.APPOINTMENT);

        // email notification to patient
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(appointment.getPatient().getUser().getEmail());
        mail.setSubject("Appointment Status Update");
        mail.setText("Dear " + appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName()
                + ",\n\nYour appointment on " + appointment.getAppointmentDate()
                + " with Dr. " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
                + " has been updated to: " + status.getStatus()
                + ".\n\nPlease check your dashboard for more details.");
        mailSender.send(mail);

        return convertToAppointmentDto(savedAppointment);
    }



    @Override
    public List<PrescriptionDTO> getMyPrescriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with Email " + email);
        }

        Doctor doctor = doctorRepo.findByUserId(user.getId());
        if (doctor == null) {
            throw new ResourceNotFoundException("No doctor profile found for user: " + email);
        }

        Long doctorId = doctor.getId();
        List<Appointment> doctorAppointments = appointmentRepo.findByDoctorId(doctorId);
        
        return doctorAppointments.stream()
                .flatMap(appointment -> prescriptionRepo.findByAppointmentId(appointment.getId()).stream())
                .map(this::convertToPrescriptionDtoWithPatient)
                .collect(Collectors.toList());
    }

    private PrescriptionDTO convertToPrescriptionDtoWithPatient(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setAppointmentId(prescription.getAppointment().getId());
        dto.setPatientId(prescription.getAppointment().getPatient().getId());
        dto.setPatientFirstName(prescription.getAppointment().getPatient().getFirstName());
        dto.setPatientLastName(prescription.getAppointment().getPatient().getLastName());
        dto.setDoctorFirstName(prescription.getAppointment().getDoctor().getFirstName());
        dto.setDoctorLastName(prescription.getAppointment().getDoctor().getLastName());
        dto.setDosages(prescription.getDosages());
        dto.setMedicationDetails(prescription.getMedicationDetails());
        dto.setCreatedAt(prescription.getCreatedAt());
        return dto;
    }

    

    
}
