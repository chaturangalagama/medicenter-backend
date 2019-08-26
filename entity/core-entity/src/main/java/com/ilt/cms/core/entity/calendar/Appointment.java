package com.ilt.cms.core.entity.calendar;

import com.ilt.cms.core.entity.PersistedObject;

import java.time.LocalDateTime;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class Appointment extends PersistedObject {

    private String patientId;
    private String doctorId;
    private String clinicId;
    private String appointmentPurpose;
    private String remark;
    private LocalDateTime reminderDate;
    private LocalDateTime startDate;
    private int duration;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getAppointmentPurpose() {
        return appointmentPurpose;
    }

    public void setAppointmentPurpose(String appointmentPurpose) {
        this.appointmentPurpose = appointmentPurpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", appointmentPurpose='" + appointmentPurpose + '\'' +
                ", remark='" + remark + '\'' +
                ", reminderDate=" + reminderDate +
                ", startDate=" + startDate +
                ", duration=" + duration +
                '}';
    }
}
