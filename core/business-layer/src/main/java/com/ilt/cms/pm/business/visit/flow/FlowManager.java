package com.ilt.cms.pm.business.visit.flow;

import com.ilt.cms.pm.business.visit.event.Event;
import com.ilt.cms.pm.business.visit.event.RegistrationEvent;
import com.ilt.cms.pm.business.visit.flow.handler.EventHandler;
import com.ilt.cms.pm.business.visit.flow.handler.registration.RegistrationPersistHandler;
import com.ilt.cms.pm.business.visit.flow.handler.registration.RegistrationValidationHandler;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowManager<T extends Event> {

    private static final Logger logger = LoggerFactory.getLogger(FlowManager.class);

    private final List<EventHandler<T>> handlers;

    public FlowManager() {
        handlers = new ArrayList<>();
    }

    public HandlerResponse<T> handle(T t) {
        final Map<String, Object> ctx = new HashMap<>();
        for (EventHandler<T> handler : handlers) {
            try {
                DefaultHandlerResponse handle = (DefaultHandlerResponse) handler.handle(t);
                ctx.putAll(handle.getCtx());
                if (!handle.isSuccess()) {
                    DefaultHandlerResponse<T> response = new DefaultHandlerResponse<>(t, handle.error());
                    response.getCtx().putAll(ctx);
                    return response;
                }
            } catch (Exception e) {
                logger.error("Error in handling the flow : ", e);
                return new DefaultHandlerResponse<>(null, StatusCode.I5000);
            }
        }
        DefaultHandlerResponse<T> response = new DefaultHandlerResponse<>(t);
        response.getCtx().putAll(ctx);
        return response;
    }

    public List<EventHandler<T>> getHandlers() {
        return handlers;
    }


    @Configuration
    public static class FlowManagerConfiguration {

        @Bean(name = "visit-reg")
        @SuppressWarnings("unchecked")
        public FlowManager visitReg(RegistrationValidationHandler registrationValidationHandler, RegistrationPersistHandler persistHandler) {
            FlowManager<RegistrationEvent> flowManager = new FlowManager<>();
            List<EventHandler<RegistrationEvent>> handlers = flowManager.getHandlers();
            handlers.add(registrationValidationHandler);
            handlers.add(persistHandler);
            return flowManager;
        }
    }
}
