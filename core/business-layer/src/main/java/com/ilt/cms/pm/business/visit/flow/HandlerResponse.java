package com.ilt.cms.pm.business.visit.flow;

import com.ilt.cms.pm.business.visit.event.Event;
import com.lippo.commons.util.StatusCode;

public interface HandlerResponse<T extends Event> {

    boolean isSuccess();
    StatusCode error();
    T event();
}
