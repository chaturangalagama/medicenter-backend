package com.lippo.cms.inventory.model;

import java.sql.Date;

public class PatientRegister {
    private int patientRegisterId;
    private int patientId;
    private int clinicId;
    private Date dateIn;
    private String patientStatus;
    private Date createdAt;
    private int createdUserId;
    private Date updatedAt;
    private int updatedUserId;

    public PatientRegister() {}

    public PatientRegister(
        int patientRegisterId,
        int patientId,
        int clinicId,
        Date dateIn,
        String patientStatus,
        Date createdAt,
        int createdUserId,
        Date updatedAt,
        int updatedUserId
    ) {
        this.patientRegisterId = patientRegisterId;
        this.patientId = patientId;
        this.clinicId = clinicId;
        this.dateIn = dateIn;
        this.patientStatus = patientStatus;
        this.createdAt = createdAt;
        this.createdUserId  = createdUserId;
        this.updatedAt = updatedAt;
        this.updatedUserId = updatedUserId;
    }

    public int getPatientRegisterId() {
        return patientRegisterId;
    }
    public void setPatientRegisterId(int patientRegisterId) {
        this.patientRegisterId = patientRegisterId;
    }

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getClinicId() {
        return clinicId;
    }
    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public Date getDateIn() {
        return dateIn;
    }
    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public String getPatientStatus() {
        return patientStatus;
    }
    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }
    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUpdatedUserId() {
        return updatedUserId;
    }
    public void setUpdatedUserId(int updatedUserId) {
        this.updatedUserId = updatedUserId;
    }
}
