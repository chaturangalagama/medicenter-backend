package com.ilt.cms.pm.business.visit.flow.handler;

import com.ilt.cms.pm.business.visit.event.Event;
import com.ilt.cms.pm.business.visit.flow.HandlerResponse;

public interface EventHandler<T extends Event> {

    HandlerResponse<T> handle(T t);
}
