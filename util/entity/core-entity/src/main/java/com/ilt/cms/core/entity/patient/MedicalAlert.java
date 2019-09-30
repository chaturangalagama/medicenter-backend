package com.ilt.cms.core.entity.patient;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ilt.cms.core.entity.patient.MedicalAlert.AlertType.REMINDERS;


public class MedicalAlert extends PersistedObject {

    public enum AlertType {
        CHRONIC_DISEASE, MEDICAL_CONDITION, MEDICATION, ALLERGY, OTHER, REMINDERS
    }

    @Indexed(unique = true)
    private String patientId;
    private List<MedicalAlertDetails> details = new ArrayList<>();

    public MedicalAlert() {
    }


    public MedicalAlert(String patientId) {
        this.patientId = patientId;
    }

    public void addAlert(MedicalAlertDetails medicalAlertDetails) {
        addAlert(medicalAlertDetails.alertType, medicalAlertDetails.name, medicalAlertDetails.remark,
                medicalAlertDetails.priority, medicalAlertDetails.addedDate, medicalAlertDetails.expiryDate,
                true);
    }

    public void addAlert(AlertType alertType, String name, String remark, MedicalAlertDetails.Priority priority,
                         Date addedDate, Date expiryDate, boolean validateExternalInput) {
        if (validateExternalInput && alertType == AlertType.ALLERGY || alertType == REMINDERS) {
            throw new RuntimeException("Type ALLERGY, REMINDERS is only used for listing");
        }
        if (validateExternalInput) {
            boolean alreadyContains = details.stream()
                    .anyMatch(mad -> mad.getName().equals(name) && mad.getAlertType() == alertType);
            if (alreadyContains) {
                throw new RuntimeException("Alert already assigned");
            }
        }
        MedicalAlertDetails details = new MedicalAlertDetails(alertType, name, remark, priority, addedDate, expiryDate);
        details.setAlertId(CommonUtils.idGenerator());
        this.details.add(details);
    }

    public static class MedicalAlertDetails {

        @Indexed
        private String alertId;
        private AlertType alertType;
        private String name;
        private String remark;
        private Priority priority;
        private Date addedDate;
        private Date expiryDate;

        public MedicalAlertDetails() {
            priority = Priority.LOW;
        }

        public MedicalAlertDetails(AlertType alertType, String name, String remark, Priority priority, Date addedDate,
                                   Date expiryDate) {
            this();
            this.alertType = alertType;
            this.name = name;
            this.remark = remark;
            this.priority = priority;
            this.addedDate = addedDate;
            this.expiryDate = expiryDate;
        }

        public boolean areParametersValid() {
            return CommonUtils.isStringValid(name) && alertType != null;
        }

        public enum Priority {
            HIGH, LOW
        }

        public AlertType getAlertType() {
            return alertType;
        }

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }

        public Date getAddedDate() {
            return addedDate;
        }

        public Priority getPriority() {
            return priority;
        }

        public Date getExpiryDate() {
            return expiryDate;
        }

        public String getAlertId()
        {
            return alertId;
        }

        public void setAlertId(String alertId)
        {
            this.alertId = alertId;
        }

        public void setAlertType(AlertType alertType) {
            this.alertType = alertType;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public void setAddedDate(Date addedDate) {
            this.addedDate = addedDate;
        }

        public void setExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
        }

        @Override
        public String toString() {
            return "MedicalAlertDetails{" +
                    "alertId='" + alertId + '\'' +
                    ", alertType=" + alertType +
                    ", name='" + name + '\'' +
                    ", remark='" + remark + '\'' +
                    ", priority=" + priority +
                    ", addedDate=" + addedDate +
                    ", expiryDate=" + expiryDate +
                    '}';
        }

        public void resetAddedDate() {
            addedDate = new Date();
        }
    }

    public String getPatientId() {
        return patientId;
    }

    public List<MedicalAlertDetails> getDetails()
    {
        return details;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setDetails(List<MedicalAlertDetails> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "MedicalAlert{" +
                "patientId='" + patientId + '\'' +
                ", details=" + details +
                '}';
    }
}
