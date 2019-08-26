package com.lippo.cms.exception;

import com.lippo.commons.util.StatusCode;

public class AllergyGroupException extends CMSException {
    public AllergyGroupException(){

    }

    public AllergyGroupException(StatusCode code){
        super(code);
    }

    public AllergyGroupException(StatusCode code, String message){
        super(code, message);
    }
}
