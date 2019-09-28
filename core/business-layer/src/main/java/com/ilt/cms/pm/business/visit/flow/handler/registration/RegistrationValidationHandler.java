package com.ilt.cms.pm.business.visit.flow.handler.registration;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
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

import java.util.Optional;

@Service
public class RegistrationValidationHandler<T extends Event> implements EventHandler<RegistrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationValidationHandler.class);

    private PatientDatabaseService patientService;
    private ClinicDatabaseService clinicService;

    public RegistrationValidationHandler(PatientDatabaseService patientService, ClinicDatabaseService clinicService) {
        this.patientService = patientService;
        this.clinicService = clinicService;
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
        return new DefaultHandlerResponse<>(registrationEvent);
    }
}
