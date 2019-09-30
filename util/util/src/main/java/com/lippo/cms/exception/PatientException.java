package com.lippo.cms.exception;

import com.lippo.commons.util.StatusCode;

public class PatientException extends CMSException{
    public PatientException(){

    }

    public PatientException(StatusCode code){
        super(code);
    }

    public PatientException(StatusCode code, String message){
        super(code, message);
    }

}