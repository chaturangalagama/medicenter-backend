package com.ilt.cms.pm.business.visit.event;

import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.core.entity.visit.Priority;
import com.lippo.commons.util.CommonUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegistrationEvent implements Event {

    private String patientId;
    private String clinicId;
    private String caseId;
    private String preferredDoctorId;
    private List<AttachedMedicalCoverage> attachedMedicalCoverages;
    private String visitPurpose;
    private Priority priority;

    public PatientVisitRegistry convert() {
        return new PatientVisitRegistry(patientId, clinicId, preferredDoctorId, visitPurpose, priority, new MedicalReference(), null, null, null);
    }

    private Stream<AttachedMedicalCoverage> attachedMedicalCoverageMap() {
        return attachedMedicalCoverages.stream()
                .map(attachedMedicalCoverage -> new AttachedMedicalCoverage());
    }


    public boolean areParametersValid() {
        return CommonUtils.isStringValid(patientId, clinicId, visitPurpose);
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

    public List<AttachedMedicalCoverage> getAttachedMedicalCoverages() {
        return attachedMedicalCoverages;
    }

    public void setAttachedMedicalCoverages(List<AttachedMedicalCoverage> attachedMedicalCoverages) {
        this.attachedMedicalCoverages = attachedMedicalCoverages;
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

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    @Override
    public String toString() {
        return "RegistrationEvent{" +
                "patientId='" + patientId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", caseId='" + caseId + '\'' +
                ", preferredDoctorId='" + preferredDoctorId + '\'' +
                ", attachedMedicalCoverages=" + attachedMedicalCoverages +
                ", visitPurpose='" + visitPurpose + '\'' +
                ", priority=" + priority +
                '}';
    }
}
