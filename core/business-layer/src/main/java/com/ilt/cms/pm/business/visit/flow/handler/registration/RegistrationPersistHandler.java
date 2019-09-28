package com.ilt.cms.pm.business.visit.flow.handler.registration;

import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
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

    public RegistrationPersistHandler(PatientVisitRegistryDatabaseService registryDatabaseService) {
        this.registryDatabaseService = registryDatabaseService;
    }


    @Override
    public HandlerResponse<RegistrationEvent> handle(RegistrationEvent registrationEvent) {
        logger.info("persisting event [" + registrationEvent + "]");
        PatientVisitRegistry registry = registrationEvent.convert();
        registry.setStartTime(LocalDateTime.now());
        PatientVisitRegistry patientVisitRegistry = registryDatabaseService.save(registry);
        logger.info("event persisted [" + patientVisitRegistry + "]");
        return new DefaultHandlerResponse<>(registrationEvent, StatusCode.S0000);
    }
}
