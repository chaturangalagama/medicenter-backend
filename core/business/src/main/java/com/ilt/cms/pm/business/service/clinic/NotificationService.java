package com.ilt.cms.pm.business.service.clinic;

import com.ilt.cms.core.entity.notification.Notification;
import com.ilt.cms.database.clinic.NotificationDatabaseService;
import com.lippo.cms.exception.NotificationException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private NotificationDatabaseService notificationDatabaseService;

    public NotificationService(NotificationDatabaseService notificationDatabaseService) {
        this.notificationDatabaseService = notificationDatabaseService;
    }


    /**
     * Clear the notification at 5 AM
     */
    @Scheduled(cron = "0 0 5 * * *")
    public void cleanAllData() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(7);
        Date from = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        logger.debug("Clearing notification store data before [" + from + "]");
        notificationDatabaseService.deleteAllByLastModifiedDateLessThanEqual(from);
    }

    /**
     * Returns the notification for the given user
     *
     * @param username
     * @param all      if true returns all notification if false returns the unread notifications
     * @return
     */
    public List<Notification> listNotification(String username, boolean all) {
        logger.trace("listing all notification for [{}] all[{}]", username, all);
        List<Notification> notifications;
        if (all) {
            notifications = notificationDatabaseService.findAllByUsername(username);
        } else {
            notifications = notificationDatabaseService.findAllByUsernameAndRead(username, false);
        }
        logger.trace("Found [{}]", notifications.size());
        return notifications;
    }

    public Notification markNotificationAsReady(String notificationId) throws NotificationException {
        logger.info("marking notification [" + notificationId + "] as read");
        Notification savedVersion = notificationDatabaseService.findOne(notificationId);
        if (savedVersion == null) {
            logger.error("Invalid notification id received");
            //return new CmsServiceResponse<>(StatusCode.E2000, "Invalid notfication id");
            throw new NotificationException(StatusCode.E2000, "Invalid notfication id");
        } else {
            savedVersion.setRead(true);
            Notification updatedVersion = notificationDatabaseService.save(savedVersion);
            return updatedVersion;
        }
    }

    public Notification createNotification(Notification notification) throws NotificationException {
        logger.info("creating new notification [" + notification + "]");
        boolean areParametersValid = notification.areParametersValid();

        if (!areParametersValid) {
            //return new CmsServiceResponse<>(StatusCode.E1002);
            throw new NotificationException(StatusCode.E1002);
        } else {
            logger.info("saving notification");
            Notification save = notificationDatabaseService.save(notification);
            return save;
        }
    }
}
