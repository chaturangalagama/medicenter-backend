package com.lippo.commons.web.api;


import com.lippo.commons.util.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

// one lower then highest so that transaction logger has the highest
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        logger.error("Exception handled as an API : ", ex);
        HttpApiResponse httpResponse = new HttpApiResponse(StatusCode.I5000);
        httpResponse.setMessage(ex.getMessage());
        return buildResponseEntity(httpResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpApiResponse httpResponse) {
        return new ResponseEntity<>(httpResponse, INTERNAL_SERVER_ERROR);
    }
}