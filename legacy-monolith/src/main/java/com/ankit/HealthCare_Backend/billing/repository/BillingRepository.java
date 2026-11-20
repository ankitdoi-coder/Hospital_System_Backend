package com.ankit.HealthCare_Backend.billing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ankit.HealthCare_Backend.billing.entity.Billing;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;

import java.time.LocalDate;

@Repository
public interface BillingRepository extends JpaRepository<Billing,Long>{
 // solved n+1 query problem 
    @Query("SELECT b FROM Billing b JOIN FETCH b.appointment_id")
    List<Billing> findAllWithDetails();

    // Calculate daily revenue directly in DB
    @Query("SELECT SUM(b.amount) FROM Billing b WHERE b.billing_status = :status AND b.appointment_id.appointmentDate = :date")
    Integer calculateDailyRevenue(@Param("status") BillingStatus status, @Param("date") LocalDate date);

    // Calculate monthly revenue directly in DB
    @Query("SELECT SUM(b.amount) FROM Billing b WHERE b.billing_status = :status AND b.appointment_id.appointmentDate BETWEEN :startDate AND :endDate")
    Integer calculateMonthlyRevenue(@Param("status") BillingStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}