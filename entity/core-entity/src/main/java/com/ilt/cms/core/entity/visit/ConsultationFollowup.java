package com.ilt.cms.core.entity.visit;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.reminder.ReminderStatus;
import com.lippo.commons.util.CommonUtils;

import java.time.LocalDate;

public class ConsultationFollowup extends PersistedObject {

    private String patientId;
    private String patientVisitId;
    private String doctorId;
    private String clinicId;
    private LocalDate followupDate;
    private String remarks;
    private ReminderStatus reminderStatus;

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(remarks) && followupDate != null;
    }

    public ConsultationFollowup copyParameters(ConsultationFollowup followupConsultation) {
        setFollowupDate(followupConsultation.followupDate);
        setRemarks(followupConsultation.remarks);
        setReminderStatus(followupConsultation.reminderStatus);
        setPatientId(followupConsultation.patientId);
        setPatientVisitId(followupConsultation.patientVisitId);
        setDoctorId(doctorId);
        return this;
    }

    public LocalDate getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(LocalDate followupDate) {
        this.followupDate = followupDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ReminderStatus getReminderStatus() {
        return reminderStatus;
    }

    public void setReminderStatus(ReminderStatus reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientVisitId() {
        return patientVisitId;
    }

    public void setPatientVisitId(String patientVisitId) {
        this.patientVisitId = patientVisitId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


    @Override
    public String toString() {
        return "ConsultationFollowup{" +
                "patientId='" + patientId + '\'' +
                ", patientVisitId='" + patientVisitId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", followupDate=" + followupDate +
                ", remarks='" + remarks + '\'' +
                ", reminderStatus=" + reminderStatus +
                '}';
    }
}
