package com.ilt.cms.core.entity.visit;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.file.FileMetaData;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.lippo.commons.util.CommonUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientVisitRegistry extends PersistedObject {


    public enum PatientVisitState {
        INITIAL, CONSULT, POST_CONSULT, PAYMENT, COMPLETE
    }

    private String visitNumber;
    private String patientId;
    private String clinicId;
    private String preferredDoctorId;
    private String visitPurpose;
    private Priority priority;
    private MedicalReference medicalReference;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private PatientVisitState visitStatus;
    private PatientQueue patientQueue;
    private String remark;


    private List<FileMetaData> fileMetaData = new ArrayList<>();

    public PatientVisitRegistry() {
    }

    public PatientVisitRegistry(String patientId, String clinicId, String preferredDoctorId, String visitPurpose, Priority priority,
                                MedicalReference medicalReference, LocalDateTime startTime, LocalDateTime endTime,
                                String remark) {
        this.patientId = patientId;
        this.clinicId = clinicId;
        this.preferredDoctorId = preferredDoctorId;
        this.visitPurpose = visitPurpose;
        this.priority = priority;
        this.medicalReference = medicalReference;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remark = remark;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(patientId, clinicId) && priority != null;
    }

    public PatientVisitRegistry copy(PatientVisitRegistry registry) {
        patientId = registry.patientId;
        clinicId = registry.clinicId;
        preferredDoctorId = registry.preferredDoctorId;
        visitPurpose = registry.visitPurpose;
        priority = registry.priority;
        remark = registry.remark;
        return this;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getPreferredDoctorId() {
        return preferredDoctorId;
    }

    public void setPreferredDoctorId(String preferredDoctorId) {
        this.preferredDoctorId = preferredDoctorId;
    }

    public String getVisitPurpose() {
        return visitPurpose;
    }

    public void setVisitPurpose(String visitPurpose) {
        this.visitPurpose = visitPurpose;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public MedicalReference getMedicalReference() {
        if (medicalReference == null) medicalReference = new MedicalReference();
        return medicalReference;
    }

    public void setMedicalReference(MedicalReference medicalReference) {
        this.medicalReference = medicalReference;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public PatientVisitState getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(PatientVisitState visitStatus) {
        this.visitStatus = visitStatus;
    }

    public List<FileMetaData> getFileMetaData() {
        return fileMetaData;
    }

    public void setFileMetaData(List<FileMetaData> fileMetaData) {
        this.fileMetaData = fileMetaData;
    }

    public PatientQueue getPatientQueue() {
        return patientQueue;
    }

    public void setPatientQueue(PatientQueue patientQueue) {
        this.patientQueue = patientQueue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PatientVisitRegistry{" +
                "patientId='" + patientId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", preferredDoctorId='" + preferredDoctorId + '\'' +
                ", visitPurpose='" + visitPurpose + '\'' +
                ", priority=" + priority +
                ", medicalReferenceEntity=" + medicalReference +
                ", patientQueue=" + patientQueue +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public static class PatientQueue {
        private long queueNumber;
        private boolean urgent;
        private boolean patientCalled;

        public PatientQueue() {
        }

        public PatientQueue(long queueNumber, boolean urgent) {
            this.queueNumber = queueNumber;
            this.urgent = urgent;
        }

        public boolean isPatientCalled() {
            return patientCalled;
        }

        public void setPatientCalled(boolean patientCalled) {
            this.patientCalled = patientCalled;
        }

        public long getQueueNumber() {
            return queueNumber;
        }

        public boolean isUrgent() {
            return urgent;
        }

        public void setUrgent(boolean urgent) {
            this.urgent = urgent;
        }
    }
}
