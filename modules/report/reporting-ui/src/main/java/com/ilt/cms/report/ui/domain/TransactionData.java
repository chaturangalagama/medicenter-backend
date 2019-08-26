package com.ilt.cms.report.ui.domain;

public class TransactionData {
    private String transactionId;
    private String msisdn;
    private String sessionId;
    private String systemId;
    private String appId;
    private String menuClassification;
    private String menuDescription;
    private String category;
    private String userLanguage;
    private String ussdTransactionType;
    private String userMessageSentTime;
    private String userResponseReceivedTime;
    private String sessionStartDateTime;
    private String sessionEndDateTime;
    private String userResponseDuration;
    private String sessionDuration;
    private String serviceCode;
    private String completePath;
    private String chargedAmount;
    private String location;
    private String systemStatusCode;
    private String systemStatusDescription;
    private String ussdStatusCode;
    private String ussdStatusDescription;
    private String smsStatusCode;
    private String smsStatusDescription;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMenuClassification() {
        return menuClassification;
    }

    public void setMenuClassification(String menuClassification) {
        this.menuClassification = menuClassification;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public String getUssdTransactionType() {
        return ussdTransactionType;
    }

    public void setUssdTransactionType(String ussdTransactionType) {
        this.ussdTransactionType = ussdTransactionType;
    }

    public String getUserMessageSentTime() {
        return userMessageSentTime;
    }

    public void setUserMessageSentTime(String userMessageSentTime) {
        this.userMessageSentTime = userMessageSentTime;
    }

    public String getUserResponseReceivedTime() {
        return userResponseReceivedTime;
    }

    public void setUserResponseReceivedTime(String userResponseReceivedTime) {
        this.userResponseReceivedTime = userResponseReceivedTime;
    }

    public String getSessionStartDateTime() {
        return sessionStartDateTime;
    }

    public void setSessionStartDateTime(String sessionStartDateTime) {
        this.sessionStartDateTime = sessionStartDateTime;
    }

    public String getSessionEndDateTime() {
        return sessionEndDateTime;
    }

    public void setSessionEndDateTime(String sessionEndDateTime) {
        this.sessionEndDateTime = sessionEndDateTime;
    }

    public String getUserResponseDuration() {
        return userResponseDuration;
    }

    public void setUserResponseDuration(String userResponseDuration) {
        this.userResponseDuration = userResponseDuration;
    }

    public String getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(String sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCompletePath() {
        return completePath;
    }

    public void setCompletePath(String completePath) {
        this.completePath = completePath;
    }

    public String getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(String chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSystemStatusCode() {
        return systemStatusCode;
    }

    public void setSystemStatusCode(String systemStatusCode) {
        this.systemStatusCode = systemStatusCode;
    }

    public String getSystemStatusDescription() {
        return systemStatusDescription;
    }

    public void setSystemStatusDescription(String systemStatusDescription) {
        this.systemStatusDescription = systemStatusDescription;
    }

    public String getUssdStatusCode() {
        return ussdStatusCode;
    }

    public void setUssdStatusCode(String ussdStatusCode) {
        this.ussdStatusCode = ussdStatusCode;
    }

    public String getUssdStatusDescription() {
        return ussdStatusDescription;
    }

    public void setUssdStatusDescription(String ussdStatusDescription) {
        this.ussdStatusDescription = ussdStatusDescription;
    }

    public String getSmsStatusCode() {
        return smsStatusCode;
    }

    public void setSmsStatusCode(String smsStatusCode) {
        this.smsStatusCode = smsStatusCode;
    }

    public String getSmsStatusDescription() {
        return smsStatusDescription;
    }

    public void setSmsStatusDescription(String smsStatusDescription) {
        this.smsStatusDescription = smsStatusDescription;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TransactionData{");
        sb.append("transactionId=").append(transactionId);
        sb.append(", msisdn='").append(msisdn).append('\'');
        sb.append(", sessionId=").append(sessionId);
        sb.append(", systemId=").append(systemId);
        sb.append(", appId=").append(appId);
        sb.append(", menuClassification='").append(menuClassification).append('\'');
        sb.append(", menuDescription='").append(menuDescription).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", userLanguage='").append(userLanguage).append('\'');
        sb.append(", ussdTransactionType='").append(ussdTransactionType).append('\'');
        sb.append(", userMessageSentTime='").append(userMessageSentTime).append('\'');
        sb.append(", userResponseReceivedTime='").append(userResponseReceivedTime).append('\'');
        sb.append(", sessionStartDateTime=").append(sessionStartDateTime);
        sb.append(", sessionEndDateTime=").append(sessionEndDateTime);
        sb.append(", userResponseDuration='").append(userResponseDuration).append('\'');
        sb.append(", sessionDuration='").append(sessionDuration).append('\'');
        sb.append(", serviceCode='").append(serviceCode).append('\'');
        sb.append(", completePath='").append(completePath).append('\'');
        sb.append(", chargedAmount='").append(chargedAmount).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", systemStatusCode='").append(systemStatusCode).append('\'');
        sb.append(", systemStatusDescription='").append(systemStatusDescription).append('\'');
        sb.append(", ussdStatusCode='").append(ussdStatusCode).append('\'');
        sb.append(", ussdStatusDescription='").append(ussdStatusDescription).append('\'');
        sb.append(", smsStatusCode='").append(smsStatusCode).append('\'');
        sb.append(", smsStatusDescription='").append(smsStatusDescription).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
