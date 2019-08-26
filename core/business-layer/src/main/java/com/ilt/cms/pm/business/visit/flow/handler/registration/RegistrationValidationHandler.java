package com.ilt.cms.pm.business.visit.flow.handler.registration;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.pm.business.visit.event.Event;
import com.ilt.cms.pm.business.visit.event.RegistrationEvent;
import com.ilt.cms.pm.business.visit.flow.DefaultHandlerResponse;
import com.ilt.cms.pm.business.visit.flow.HandlerResponse;
import com.ilt.cms.pm.business.visit.flow.handler.EventHandler;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationValidationHandler<T extends Event> implements EventHandler<RegistrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationValidationHandler.class);

    private PatientDatabaseService patientService;
    private ClinicDatabaseService clinicService;
    private MedicalCoverageDatabaseService medicalCoverageService;
    private CaseDatabaseService caseDatabaseService;

    public RegistrationValidationHandler(PatientDatabaseService patientService, ClinicDatabaseService clinicService,
                                         MedicalCoverageDatabaseService medicalCoverageService, CaseDatabaseService caseDatabaseService) {
        this.patientService = patientService;
        this.clinicService = clinicService;
        this.medicalCoverageService = medicalCoverageService;
        this.caseDatabaseService = caseDatabaseService;
    }


    @Override
    public HandlerResponse<RegistrationEvent> handle(RegistrationEvent registrationEvent) {

        logger.info("Validating patient registration");
        boolean parametersValid = registrationEvent.areParametersValid();
        if (!parametersValid) {
            logger.error("Registration event details are not valid [" + registrationEvent + "]");
            return new DefaultHandlerResponse<>(registrationEvent, StatusCode.E1002);
        }

        Optional<Patient> patientOpt = patientService.findPatientById(registrationEvent.getPatientId());

        if (!patientOpt.isPresent()) {
            logger.error("Patient [" + registrationEvent.getPatientId() + "] not found");
            return new DefaultHandlerResponse<>(registrationEvent, StatusCode.E2003);
        }
        Optional<Clinic> clinicOpt = clinicService.findActiveClinic(registrationEvent.getClinicId());
        if (!clinicOpt.isPresent()) {
            logger.error("Clinic [" + registrationEvent.getClinicId() + "] not found");
            return new DefaultHandlerResponse<>(registrationEvent, StatusCode.E2002);
        }

        List<AttachedMedicalCoverage> attachedMedicalCoverages = registrationEvent.getAttachedMedicalCoverages();
        if (attachedMedicalCoverages != null && attachedMedicalCoverages.size() > 0) {

            boolean match = attachedMedicalCoverages.stream()
                    .allMatch(this::medicalCoverageValid);

            if (!match) {
                logger.error("The attached medical coverages did not have valid ");
                return new DefaultHandlerResponse<>(registrationEvent, StatusCode.E2001);
            }
        }

        if (registrationEvent.getCaseId() != null) {
            boolean exists = caseDatabaseService.existsAndActive(registrationEvent.getCaseId());
            if (!exists) {
                logger.error("The given case id is not available or not active");
                return new DefaultHandlerResponse<>(registrationEvent, StatusCode.E2004);
            }
        }
        return new DefaultHandlerResponse<>(registrationEvent);
    }

    private boolean medicalCoverageValid(AttachedMedicalCoverage attachedMedicalCoverage) {
        return attachedMedicalCoverage.areParametersValid()
                && medicalCoverageService.findActivePlan(attachedMedicalCoverage.getPlanId()) != null;
    }
}
