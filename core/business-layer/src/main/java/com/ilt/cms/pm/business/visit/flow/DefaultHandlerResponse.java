package com.ilt.cms.pm.business.visit.flow;

import com.ilt.cms.pm.business.visit.event.Event;
import com.lippo.commons.util.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class DefaultHandlerResponse<T extends Event> implements HandlerResponse<T> {

    private T event;
    private StatusCode statusCode;
    private Map<String, Object> ctx = new HashMap<>();


    public DefaultHandlerResponse(T event) {
        this.event = event;
    }

    public DefaultHandlerResponse(T event, StatusCode statusCode) {
        this.event = event;
        this.statusCode = statusCode;
    }

    @Override
    public boolean isSuccess() {
        return statusCode == null || statusCode == StatusCode.S0000;
    }

    @Override
    public StatusCode error() {
        return statusCode;
    }

    @Override
    public T event() {
        return event;
    }

    public void setEvent(T event) {
        this.event = event;
    }

    public Map<String, Object> getCtx() {
        return ctx;
    }
}
