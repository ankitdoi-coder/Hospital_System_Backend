package com.ankit.HealthCare_Backend.Notification;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ankit.HealthCare_Backend.Notification.NotificationEntity;
import com.ankit.HealthCare_Backend.Notification.NotificationRepo;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.ankit.HealthCare_Backend.Exception.ResourceNotFoundException;


@Service
public class NotificationService{
    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserRepository userRepo;

    //create notificatiion
    public NotificationEntity createNotification(Long receiverId, String message,Long senderId,NotificationType type){
        NotificationEntity notification=new NotificationEntity();
        notification.setReceiverId(receiverId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setSenderId(senderId);
        notification.setType(type);
        return notificationRepo.save(notification);
    }

    //get My Notifications of current user
    public List<NotificationEntity> getMyNotification(){
        Long receiverId = getCurrentUserId();

        if(receiverId==null){
            throw new IllegalArgumentException("Receiver ID cannot be null");
        }else{
            return notificationRepo.findByReceiverIdOrderByCreatedAtDesc(receiverId);
        }
    }


    //getUnread Count
    public Long getUnreadNotificationCount(){
        return notificationRepo.countByReceiverIdAndIsReadFalse(getCurrentUserId());
    }

    //mark as Read
    public String markAsRead(Long notificationId){
        
        NotificationEntity notification=notificationRepo.findById(notificationId)
        .orElseThrow(()->new ResourceNotFoundException("Notification Not Found with id: "+notificationId));
        notification.setRead(true);
        notificationRepo.save(notification);
        return "Notification Read";
        
    }

    //mark All as Read
    public String markAllAsRead(){
        Long receiverId=getCurrentUserId();
        List<NotificationEntity> notifications=notificationRepo.findByReceiverIdOrderByCreatedAtDesc(receiverId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepo.saveAll(notifications);
        return "All read Success";
    }

    //helper to Get the Current User id 
    private Long getCurrentUserId() {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        User user=userRepo.findByEmail(auth.getName());
        if(user==null) throw new ResourceNotFoundException("User Not Found  ");
        return user.getId();
    }
}