package com.ankit.HealthCare_Backend.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {

}