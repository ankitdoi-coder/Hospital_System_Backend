package com.ankit.HealthCare_Backend.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    NotificationService notificationService;

    //get My Notification 
    @GetMapping("/my")
    public ResponseEntity<List<NotificationEntity>> getMyNotification(){
        List<NotificationEntity> notifications=notificationService.getMyNotification();

        return ResponseEntity.ok(notifications);
    }

    //get unread Count
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadNotificationCount(){
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount());
    }

    //mark unread as Read
    @PutMapping("/{id}/read")
    public ResponseEntity<String> markRead(@PathVariable("id") Long notificationId){
        String msg=notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(msg);
    }

    //mark All As Read
    @PutMapping("/mark-all-read")
    public ResponseEntity<String> markAllRead(){
        String msg=notificationService.markAllAsRead();
        return ResponseEntity.ok(msg);
    }

}
