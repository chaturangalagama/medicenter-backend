package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.notification.NotificationEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface NotificationDownstream {

    ResponseEntity<ApiResponse> listNotification(String name, boolean listAll);

    ResponseEntity<ApiResponse> markNotification(String notificationId);

    ResponseEntity<ApiResponse> createNotification(NotificationEntity notificationEntity);
}