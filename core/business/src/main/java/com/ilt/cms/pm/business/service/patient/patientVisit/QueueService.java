package com.ilt.cms.pm.business.service.patient.patientVisit;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.core.entity.visit.VisitPurpose;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.ilt.cms.repository.clinic.ClinicRepository;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
import com.ilt.cms.repository.patient.patientVisit.VisitPurposeRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
    private PatientVisitRepository visitRegistryRepository;
    private ClinicRepository clinicRepository;
    private VisitPurposeRepository visitPurposeRepository;
    private RunningNumberService runningNumberService;

    public QueueService(PatientVisitRepository visitRegistryRepository, ClinicRepository clinicRepository,
                        VisitPurposeRepository visitPurposeRepository, RunningNumberService runningNumberService) {
        this.visitRegistryRepository = visitRegistryRepository;
        this.clinicRepository = clinicRepository;
        this.visitPurposeRepository = visitPurposeRepository;
        this.runningNumberService = runningNumberService;
    }

    public PatientVisitRegistry.PatientQueue generateQueueNumber(String clinicId, String visitPurpose) throws CMSException {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new CMSException(StatusCode.E2000, "Invalid clinic code"));
        VisitPurpose vp = null;
        if (visitPurpose != null && !visitPurpose.isEmpty()) {
            vp = visitPurposeRepository.findByName(visitPurpose)
                    .orElseThrow(() -> new CMSException(StatusCode.E2000, "Invalid visit purpose"));
        }
        long nextNumber = runningNumberService.queueNextNumber(clinic.getClinicCode(), (vp == null) ? 1 : vp.getQueuePrefix());
        return new PatientVisitRegistry.PatientQueue(nextNumber, (vp != null) && vp.isUrgent());

    }

    public List<PatientVisitRegistry> listQueueByPriority(String clinicId) {
        return visitRegistryRepository.findAllByClinicIdAndStartTimeBetween(clinicId, LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay(), sortingLogic());
    }

    private Sort sortingLogic() {
//        return Sort.by("patientQueue.urgent", "patientQueue.queueNumber", "patientQueue.patientCalled").ascending();
        return Sort.by("patientQueue.urgent", "patientQueue.queueNumber").ascending();
    }

    public Optional<PatientVisitRegistry> callPatientVisit(String clinicId, boolean persist) {
        logger.info("calling next patient  for clinic [" + clinicId + "]");
        Optional<PatientVisitRegistry> registry = visitRegistryRepository
                .findFirstByClinicIdAndStartTimeBetweenAndPatientQueuePatientCalled(clinicId, LocalDate.now().atStartOfDay(),
                        LocalDate.now().plusDays(1).atStartOfDay(), false, sortingLogic());

        if (registry.isPresent()) {
            PatientVisitRegistry visitRegistry = registry.get();
            visitRegistry.getPatientQueue().setPatientCalled(true);
            PatientVisitRegistry patientVisitRegistry = visitRegistry;
            if (persist) {
                patientVisitRegistry = visitRegistryRepository.save(visitRegistry);
            }
            return Optional.of(patientVisitRegistry);
        } else {
            return registry;
        }
    }
}
