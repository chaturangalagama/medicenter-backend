package com.ilt.cms.core.entity.medical;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.core.entity.consultation.MedicalCertificate;
import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

public class MedicalReference extends PersistedObject {

    @DBRef
    private Consultation consultation;
    private List<String> diagnosisIds;
    private List<MedicalCertificate> medicalCertificates;
    @DBRef
    private ConsultationFollowup consultationFollowup;
    //Dispenced List; Put items here with different instructions
    @DBRef
    private PatientReferral patientReferral;
    private List<DispatchItem> dispatchItems;


    public boolean areParameterValid() {
        return diagnosisIds != null && dispatchItems.stream().allMatch(DispatchItem::areParameterValid);
    }

    public void copyNonReferanceFields(MedicalReference reference) {
        if (reference.diagnosisIds != null) {
            diagnosisIds = new ArrayList<>(reference.diagnosisIds);
        }
        if (reference.medicalCertificates != null) {
            medicalCertificates = new ArrayList<>(reference.medicalCertificates);
        }
        if (reference.dispatchItems != null) {
            dispatchItems = new ArrayList<>(reference.dispatchItems);
        }
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public List<String> getDiagnosisIds() {
        return diagnosisIds;
    }

    public void setDiagnosisIds(List<String> diagnosisIds) {
        this.diagnosisIds = diagnosisIds;
    }

    public List<MedicalCertificate> getMedicalCertificates() {
        if (medicalCertificates == null) medicalCertificates = new ArrayList<>();
        return medicalCertificates;
    }

    public void setMedicalCertificates(List<MedicalCertificate> medicalCertificates) {
        this.medicalCertificates = medicalCertificates;
    }

    public ConsultationFollowup getConsultationFollowup() {
        return consultationFollowup;
    }

    public void setConsultationFollowup(ConsultationFollowup consultationFollowup) {
        this.consultationFollowup = consultationFollowup;
    }

    public PatientReferral getPatientReferral() {
        return patientReferral;
    }

    public void setPatientReferral(PatientReferral patientReferral) {
        this.patientReferral = patientReferral;
    }

    public List<DispatchItem> getDispatchItems() {
        if (dispatchItems == null) return new ArrayList<>();
        return dispatchItems;
    }

    public void setDispatchItems(List<DispatchItem> dispatchItems) {
        this.dispatchItems = dispatchItems;
    }

    @Override
    public String toString() {
        return "MedicalReference{" +
                "consultation=" + consultation +
                ", diagnosisIds=" + diagnosisIds +
                ", medicalCertificates=" + medicalCertificates +
                ", consultationFollowup=" + consultationFollowup +
                ", patientReferral=" + patientReferral +
                '}';
    }
}
