package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.notification.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByUsername(String username);

    List<Notification> findAllByUsernameAndRead(String username, boolean read);

    void deleteAllByLastModifiedDateLessThanEqual(Date lastModifiedDate);
}
