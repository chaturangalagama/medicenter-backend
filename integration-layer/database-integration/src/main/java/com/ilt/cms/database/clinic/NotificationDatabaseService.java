package com.ilt.cms.database.clinic;

import com.ilt.cms.core.entity.notification.Notification;

import java.util.Date;
import java.util.List;

public interface NotificationDatabaseService {

    List<Notification> findAllByUsername(String username);

    List<Notification> findAllByUsernameAndRead(String username, boolean isRead);

    Notification findOne(String notificationId);

    Notification save(Notification savedVersion);

    boolean deleteAllByLastModifiedDateLessThanEqual(Date from);
}
