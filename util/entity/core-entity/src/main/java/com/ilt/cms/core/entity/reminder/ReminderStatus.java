package com.ilt.cms.core.entity.reminder;

import java.time.LocalDateTime;

public class ReminderStatus {

    private boolean reminderSent;
    private LocalDateTime reminderSentTime;
    private boolean sentSuccessfully;
    private String remark;
    private String externalReferenceNumber;

    public ReminderStatus() {
    }

    public ReminderStatus(boolean reminderSent, LocalDateTime reminderSentTime, boolean sentSuccessfully, String remark, String externalReferenceNumber) {
        this.reminderSent = reminderSent;
        this.reminderSentTime = reminderSentTime;
        this.sentSuccessfully = sentSuccessfully;
        this.remark = remark;
        this.externalReferenceNumber = externalReferenceNumber;
    }

    public boolean isSentSuccessfully() {
        return sentSuccessfully;
    }

    public String getRemark() {
        return remark;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public LocalDateTime getReminderSentTime() {
        return reminderSentTime;
    }

    public String getExternalReferenceNumber() {
        return externalReferenceNumber;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public void setReminderSentTime(LocalDateTime reminderSentTime) {
        this.reminderSentTime = reminderSentTime;
    }

    public void setSentSuccessfully(boolean sentSuccessfully) {
        this.sentSuccessfully = sentSuccessfully;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setExternalReferenceNumber(String externalReferenceNumber) {
        this.externalReferenceNumber = externalReferenceNumber;
    }

    @Override
    public String toString() {
        return "ReminderStatus{" +
                "reminderSent=" + reminderSent +
                ", reminderSentTime=" + reminderSentTime +
                ", sentSuccessfully=" + sentSuccessfully +
                ", remark='" + remark + '\'' +
                ", externalReferenceNumber='" + externalReferenceNumber + '\'' +
                '}';
    }
}
