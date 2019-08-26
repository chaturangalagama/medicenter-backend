package com.lippo.cms.exception;

import com.lippo.commons.util.StatusCode;

public class PolicyHolderException extends CMSException{
    public PolicyHolderException(){

    }

    public PolicyHolderException(StatusCode code){
        super(code);
    }

    public PolicyHolderException(StatusCode code, String message){
        super(code, message);
    }

}
