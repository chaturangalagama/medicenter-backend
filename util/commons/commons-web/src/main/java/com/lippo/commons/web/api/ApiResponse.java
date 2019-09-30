package com.lippo.commons.web.api;

import com.lippo.commons.util.StatusCode;

public interface ApiResponse {
    String getMessage();

    void setMessage(String message);

    Object getPayload();

    void setPayload(Object payload);

    StatusCode getStatusCode();
}
