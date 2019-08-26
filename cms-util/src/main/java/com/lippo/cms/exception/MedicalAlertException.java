package com.lippo.cms.exception;


import com.lippo.commons.util.StatusCode;

public class MedicalAlertException extends CMSException {
    public MedicalAlertException(){

    }

    public MedicalAlertException(StatusCode code){
        super(code);
    }

    public MedicalAlertException(StatusCode code, String message){
        super(code, message);
    }
}
