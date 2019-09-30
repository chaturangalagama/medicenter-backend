package com.lippo.commons.util.exception;

public class RestValidationException extends RestCheckedException {

    public RestValidationException() {
    }

    public RestValidationException(String message) {
        super(message);
    }
}
