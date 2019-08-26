package com.ilt.cms.api.entity.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalAlertEntity {

    public enum AlertType {
        CHRONIC_DISEASE, MEDICAL_CONDITION, MEDICATION, ALLERGY, OTHER, REMINDERS
    }
    private String id;
    private String patientId;
    private List<MedicalAlertDetails> details = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class MedicalAlertDetails {

        private String alertId;
        private String alertType;
        private String name;
        private String remark;
        private Priority priority;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
        private Date addedDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
        private Date expiryDate;

        public MedicalAlertDetails() {
            priority = Priority.LOW;
        }

        public MedicalAlertDetails(AlertType alertType, String name, String remark, Priority priority, Date addedDate,
                                   Date expiryDate) {
            this();
            this.alertType = alertType.name();
            this.name = name;
            this.remark = remark;
            this.priority = priority;
            this.addedDate = addedDate;
            this.expiryDate = expiryDate;
        }


        public enum Priority {
            HIGH, LOW
        }
    }
}
