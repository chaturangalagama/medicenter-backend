package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.container.DiagnosisDrugDispatchContainer;
import com.ilt.cms.api.container.PageableHttpApiResponse;
import com.ilt.cms.api.container.VisitPatientWrapper;
import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import com.ilt.cms.api.entity.consultation.MedicalCertificate;
import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.api.entity.diagnosis.DiagnosisEntity;
import com.ilt.cms.api.entity.medical.DispatchItemEntity;
import com.ilt.cms.api.entity.medical.MedicalReferenceEntity;
import com.ilt.cms.api.entity.patient.PatientEntity;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowupWrapper;
import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.medical.DispatchItem;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.downstream.PatientVisitDownstream;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
import com.ilt.cms.pm.business.service.clinic.ItemService;
import com.ilt.cms.pm.business.service.doctor.ConsultationFollowupService;
import com.ilt.cms.pm.business.service.doctor.DiagnosisService;
import com.ilt.cms.pm.business.service.patient.CaseService;
import com.ilt.cms.pm.business.service.patient.PatientService;
import com.ilt.cms.pm.business.service.patient.PatientVisitService;
import com.ilt.cms.pm.business.service.queue.QueueService;
import com.ilt.cms.pm.integration.mapper.*;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultPatientVisitDownstream implements PatientVisitDownstream {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPatientVisitDownstream.class);

    private PatientVisitService patientVisitService;
    private ClinicService clinicService;
    private DiagnosisService diagnosisService;
    private ItemService itemService;
    private CaseService caseService;
    private PatientService patientService;
    private QueueService queueService;
    private ConsultationFollowupService consultationFollowupService;

    public DefaultPatientVisitDownstream(PatientVisitService patientVisitService, ClinicService clinicService,
                                         DiagnosisService diagnosisService, ItemService itemService,
                                         CaseService caseService, PatientService patientService, QueueService queueService,
                                         ConsultationFollowupService consultationFollowupService) {
        this.patientVisitService = patientVisitService;
        this.clinicService = clinicService;
        this.diagnosisService = diagnosisService;
        this.itemService = itemService;
        this.caseService = caseService;
        this.patientService = patientService;
        this.queueService = queueService;
        this.consultationFollowupService = consultationFollowupService;
    }

    @Override
    public ResponseEntity<ApiResponse> searchById(String visitId) {
        try {
            PatientVisitRegistry registry = patientVisitService.searchById(visitId);
            logger.info("PatientVisitRegistry found for [visitId]:[" + visitId + "]");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchByVisitNumber(String visitNumber) {
        try {
            PatientVisitRegistry registry = patientVisitService.searchByVisitNumber(visitNumber);
            logger.info("PatientVisitRegistry found for [visitNumber]:[" + visitNumber + "]");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listPatientVisits(String patientId) {
        try {
            List<PatientVisitRegistry> visitRegistries = patientVisitService.listVisits(patientId);
            logger.info("PatientVisitRegistries [" + visitRegistries.size() + "] found");
            List<VisitRegistryEntity> registryEntities = new ArrayList<>();
            for (PatientVisitRegistry visitRegistry : visitRegistries) {
                registryEntities.add(PatientVisitRegistryMapper.mapToEntity(visitRegistry));
            }
            return httpApiResponse(new HttpApiResponse(registryEntities));
        } catch (Exception e) {
            logger.error("Finding PatientVisitRegistries error :" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listPatientVisits(String patientId, int page, int size) {
        try {
            Page<PatientVisitRegistry> visitRegistries = patientVisitService.listVisits(patientId, page, size);
            List<VisitRegistryEntity> visitEntities = new ArrayList<>();
            for (PatientVisitRegistry visitRegistry : visitRegistries) {
                visitEntities.add(PatientVisitRegistryMapper.mapToEntity(visitRegistry));
            }
            return httpApiResponse(new PageableHttpApiResponse((int) visitRegistries.getTotalElements(), visitRegistries.getTotalPages(), visitRegistries.getNumber(), visitEntities));
        } catch (Exception e) {
            logger.error("Finding PatientVisitRegistries error :" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateVisit(String visitId, VisitRegistryEntity visitEntity) {
        try {
            PatientVisitRegistry registry = patientVisitService.updatePatientVisitRegistry(visitId, PatientVisitRegistryMapper.mapToCore(visitEntity));
            logger.info("PatientVisitRegistry updated [visitId]:[" + registry.getVisitNumber() + "]");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry update error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> removeVisitFromCase(String visitId, String caseId) {
        try {
            patientVisitService.detachedVisitFromCase(visitId, caseId);
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry remove error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
        logger.info("PatientVisitRegistry [visitId]: [" + visitId + "] removed from case [caseId]:[" + caseId + "]");
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @Override
    public ResponseEntity<ApiResponse> createVisit(String caseId, VisitRegistryEntity registryEntity, List<String> attachedMedicalCoverages, Boolean isSingleVisitCase) {
        try {
            PatientVisitRegistry createdVisitRegistry;
            if (caseId == null) {
                createdVisitRegistry = patientVisitService.createPatientVisitRegistry(PatientVisitRegistryMapper.mapToCore(registryEntity), attachedMedicalCoverages, isSingleVisitCase);
                logger.info("New PatientVisitRegistry [visitId]:[" + createdVisitRegistry.getVisitNumber() + "] created and added to the new case");
            } else {
                createdVisitRegistry = patientVisitService.createPatientVisitRegistryForCase(caseId, PatientVisitRegistryMapper.mapToCore(registryEntity));
                logger.info("New PatientVisitRegistry [visitId]:[" + createdVisitRegistry.getVisitNumber() + "] created and added to the case [id]:[" + caseId + "]");
            }
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(createdVisitRegistry)));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry create error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> createVisit(VisitRegistryEntity registryEntity, List<String> attachedMedicalCoverages, Boolean isSingleVisitCase) {
        try {
            PatientVisitRegistry createdVisitRegistry = patientVisitService.createPatientVisitRegistry(PatientVisitRegistryMapper.mapToCore(registryEntity), attachedMedicalCoverages, isSingleVisitCase);
            logger.info("New PatientVisitRegistry [visitId]:[" + createdVisitRegistry.getVisitNumber() + "] created and added to the new case");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(createdVisitRegistry)));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry create error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listVisitsByClinicAndStartTime(String clinicId, LocalDateTime start, LocalDateTime end) {
        try {
            List<PatientVisitRegistry> visits = patientVisitService.searchClinicAndDateRange(clinicId, start, end);
            List<VisitPatientWrapper> visitPatientWrappers = new ArrayList<>();
            for (PatientVisitRegistry visit : visits) {
                Patient patient = patientService.findPatientById(visit.getPatientId());
                visitPatientWrappers.add(new VisitPatientWrapper(PatientVisitRegistryMapper.mapToEntity(visit), patient.getName(), PatientMapper.mapToUserIdEntity(patient.getUserId())));
            }
            logger.info("Visits found for clinic [id]:[" + clinicId + "] startTime between [" + start + "] and [" + end + "]");
            return httpApiResponse(new HttpApiResponse(visitPatientWrappers));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry create error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> attachVisitToCase(List<String> visitIds, String caseId) {
        try {
            patientVisitService.attachedVisitToCase(visitIds, caseId);
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry attaching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
        logger.info("PatientVisitRegistry [visitIds]: [" + visitIds.toString() + "] attached to case [caseId]:[" + caseId + "]");
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @Override
    public ResponseEntity<ApiResponse> findAttachableVisits(String patientId, String caseId, LocalDateTime startTime, int limit) {
        try {
            List<PatientVisitRegistry> visits = patientVisitService.searchVisitByPatentAndMonth(patientId, caseId, startTime, null, limit);
            List<VisitRegistryEntity> registryEntities = new ArrayList<>();
            for (PatientVisitRegistry visit : visits) {
                Case aCase = caseService.findByCaseId(visit.getCaseId());
                VisitRegistryEntity registryEntity = PatientVisitRegistryMapper.mapToEntity(visit);
                registryEntity.setCaseNumber(aCase.getCaseNumber());
                registryEntities.add(registryEntity);
            }
            logger.info("Visits found [" + visits.size() + "] for patient [id]:[" + patientId + "] startTime in month [" + startTime + "}");
            return httpApiResponse(new HttpApiResponse(registryEntities));
        } catch (CMSException e) {
            logger.error("Getting attachable visits error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatusToConsult(String visitId, String doctorId) {
        try {
            PatientVisitRegistry registry = patientVisitService.changeStateToConsult(visitId, doctorId, true);
            logger.info("Visit [visitId]:[" + visitId + "] updated to : [CONSULT] status for doctor:[" + doctorId + "]");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit to : [CONSULT] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatusToPostConsult(String visitId, MedicalReferenceEntity medicalReferenceEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.changeStateToPostConsult(visitId, MedicalReferenceMapper.mapToCore(medicalReferenceEntity), principal);
            logger.info("Visit [visitId]:[" + visitId + "] updated to : [POST_CONSULT] status");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit to : [POST_CONSULT] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatusToPayment(String visitId, MedicalReferenceEntity medicalReferenceEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.changeStatusToPayment(visitId,
                    MedicalReferenceMapper.mapToCore(medicalReferenceEntity), principal, medicalReferenceEntity.getPlanMaxUsage());
            logger.info("Visit [visitId]:[" + visitId + "] updated to : [PAYMENT] status");
            return httpApiResponse(new HttpApiResponse(registry));
        } catch (CMSException e) {
            logger.error("Updating visit to : [PAYMENT] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> rollbackStatusToPostConsult(String visitId, Principal principal) {
        try {
            logger.info("Visit [visitId]:[" + visitId + "] roll backed to : [POST_CONSULT] by [" + principal.getName() + "]");
            PatientVisitRegistry registry = patientVisitService.rollbackStatusToPostConsult(visitId);
            return httpApiResponse(new HttpApiResponse(registry));
        } catch (CMSException e) {
            logger.error("Updating visit to : [PAYMENT] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatusToComplete(String visitId) {
        try {
            PatientVisitRegistry registry = patientVisitService.changeStatusToComplete(visitId);
            logger.info("Visit [visitId]:[" + visitId + "] updated to : [COMPLETE] status");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit to : [COMPLETE] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> findDiagnosisDataByVisit(String visitId) {
        try {
            PatientVisitRegistry visit = patientVisitService.searchById(visitId);
            DiagnosisDrugDispatchContainer container = new DiagnosisDrugDispatchContainer();
            List<DiagnosisDrugDispatchContainer.DispatchContainer> dispatchContainers = new ArrayList<>();
            List<DiagnosisEntity> diagnosisEntities = new ArrayList<>();

            MedicalReference medicalReference = visit.getMedicalReference();
            if (medicalReference != null) {
                for (DispatchItem dispatchItem : medicalReference.getDispatchItems()) {
                    dispatchContainers.add(new DiagnosisDrugDispatchContainer.DispatchContainer(
                            ItemMapper.mapItemToEntity(itemService.searchItemById(dispatchItem.getItemId())),
                            MedicalReferenceMapper.mapDispatchItemToEntity(dispatchItem)));
                }
                diagnosisEntities = diagnosisService.searchById(medicalReference.getDiagnosisIds())
                        .parallelStream()
                        .map(DiagnosisMapper::mapToEntity)
                        .collect(Collectors.toList());
            }

            container.setDispatchContainers(dispatchContainers);
            container.setDiagnosis(diagnosisEntities);
            logger.info("Diagnosis and Dispatch details found for [visitId]:[" + visitId + "]");
            return httpApiResponse(new HttpApiResponse(container));
        } catch (CMSException e) {
            logger.error("Getting Diagnosis and Dispatch details error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> findVisitDataForCase(String caseId) {
        try {
            Case aCase = caseService.findByCaseId(caseId);
            List<VisitRegistryEntity> visitEntities = patientVisitService.searchByIds(aCase.getVisitIds())
                    .parallelStream()
                    .map(PatientVisitRegistryMapper::mapToEntity)
                    .collect(Collectors.toList());

            for (VisitRegistryEntity visitEntity : visitEntities) {
                Clinic clinic = clinicService.searchById(visitEntity.getClinicId());
                if (visitEntity.getMedicalReferenceEntity().getDiagnosisIds() != null) {
                    List<DiagnosisEntity> diagnosisEntities = diagnosisService.searchById(visitEntity.getMedicalReferenceEntity().getDiagnosisIds())
                            .parallelStream()
                            .map(DiagnosisMapper::mapToEntity)
                            .collect(Collectors.toList());
                    visitEntity.setDiagnosisEntities(diagnosisEntities);
                }

                if (visitEntity.getMedicalReferenceEntity().getDispatchItemEntities() != null) {
                    List<DispatchItemEntity> dispatchItemEntities = new ArrayList<>();
                    for (DispatchItemEntity dispatchItemEntity : visitEntity.getMedicalReferenceEntity().getDispatchItemEntities()) {
                        if (dispatchItemEntity.getPurchasedId() == null) {
                            Item item = itemService.searchItemById(dispatchItemEntity.getItemId());
                            dispatchItemEntity.setItemCode(item.getCode());
                            dispatchItemEntity.setItemName(item.getName());
                            dispatchItemEntities.add(dispatchItemEntity);
                        }
                    }
                    visitEntity.getMedicalReferenceEntity().setDispatchItemEntities(dispatchItemEntities);
                }
                visitEntity.setClinicName(clinic.getName());
            }
            logger.info("Visits [" + visitEntities.size() + "] found for [caseId]:[" + caseId + "]");
            return httpApiResponse(new HttpApiResponse(visitEntities));
        } catch (CMSException e) {
            logger.error("Getting visits details for caseId error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateVisitConsultation(String visitId, ConsultationEntity consultationEntity, List<String> diagnosisIds, Principal principal) {
        try {
            PatientVisitRegistry visit = patientVisitService.updateConsultation(visitId, ConsultationMapper.mapToCore(consultationEntity), diagnosisIds, principal);
            logger.info("PatientVisitRegistry [visitId]:[" + visitId + "] consultation updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(visit)));
        } catch (CMSException e) {
            logger.error("Updating consultation for visit [visitId]:[" + visitId + "] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateVisitPatientReferral(String visitId, PatientReferral patientReferral, Principal principal) {
        try {
            PatientVisitRegistry visit = patientVisitService.updatePatientReferral(visitId, MedicalReferenceMapper.mapPatientReferralToCore(patientReferral), principal);
            logger.info("PatientVisitRegistry [visitId]:[" + visitId + "] PatientReferral updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(visit)));
        } catch (CMSException e) {
            logger.error("Updating PatientReferral for visit [visitId]:[" + visitId + "] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateVisitConsultationFollowup(String visitId, ConsultationFollowUp followUp, Principal principal) {
        try {
            PatientVisitRegistry visit = patientVisitService.updateConsultationFollowup(visitId, ConsultationFollowUpMapper.mapToCore(followUp), principal);
            logger.info("PatientVisitRegistry [visitId]:[" + visitId + "] ConsultationFollowUp updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(visit)));
        } catch (CMSException e) {
            logger.error("Updating ConsultationFollowUp for visit [visitId]:[" + visitId + "] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateVisitMedicalCertificates(String visitId, List<MedicalCertificate> certificates, Principal principal) {
        try {
            PatientVisitRegistry visit = patientVisitService.updateMedicalCertificates(visitId, certificates.stream()
                    .map(MedicalReferenceMapper.mapMedicalCertificatesToCore())
                    .collect(Collectors.toList()), principal);
            logger.info("PatientVisitRegistry [visitId]:[" + visitId + "] MedicalCertificates updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(visit)));
        } catch (CMSException e) {
            logger.error("Updating MedicalCertificates for visit [visitId]:[" + visitId + "] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> saveConsultationData(String visitId, MedicalReferenceEntity medicalReferenceEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.saveConsultationData(visitId, MedicalReferenceMapper.mapToCore(medicalReferenceEntity), principal);
            logger.info("Visit [visitId]:[" + visitId + "] consultation data updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit consultation data error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> saveDispensingData(String visitId, MedicalReferenceEntity medicalReferenceEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.saveDispensingData(visitId, MedicalReferenceMapper.mapToCore(medicalReferenceEntity), principal);
            logger.info("Visit [visitId]:[" + visitId + "] dispensing data updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit dispensing data error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchVisitByClinicQueue(String clinicId) {
        List<PatientVisitRegistry> visits = queueService.listQueueByPriority(clinicId);
        logger.info("listing patient queue for [" + clinicId + "] found [" + visits.size() + "]");
        List<VisitPatientWrapper> visitPatientWrappers = new ArrayList<>();
        for (PatientVisitRegistry visit : visits) {
            Patient patient = patientService.findPatientById(visit.getPatientId());
            visitPatientWrappers.add(new VisitPatientWrapper(PatientVisitRegistryMapper.mapToEntity(visit), patient.getName(), PatientMapper.mapToUserIdEntity(patient.getUserId())));
        }
        return httpApiResponse(new HttpApiResponse(visitPatientWrappers));
    }

    @Override
    public ResponseEntity<ApiResponse> callNextPatient(String clinicId, String doctorId) {
        try {
            logger.info("Calling next patient for clinic [" + clinicId + "] by [" + doctorId + "]");
            PatientVisitRegistry patientVisitRegistry = queueService.callPatientVisit(clinicId, false)
                    .orElseThrow(() -> new CMSException(StatusCode.E1010, "No patient found to change state"));
            PatientVisitRegistry registry = patientVisitService.changeStateToConsult(patientVisitRegistry.getId(), doctorId, true);
            logger.info("Visit [visitId]:[" + patientVisitRegistry.getId() + "] updated to : [CONSULT] status for doctor:[" + doctorId + "]");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("There was no patient found in clinic[" + clinicId + "] to call");
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> listFollowups(Principal principal, String clinicId, LocalDate startDate, LocalDate endDate) {
        try {
            List<ConsultationFollowup> followups = consultationFollowupService.listFollowups(principal,
                    clinicId, startDate, endDate);
            Map<String, PatientEntity> patientMap = loadPatients(followups);
            List<ConsultationFollowupWrapper> followupWrappers = new ArrayList<>();
            List<ConsultationFollowUp> consultationFollowUpsEntities = followups.stream()
                    .map(followup -> ConsultationFollowUpMapper.mapToEntity(followup))
                    .collect(Collectors.toList());
            for (ConsultationFollowUp followup : consultationFollowUpsEntities) {
                followupWrappers.add(new ConsultationFollowupWrapper(followup, patientMap.get(followup.getPatientId())));
            }
            return httpApiResponse(new HttpApiResponse(followupWrappers));
        } catch (CMSException e) {
            logger.error("ConsultationFollowup list error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    private Map<String, PatientEntity> loadPatients(List<ConsultationFollowup> followups) {
        Map<String, PatientEntity> patientMap = new HashMap<>();
        List<Patient> patients = patientService.findAll(followups.stream()
                .map(ConsultationFollowup::getPatientId)
                .collect(Collectors.toList()));
        patients.stream().map(patient -> PatientMapper.mapToEntity(patient)).collect(Collectors.toList()).forEach(
                patientEntity -> patientMap.put(patientEntity.getId(), patientEntity)
        );
        return patientMap;
    }

}
