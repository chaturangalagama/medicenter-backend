package com.ilt.cms.db.service.clinic;

import com.ilt.cms.core.entity.notification.Notification;
import com.ilt.cms.database.clinic.NotificationDatabaseService;
import com.ilt.cms.repository.clinic.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class MongoNotificationDatabaseService implements NotificationDatabaseService {
    private NotificationRepository notificationRepository;

    public MongoNotificationDatabaseService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> findAllByUsername(String username) {
        return notificationRepository.findAllByUsername(username);
    }

    @Override
    public List<Notification> findAllByUsernameAndRead(String username, boolean isRead) {
        return notificationRepository.findAllByUsernameAndRead(username, isRead);
    }

    @Override
    public Notification findOne(String notificationId) {
        return notificationRepository.findById(notificationId).orElse(null);
    }

    @Override
    public Notification save(Notification savedVersion) {
        return notificationRepository.save(savedVersion);
    }

    @Override
    public boolean deleteAllByLastModifiedDateLessThanEqual(Date from) {
        notificationRepository.deleteAllByLastModifiedDateLessThanEqual(from);
        return true;
    }
}
