package com.ilt.cms.core.entity.consultation;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Consultation extends PersistedObject {


    private String patientId;
    private String consultationNotes;
    private String memo;
    private String clinicNotes;
    private String doctorId;
    private String clinicId;

    private LocalDateTime consultationStartTime;
    private LocalDateTime consultationEndTime;

    public Consultation() {
    }

    public Consultation(String patientId, String doctorId, String clinicId, LocalDateTime consultationStartTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.consultationStartTime = consultationStartTime;
    }

    public void prepareForPersistent() {
        id = null;
        patientId = null;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(patientId, doctorId, clinicId);
    }

    public Consultation copy(Consultation consultation) {

        patientId = consultation.patientId;
        consultationNotes = consultation.consultationNotes;
        memo = consultation.memo;
        clinicNotes = consultation.clinicNotes;
        doctorId = consultation.doctorId;
        clinicId = consultation.clinicId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public LocalDateTime getConsultationStartTime() {
        return consultationStartTime;
    }

    public void setConsultationStartTime(LocalDateTime consultationStartTime) {
        this.consultationStartTime = consultationStartTime;
    }

    public LocalDateTime getConsultationEndTime() {
        return consultationEndTime;
    }

    public void setConsultationEndTime(LocalDateTime consultationEndTime) {
        this.consultationEndTime = consultationEndTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicNotes() {
        return clinicNotes;
    }

    public void setClinicNotes(String clinicNotes) {
        this.clinicNotes = clinicNotes;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "patientId='" + patientId + '\'' +
                ", consultationNotes='" + consultationNotes + '\'' +
                ", memo='" + memo + '\'' +
                ", clinicNotes='" + clinicNotes + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", consultationStartTime=" + consultationStartTime +
                ", consultationEndTime=" + consultationEndTime +
                '}';
    }

}
