package com.lippo.commons.web.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.commons.util.StatusCode;

import java.time.LocalDateTime;

public class HttpApiResponse implements ApiResponse {


    private StatusCode statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private Object payload;

    private HttpApiResponse() {
        timestamp = LocalDateTime.now();
    }

    public HttpApiResponse(StatusCode status) {
        this();
        this.statusCode = status;
        this.message = status.description();
    }

    /**
     * Creates a Success Http ApiResponse with a custom Message
     *
     * @param message
     */
    public HttpApiResponse(String message) {
        this(StatusCode.S0000);
        this.message = message;
    }

    /**
     * Created a new Success HTTP response with a payload
     *
     * @param payload
     */
    public HttpApiResponse(Object payload) {
        this(StatusCode.S0000);
        this.payload = payload;
    }

    public HttpApiResponse(StatusCode statusCode, String message) {
        this(statusCode);
        this.message = message;
    }

    public HttpApiResponse(StatusCode statusCode, String message, Throwable ex) {
        this();
        this.statusCode = statusCode;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
