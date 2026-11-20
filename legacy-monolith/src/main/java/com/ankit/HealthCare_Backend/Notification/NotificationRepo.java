package com.ankit.HealthCare_Backend.Notification;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {
    //finds All notification from db
    List<NotificationEntity> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    //count the undread Messages 
    long countByReceiverIdAndIsReadFalse(Long receiverId);
}