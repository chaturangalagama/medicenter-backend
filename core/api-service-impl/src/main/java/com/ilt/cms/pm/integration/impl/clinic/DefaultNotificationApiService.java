package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.api.entity.notification.NotificationEntity;
import com.ilt.cms.core.entity.notification.Notification;
import com.ilt.cms.downstream.clinic.NotificationApiService;
import com.ilt.cms.pm.business.service.clinic.NotificationService;
import com.ilt.cms.pm.integration.mapper.clinic.NotificationMapper;
import com.lippo.cms.exception.NotificationException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultNotificationApiService implements NotificationApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultNotificationApiService.class);
    private NotificationService notificationService;

    public DefaultNotificationApiService(NotificationService notificationService){
        this.notificationService = notificationService;
    }
    @Override
    public ResponseEntity<ApiResponse> listNotification(String name, boolean listAll) {

            List<Notification> notificationList = notificationService.listNotification(name, listAll);
            List<NotificationEntity> notificationEntityList = new ArrayList<NotificationEntity>();

            notificationList.stream().forEach(p-> notificationEntityList.add(NotificationMapper.mapToEntity(p)));
            return httpApiResponse(new HttpApiResponse(notificationEntityList));

    }

    @Override
    public ResponseEntity<ApiResponse> markNotification(String notificationId) {
        Notification notification = null;
        try {
            notification = notificationService.markNotificationAsReady(notificationId);
            return httpApiResponse(new HttpApiResponse(NotificationMapper.mapToEntity(notification)));
        } catch (NotificationException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> createNotification(NotificationEntity notificationEntity) {
        Notification notification = null;
        try {
            notification = notificationService.createNotification(NotificationMapper.mapToCore(notificationEntity));
            return httpApiResponse(new HttpApiResponse(NotificationMapper.mapToEntity(notification)));
        } catch (NotificationException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
}
