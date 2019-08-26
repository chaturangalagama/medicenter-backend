package com.ilt.cms.pm.business.visit.flow.handler.registration;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.visit.PatientVisitRegistryDatabaseService;
import com.ilt.cms.pm.business.visit.event.Event;
import com.ilt.cms.pm.business.visit.event.RegistrationEvent;
import com.ilt.cms.pm.business.visit.flow.DefaultHandlerResponse;
import com.ilt.cms.pm.business.visit.flow.HandlerResponse;
import com.ilt.cms.pm.business.visit.flow.handler.EventHandler;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationPersistHandler<T extends Event> implements EventHandler<RegistrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationPersistHandler.class);
    private PatientVisitRegistryDatabaseService registryDatabaseService;
    private CaseDatabaseService caseDatabaseService;

    public RegistrationPersistHandler(PatientVisitRegistryDatabaseService registryDatabaseService, CaseDatabaseService caseDatabaseService) {
        this.registryDatabaseService = registryDatabaseService;
        this.caseDatabaseService = caseDatabaseService;
    }


    @Override
    public HandlerResponse<RegistrationEvent> handle(RegistrationEvent registrationEvent) {
        logger.info("persisting event [" + registrationEvent + "]");
        PatientVisitRegistry registry = registrationEvent.convert();
        registry.setStartTime(LocalDateTime.now());
        PatientVisitRegistry patientVisitRegistry = registryDatabaseService.save(registry);
        if (registrationEvent.getCaseId() != null) {
            logger.info("adding to existing case [" + registrationEvent.getCaseId() + "]");
            caseDatabaseService.addNewVisitToCase(registrationEvent.getCaseId(), patientVisitRegistry.getId());
        } else {
            logger.info("creating a new case for the visit");
            Case visitCase = new Case();
            visitCase.getVisitIds().add(patientVisitRegistry.getId());
            caseDatabaseService.save(visitCase);
        }
        logger.info("event persisted [" + patientVisitRegistry + "]");
        return new DefaultHandlerResponse<>(registrationEvent, StatusCode.S0000);
    }
}
