package com.ilt.cms.pm.rest.controller.clinic;

import com.ilt.cms.api.entity.notification.NotificationEntity;
import com.ilt.cms.downstream.clinic.NotificationApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
//@RolesAllowed("ROLE_NOTIFICATION_MANAGEMENT")
public class NotificationRestController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRestController.class);
    private NotificationApiService notificationApiService;

    public NotificationRestController(NotificationApiService notificationApiService) {
        this.notificationApiService = notificationApiService;
    }

    @PostMapping("/list/{listAll}")
    public HttpEntity<ApiResponse> listNotification(/*@PathVariable("name") String name,*/ @PathVariable("listAll") boolean listAll) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = "";//auth.getName(); //get logged in username
//        logger.trace("Listing for [{}]", name);
        ResponseEntity<ApiResponse> serviceResponse = notificationApiService.listNotification(name, listAll);
        return serviceResponse;
    }

    @PostMapping("/mark-read/{notificationId}")
    public HttpEntity<ApiResponse> markNotification(@PathVariable("notificationId") String notificationId) {
        logger.info("Marking notification [" + notificationId + "]");
        ResponseEntity<ApiResponse> serviceResponse = notificationApiService.markNotification(notificationId);
        return serviceResponse;
    }

    @PostMapping("/create")
    public HttpEntity<ApiResponse> createNotification(@RequestBody NotificationEntity notificationEntity) {
        logger.info("Creating new notification [" + notificationEntity + "]");
        ResponseEntity<ApiResponse> serviceResponse = notificationApiService.createNotification(notificationEntity);
        return serviceResponse;
    }
}
