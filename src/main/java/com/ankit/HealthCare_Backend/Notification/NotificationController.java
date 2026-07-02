package com.ankit.HealthCare_Backend.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.*;

@Tag(name = "Notifications", description = "Notification operations for patients and doctors — requires JWT token")
@SecurityRequirement(name = "Bearer Auth")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Operation(summary = "Get my notifications", description = "Returns all notifications for the logged-in user (patient or doctor), newest first")
    @ApiResponse(responseCode = "200", description = "Notification list returned")
    @GetMapping("/my")
    public ResponseEntity<List<NotificationEntity>> getMyNotification() {
        List<NotificationEntity> notifications = notificationService.getMyNotification();
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Get unread notification count", description = "Returns the count of unread notifications for the logged-in user — used for the bell badge")
    @ApiResponse(responseCode = "200", description = "Unread count returned")
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadNotificationCount() {
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount());
    }

    @Operation(summary = "Mark a notification as read", description = "Marks a single notification as read by its ID")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    @PutMapping("/{id}/read")
    public ResponseEntity<String> markRead(
            @Parameter(description = "Notification ID", required = true, example = "1")
            @PathVariable("id") Long notificationId) {
        String msg = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(msg);
    }

    @Operation(summary = "Mark all notifications as read", description = "Marks all unread notifications as read for the logged-in user")
    @ApiResponse(responseCode = "200", description = "All notifications marked as read")
    @PutMapping("/mark-all-read")
    public ResponseEntity<String> markAllRead() {
        String msg = notificationService.markAllAsRead();
        return ResponseEntity.ok(msg);
    }
}
