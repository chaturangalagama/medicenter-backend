package com.lippo.cms.exception;

import com.lippo.commons.util.StatusCode;

public class NotificationException extends CMSException{

    public NotificationException(){

    }

    public NotificationException(StatusCode code){
        super(code);
    }

    public NotificationException(StatusCode code, String message){
        super(code, message);
    }
}
