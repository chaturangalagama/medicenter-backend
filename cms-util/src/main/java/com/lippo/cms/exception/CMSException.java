package com.lippo.cms.exception;

import com.lippo.commons.util.StatusCode;

public class CMSException extends Exception{

    private StatusCode code;
    private String message;

    public CMSException(){
        code = StatusCode.I5000;
        this.message = code.description();
    }

    public CMSException(Throwable err){
        super(err);
        code = StatusCode.I5000;
        this.message = code.description();
    }

    public CMSException(StatusCode code) {
        super(code.name());
        this.code = code;
        this.message = code.description();
    }

    public CMSException(StatusCode code, Throwable err) {
        super(code.name(), err);
        this.code = code;
        this.message = code.description();
    }

    public CMSException(StatusCode code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public CMSException(StatusCode code, String message, Throwable err){
        super(message, err);
        this.code = code;
        this.message = message;
    }

    public StatusCode getCode() {
        return code;
    }

    public StatusCode getStatusCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}