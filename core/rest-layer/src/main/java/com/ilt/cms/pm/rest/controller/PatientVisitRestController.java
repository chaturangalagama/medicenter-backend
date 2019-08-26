package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.container.ConsultationWrapper;
import com.ilt.cms.api.container.VisitRegistryWrapper;
import com.ilt.cms.api.entity.consultation.MedicalCertificate;
import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.api.entity.medical.MedicalReferenceEntity;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import com.ilt.cms.downstream.PatientVisitDownstream;
import com.lippo.cms.util.CMSConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/patient-visit")
//@RolesAllowed("ROLE_VIEW_PATIENT_VISIT")
public class PatientVisitRestController {

    private static final Logger logger = LoggerFactory.getLogger(PatientVisitRestController.class);
    private PatientVisitDownstream patientVisitDownstream;

    public PatientVisitRestController(PatientVisitDownstream patientVisitDownstream) {
        this.patientVisitDownstream = patientVisitDownstream;
    }

    @PostMapping("/search/{visitId}")
    public ResponseEntity searchPatientVisitRegistryByVisitId(Principal principal, @PathVariable("visitId") String visitId) {
        logger.info("Getting PatientVisitRegistry [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.searchById(visitId);
    }

    @PostMapping("/list/{patientId}")
    public ResponseEntity searchPatientVisitRegistryByPatient(Principal principal, @PathVariable("patientId") String patientId) {
        logger.info("Getting all PatientVisitRegistries for user [" + principal.getName() + "]");
        return patientVisitDownstream.listPatientVisits(patientId);
    }

    @PostMapping("/list/{patientId}/{page}/{size}")
    public ResponseEntity searchPatientVisitRegistryByPatient(Principal principal, @PathVariable("patientId") String patientId, @PathVariable("page") int page, @PathVariable("size") int size) {
        logger.info("Getting all PatientVisitRegistries for user [" + principal.getName() + "]");
        return patientVisitDownstream.listPatientVisits(patientId, page, size);
    }

    @PostMapping("/remove/{visitId}/{caseId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity removePatientVisitRegistry(Principal principal, @PathVariable("visitId") String visitId, @PathVariable("caseId") String caseId) {
        logger.info("Removing PatientVisitRegistry [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.removeVisitFromCase(visitId, caseId);
    }

    @PostMapping("/update/{visitId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity updatePatientVisitRegistry(Principal principal, @PathVariable("visitId") String visitId, @RequestBody VisitRegistryEntity visitRegistryEntity) {
        logger.info("Updating PatientVisitRegistry [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateVisit(visitId, visitRegistryEntity);
    }

    @PostMapping("/create/{caseId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity createPatientVisitRegistry(Principal principal, @PathVariable(value = "caseId", required = false) String caseId, @RequestBody VisitRegistryWrapper registryWrapper) {
        logger.info("Creating new PatientVisitRegistry for case [caseId]: [" + caseId + "] user [" + principal.getName() + "]");
        return patientVisitDownstream.createVisit(caseId, registryWrapper.getRegistryEntity(), registryWrapper.getAttachedMedicalCoverages(), registryWrapper.getSingleVisitCase());
    }

    @PostMapping("/create")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity createPatientVisitRegistry(Principal principal, @RequestBody VisitRegistryWrapper registryWrapper) {
        logger.info("Creating new PatientVisitRegistry user [" + principal.getName() + "]");
        return patientVisitDownstream.createVisit(registryWrapper.getRegistryEntity(), registryWrapper.getAttachedMedicalCoverages(), registryWrapper.getSingleVisitCase());
    }

    @PostMapping("/list/by-time/{clinicId}/{from}/{to}")
    public ResponseEntity searchVisitByClinicAndStartTime(Principal principal, @PathVariable("clinicId") String clinicId,
                                                          @PathVariable("from") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS) LocalDateTime start,
                                                          @PathVariable("to") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS) LocalDateTime end) {
        logger.info("Getting visit by clinic id[" + clinicId + "] for startTime between [" + start + "] and ["
                + end + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.listVisitsByClinicAndStartTime(clinicId, start, end);
    }

    @PostMapping("/list/queue/{clinicId}")
    public ResponseEntity searchVisitByClinicQueue(Principal principal, @PathVariable("clinicId") String clinicId) {
        logger.info("Getting visit by clinic id[" + clinicId + "] queue for user [" + principal.getName() + "]");
        return patientVisitDownstream.searchVisitByClinicQueue(clinicId);
    }

    @PostMapping("/queue/next-patient/{clinicId}/{doctorId}")
    public ResponseEntity queueCallNextpatient(Principal principal, @PathVariable("clinicId") String clinicId,
                                               @PathVariable("doctorId") String doctorId) {
        logger.info("Calling next patient clinicId[" + clinicId + "] doctorId[" + doctorId + "] by [" + principal.getName() + "]");
        return patientVisitDownstream.callNextPatient(clinicId, doctorId);
    }

    @PostMapping("/attach/{caseId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity attachPatientVisitRegistyToCase(Principal principal, @PathVariable("caseId") String caseId,
                                                          @RequestBody List<String> visitIds) {
        logger.info("Attaching PatientVisitRegistries to case [" + caseId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.attachVisitToCase(visitIds, caseId);
    }

    @PostMapping("/list/by-month/{patientId}/{month}/{caseId}/{limit}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity getPatientLastVisitsByMonth(Principal principal, @PathVariable("patientId") String patientId,
                                                      @PathVariable("month") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS) LocalDateTime month,
                                                      @PathVariable("caseId") String caseId, @PathVariable("limit") int limit) {
        logger.info("Getting visit details by patient [patentId]:[" + patientId + "] for month [" + month + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.findAttachableVisits(patientId, caseId, month, limit);
    }

    @PostMapping("/update/consult/{visitId}/{doctorId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity updateToConsultState(Principal principal, @PathVariable("visitId") String visitId,
                                               @PathVariable("doctorId") String doctorId) {
        logger.info("Updating PatientVisitRegistry visitId[" + visitId + "] to [CONSULT] status for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateStatusToConsult(visitId, doctorId);
    }

    @PostMapping("/update/post-consult/{visitId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity updateToPostConsultState(Principal principal, @PathVariable("visitId") String visitId,
                                                   @RequestBody MedicalReferenceEntity medicalReferenceEntity) {
        logger.info("Updating PatientVisitRegistry [visitId]:[" + visitId + "] to [POST_CONSULT] status for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateStatusToPostConsult(visitId, medicalReferenceEntity, principal);

    }

    @PostMapping("/update/payment/{visitId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity updateToPaymentState(Principal principal, @PathVariable("visitId") String visitId,
                                               @RequestBody MedicalReferenceEntity medicalReferenceEntity) {
        logger.info("Updating PatientVisitRegistry [visitId]:[" + visitId + "] to [PAYMENT] status for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateStatusToPayment(visitId, medicalReferenceEntity, principal);
    }

    @PostMapping("/update/complete/{visitId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity updateToCompleteState(Principal principal, @PathVariable("visitId") String visitId) {
        logger.info("Updating PatientVisitRegistry [visitId]:[" + visitId + "] to [COMPLETE] status for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateStatusToComplete(visitId);
    }

    @PostMapping("/rollback/{visitId}")
    @RolesAllowed("ROLE_PATIENT_VISIT_MANAGE")
    public ResponseEntity rollbackStatusToPostConsult(Principal principal, @PathVariable("visitId") String visitId) {
        logger.info("rollback PatientVisitRegistry [visitId]:[" + visitId + "] to [POST_CONSULT] status for user [" + principal.getName() + "]");
        return patientVisitDownstream.rollbackStatusToPostConsult(visitId, principal);
    }

    @PostMapping("/list/by-case/{caseId}")
    public ResponseEntity findVisitDataByCase(Principal principal, @PathVariable("caseId") String caseId) {
        logger.info("Get all PatientVisitRegistries data for [caseId]:[" + caseId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.findVisitDataForCase(caseId);
    }

    @PostMapping("/update/consultation/{visitId}")
    public ResponseEntity updateVisitConsultation(Principal principal, @PathVariable("visitId") String visitId,
                                                  @RequestBody ConsultationWrapper consultationWrapper) {
        logger.info("Update PatientVisitRegistry - Consultation for [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateVisitConsultation(visitId, consultationWrapper.getConsultationEntity(),
                consultationWrapper.getDiagnosisIds(), principal);
    }

    @PostMapping("/update/referral/{visitId}")
    public ResponseEntity updateVisitPatientReferral(Principal principal, @PathVariable("visitId") String visitId,
                                                     @RequestBody PatientReferral patientReferral) {
        logger.info("Update PatientVisitRegistry - PatientReferral for [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateVisitPatientReferral(visitId, patientReferral, principal);
    }

    @PostMapping("/update/followup/{visitId}")
    public ResponseEntity updateVisitConsultationFollowup(Principal principal, @PathVariable("visitId") String visitId,
                                                          @RequestBody ConsultationFollowUp followUp) {
        logger.info("Update PatientVisitRegistry - ConsultationFollowUp for [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateVisitConsultationFollowup(visitId, followUp, principal);
    }

    @PostMapping("/update/certificates/{visitId}")
    public ResponseEntity updateVisitMedicalCertificates(Principal principal, @PathVariable("visitId") String visitId,
                                                         @RequestBody List<MedicalCertificate> certificates) {
        logger.info("Update PatientVisitRegistry - MedicalCertificates for [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.updateVisitMedicalCertificates(visitId, certificates, principal);
    }

    @PostMapping("/save/consultation/{visitId}")
    public ResponseEntity saveVisitConsultationData(Principal principal, @PathVariable("visitId") String visitId,
                                                    @RequestBody MedicalReferenceEntity medicalReferenceEntity) {
        logger.info("Update PatientVisitRegistry - Consultation data for [visitId]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.saveConsultationData(visitId, medicalReferenceEntity, principal);
    }

    @PostMapping("/save/dispensing/{visitId}")
    public ResponseEntity saveVisitDispensingData(Principal principal, @PathVariable("visitId") String visitId,
                                                  @RequestBody MedicalReferenceEntity medicalReferenceEntity) {
        logger.info("Update PatientVisitRegistry - dispensing data for [visit]:[" + visitId + "] for user [" + principal.getName() + "]");
        return patientVisitDownstream.saveDispensingData(visitId, medicalReferenceEntity, principal);
    }

    @PostMapping("/patient-consultation/list-followup/{clinicId}/{startDate}/{endDate}")
    public ResponseEntity listFollowups(Principal principal,
                                        @PathVariable("startDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                        @PathVariable("endDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate,
                                        @PathVariable("clinicId") String clinicId) {

        logger.info("Listing followups for [" + clinicId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return patientVisitDownstream.listFollowups(principal, clinicId, startDate, endDate);
    }

}
