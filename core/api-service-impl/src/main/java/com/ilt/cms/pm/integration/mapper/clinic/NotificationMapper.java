package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.api.entity.notification.NotificationEntity;
import com.ilt.cms.core.entity.notification.Notification;


public class NotificationMapper {
    public static NotificationEntity mapToEntity(Notification notification) {
        if(notification == null){
            return null;
        }
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(notification.getId());
        notificationEntity.setCreatedDateTime(notification.getCreatedDateTime());
        notificationEntity.setUsername(notification.getUsername());
        if(notification.getAddedBy() != null) {
            notificationEntity.setAddedBy(NotificationEntity.AddedBy.valueOf(notification.getAddedBy().name()));
        }
        notificationEntity.setAdderId(notification.getAdderId());
        notificationEntity.setMessage(notification.getMessage());
        notificationEntity.setRead(notification.isRead());
        if(notification.getPriority() != null) {
            notificationEntity.setPriority(NotificationEntity.Priority.valueOf(notification.getPriority().name()));
        }

        return notificationEntity;
    }

    public static Notification mapToCore(NotificationEntity notificationEntity) {
        if(notificationEntity == null){
            return null;
        }
        Notification notification = new Notification(notificationEntity.getUsername(),
        Notification.AddedBy.valueOf(notificationEntity.getAddedBy().name()), notificationEntity.getAdderId(),
        notificationEntity.getMessage(), Notification.Priority.valueOf(notificationEntity.getPriority().name()));
        notification.setId(notificationEntity.getId());
        return notification;
    }
}
