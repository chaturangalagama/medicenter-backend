package com.lippo.cms.container;

import com.lippo.commons.util.StatusCode;

public class CmsServiceResponse<T> {

    private StatusCode statusCode;
    private String message;
    private String debugMessage;
    private T payload;

    private CmsServiceResponse() {

    }

    public CmsServiceResponse(T payload) {
        this(StatusCode.S0000);
        this.payload = payload;
    }

    public CmsServiceResponse(StatusCode status) {
        this();
        this.statusCode = status;
        this.message = status.description();
    }

    public CmsServiceResponse(StatusCode statusCode, String message) {
        this(statusCode);
        this.message = message;
    }

    public CmsServiceResponse(StatusCode statusCode, String message, Throwable ex) {
        this(statusCode, message);
        this.debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Just to make autocompletion easier
     *
     * @param payload
     * @param <Type>
     * @return
     */
    public static <Type> CmsServiceResponse<Type> newInstance(Type payload) {
        return new CmsServiceResponse<>(payload);
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "CmsServiceResponse{" +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", debugMessage='" + debugMessage + '\'' +
                ", payload=" + payload +
                '}';
    }
}
