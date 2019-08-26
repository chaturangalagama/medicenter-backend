package com.ilt.cms.core.entity.notification;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;

import java.time.LocalDateTime;

public class Notification extends PersistedObject {

    public enum Priority {
        HIGH, INFO, LOW
    }

    public enum AddedBy {
        SYSTEM, USER
    }

    private LocalDateTime createdDateTime;
    private String username;
    private AddedBy addedBy;
    private String adderId;
    private String message;
    private boolean read;
    private Priority priority;

    public Notification() {
    }


    public Notification(String username, String message, String adderId) {
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
    public Notification(String username, AddedBy addedBy, String adderId, String message, Priority priority) {
        this.username = username;
        this.addedBy = addedBy;
        this.adderId = adderId;
        this.message = message;
        this.priority = priority;
        this.createdDateTime = LocalDateTime.now();
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(username, message) && priority != null;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Priority getPriority() {
        return priority;
    }

    public AddedBy getAddedBy() {
        return addedBy;
    }

    public String getAdderId() {
        return adderId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "createdDateTime=" + createdDateTime +
                ", username='" + username + '\'' +
                ", addedBy=" + addedBy +
                ", adderId='" + adderId + '\'' +
                ", message='" + message + '\'' +
                ", read=" + read +
                ", priority=" + priority +
                '}';
    }
}
