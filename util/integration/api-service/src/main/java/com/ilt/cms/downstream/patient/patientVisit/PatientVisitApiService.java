package com.ilt.cms.downstream.patient.patientVisit;

import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import com.ilt.cms.api.entity.consultation.MedicalCertificate;
import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.api.entity.medical.PatientReferralEntity;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PatientVisitApiService {

    ResponseEntity<ApiResponse> searchById(String visitId);

    ResponseEntity<ApiResponse> searchByVisitNumber(String visitNumber);

    ResponseEntity<ApiResponse> listPatientVisits(String patientId);

    ResponseEntity<ApiResponse> listPatientVisits(String patientId, int page, int size);

    ResponseEntity<ApiResponse> updateVisit(String visitID, VisitRegistryEntity visitEntity);

    ResponseEntity<ApiResponse> createVisit(String caseId, VisitRegistryEntity registryEntity, Boolean isSingleVisitCase);

    ResponseEntity<ApiResponse> createVisit(VisitRegistryEntity registryEntity, Boolean isSingleVisitCase);

    ResponseEntity<ApiResponse> listVisitsByClinicAndStartTime(String clinicId, LocalDateTime start, LocalDateTime end);

    ResponseEntity<ApiResponse> updateStatusToConsult(String visitId, String doctorId);

    ResponseEntity<ApiResponse> updateStatusToPostConsult(String visitId, PatientReferralEntity patientReferralEntity, Principal principal);

    ResponseEntity<ApiResponse> updateStatusToPayment(String visitId, PatientReferralEntity patientReferralEntity, Principal principal);

    ResponseEntity<ApiResponse> rollbackStatusToPostConsult(String visitId, Principal principal);

    ResponseEntity<ApiResponse> updateStatusToComplete(String visitId);

    ResponseEntity<ApiResponse> findDiagnosisDataByVisit(String visitId);

    ResponseEntity<ApiResponse> updateVisitConsultation(String visitId, ConsultationEntity consultationEntity, List<String> diagnosisIds, Principal principal);

    ResponseEntity<ApiResponse> updateVisitPatientReferral(String visitId, PatientReferral patientReferral, Principal principal);

    ResponseEntity<ApiResponse> updateVisitConsultationFollowup(String visitId, ConsultationFollowUp followUp, Principal principal);

    ResponseEntity<ApiResponse> updateVisitMedicalCertificates(String visitId, List<MedicalCertificate> certificates, Principal principal);

    ResponseEntity<ApiResponse> saveConsultationData(String visitId, PatientReferralEntity patientReferralEntity, Principal principal);

    ResponseEntity<ApiResponse> saveDispensingData(String visitId, PatientReferralEntity patientReferralEntity, Principal principal);

    ResponseEntity<ApiResponse> searchVisitByClinicQueue(String clinicId);

    ResponseEntity<ApiResponse> callNextPatient(String clinicId, String doctorId);

    ResponseEntity<ApiResponse> listFollowups(Principal principal, String clinicId, LocalDate startDate, LocalDate endDate);
}
