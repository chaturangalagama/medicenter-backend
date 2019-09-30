package com.ilt.cms.pm.integration.impl.patient.patientVisit;

import com.ilt.cms.api.container.DiagnosisDrugDispatchContainer;
import com.ilt.cms.api.container.PageableHttpApiResponse;
import com.ilt.cms.api.container.VisitPatientWrapper;
import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import com.ilt.cms.api.entity.consultation.MedicalCertificate;
import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.api.entity.diagnosis.DiagnosisEntity;
import com.ilt.cms.api.entity.medical.PatientReferralEntity;
import com.ilt.cms.api.entity.patient.PatientEntity;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowupWrapper;
import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import com.ilt.cms.core.entity.medical.DispatchItem;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.downstream.patient.patientVisit.PatientVisitApiService;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
import com.ilt.cms.pm.business.service.clinic.inventory.ItemService;
import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationFollowupService;
import com.ilt.cms.pm.business.service.patient.patientVisit.DiagnosisService;
import com.ilt.cms.pm.business.service.patient.PatientService;
import com.ilt.cms.pm.business.service.patient.patientVisit.PatientVisitService;
import com.ilt.cms.pm.business.service.patient.patientVisit.QueueService;
import com.ilt.cms.pm.integration.mapper.clinic.inventory.ItemMapper;
import com.ilt.cms.pm.integration.mapper.patient.PatientMapper;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.*;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.consultation.ConsultationFollowUpMapper;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.consultation.ConsultationMapper;
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
public class DefaultPatientVisitApiService implements PatientVisitApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPatientVisitApiService.class);

    private PatientVisitService patientVisitService;
    private ClinicService clinicService;
    private DiagnosisService diagnosisService;
    private ItemService itemService;
    private PatientService patientService;
    private QueueService queueService;
    private ConsultationFollowupService consultationFollowupService;

    public DefaultPatientVisitApiService(PatientVisitService patientVisitService, ClinicService clinicService,
                                         DiagnosisService diagnosisService, ItemService itemService,
                                         PatientService patientService, QueueService queueService,
                                         ConsultationFollowupService consultationFollowupService) {
        this.patientVisitService = patientVisitService;
        this.clinicService = clinicService;
        this.diagnosisService = diagnosisService;
        this.itemService = itemService;
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
    public ResponseEntity<ApiResponse> createVisit(String caseId, VisitRegistryEntity registryEntity, Boolean isSingleVisitCase) {
        try {
            PatientVisitRegistry createdVisitRegistry;
//            if (caseId == null) {
                createdVisitRegistry = patientVisitService.createPatientVisitRegistry(PatientVisitRegistryMapper.mapToCore(registryEntity), isSingleVisitCase);
                logger.info("New PatientVisitRegistry [visitId]:[" + createdVisitRegistry.getVisitNumber() + "] created and added to the new case");
//            } else {
//                createdVisitRegistry = patientVisitService.createPatientVisitRegistryForCase(caseId, PatientVisitRegistryMapper.mapToCore(registryEntity));
//                logger.info("New PatientVisitRegistry [visitId]:[" + createdVisitRegistry.getVisitNumber() + "] created and added to the case [id]:[" + caseId + "]");
//            }
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(createdVisitRegistry)));
        } catch (CMSException e) {
            logger.error("PatientVisitRegistry create error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> createVisit(VisitRegistryEntity registryEntity, Boolean isSingleVisitCase) {
        try {
            PatientVisitRegistry createdVisitRegistry = patientVisitService.createPatientVisitRegistry(PatientVisitRegistryMapper.mapToCore(registryEntity), isSingleVisitCase);
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
    public ResponseEntity<ApiResponse> updateStatusToPostConsult(String visitId, PatientReferralEntity patientReferralEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.changeStateToPostConsult(visitId, PatientReferralMapper.mapToCore(patientReferralEntity), principal);
            logger.info("Visit [visitId]:[" + visitId + "] updated to : [POST_CONSULT] status");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit to : [POST_CONSULT] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatusToPayment(String visitId, PatientReferralEntity patientReferralEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.changeStatusToPayment(visitId,
                    PatientReferralMapper.mapToCore(patientReferralEntity), principal, patientReferralEntity.getPlanMaxUsage());
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
                            PatientReferralMapper.mapDispatchItemToEntity(dispatchItem)));
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
            PatientVisitRegistry visit = patientVisitService.updatePatientReferral(visitId, PatientReferralMapper.mapPatientReferralToCore(patientReferral), principal);
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
                    .map(PatientReferralMapper.mapMedicalCertificatesToCore())
                    .collect(Collectors.toList()), principal);
            logger.info("PatientVisitRegistry [visitId]:[" + visitId + "] MedicalCertificates updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(visit)));
        } catch (CMSException e) {
            logger.error("Updating MedicalCertificates for visit [visitId]:[" + visitId + "] error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> saveConsultationData(String visitId, PatientReferralEntity patientReferralEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.saveConsultationData(visitId, PatientReferralMapper.mapToCore(patientReferralEntity), principal);
            logger.info("Visit [visitId]:[" + visitId + "] consultation data updated");
            return httpApiResponse(new HttpApiResponse(PatientVisitRegistryMapper.mapToEntity(registry)));
        } catch (CMSException e) {
            logger.error("Updating visit consultation data error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> saveDispensingData(String visitId, PatientReferralEntity patientReferralEntity, Principal principal) {
        try {
            PatientVisitRegistry registry = patientVisitService.saveDispensingData(visitId, PatientReferralMapper.mapToCore(patientReferralEntity), principal);
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
