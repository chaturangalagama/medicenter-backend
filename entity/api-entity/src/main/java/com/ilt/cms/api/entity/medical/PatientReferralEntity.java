package com.ilt.cms.api.entity.medical;

import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import com.ilt.cms.api.entity.consultation.MedicalCertificate;
import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientReferralEntity {

    private Map<String, Integer> planMaxUsage;
    private ConsultationEntity consultation;
    private List<String> diagnosisIds;
    private List<MedicalCertificate> medicalCertificates;
    private ConsultationFollowUp consultationFollowup;
    private PatientReferral patientReferral;
    private List<DispatchItemEntity> dispatchItemEntities;
}
