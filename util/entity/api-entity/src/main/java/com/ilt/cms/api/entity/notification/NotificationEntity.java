package com.ilt.cms.api.entity.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationEntity {
    public enum Priority {
        HIGH, INFO, LOW
    }

    public enum AddedBy {
        SYSTEM, USER
    }
    private String id;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime createdDateTime;
    private String username;
    private AddedBy addedBy;
    private String adderId;
    private String message;
    private boolean read;
    private Priority priority;



    public NotificationEntity(String username, String message, String adderId) {
        this(username, AddedBy.SYSTEM, adderId, message, Priority.INFO);
    }

    /**
     * Created the object and sets current time as the created time.
     *
     * @param username
     * @param addedBy
     * @param adderId
     * @param message
     * @param priority
     */
    public NotificationEntity(String username, AddedBy addedBy, String adderId, String message, Priority priority) {
        this.username = username;
        this.addedBy = addedBy;
        this.adderId = adderId;
        this.message = message;
        this.priority = priority;
        this.createdDateTime = LocalDateTime.now();
    }
}
