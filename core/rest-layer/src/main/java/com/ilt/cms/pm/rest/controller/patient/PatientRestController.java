package com.ilt.cms.pm.rest.controller.patient;

import com.ilt.cms.api.entity.patient.PatientEntity;
import com.ilt.cms.downstream.patient.PatientDownstream;
import com.lippo.commons.util.exception.RestValidationException;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/patient")
//@RolesAllowed("ROLE_PATIENT_ACCESS")
public class PatientRestController {

    private static final Logger logger = LoggerFactory.getLogger(PatientRestController.class);
    private static final Logger patientUsageTrans = LoggerFactory.getLogger("PATIENT_USAGE_TRANS");

    private PatientDownstream patientDownstream;

    public PatientRestController(PatientDownstream patientDownstream) {
        this.patientDownstream = patientDownstream;
    }

    @PostMapping("/create")
    public HttpEntity<ApiResponse> createPatient(PatientEntity patientEntity) {
        ResponseEntity<ApiResponse> patient = patientDownstream.createPatient(patientEntity);
        return patient;
    }
    /*new */
    @PostMapping("/search/{type}/{value}")
    public HttpEntity<ApiResponse> search(@PathVariable("type") String type,
                                 @PathVariable("value") String value) {
        ResponseEntity<ApiResponse> patient = patientDownstream.search(type, value);
        return patient;
    }

    @PostMapping("/validate/{idNumber}")
    public HttpEntity<ApiResponse> validate(@PathVariable("idNumber") String idNumber) {

        ResponseEntity<ApiResponse> validate = patientDownstream.validate(idNumber);
        return validate;
    }

    @PostMapping("/like-search/{value}")
    public HttpEntity<ApiResponse> likeSearch(@PathVariable("value") String value) {

        ResponseEntity<ApiResponse> patient = patientDownstream.likeSearch(value);
        return patient;
    }

    @PostMapping("/update/{id}")
    @RolesAllowed("ROLE_PATIENT_REGISTRATION")
    public HttpEntity<ApiResponse> update(@PathVariable("id") String id,
                                 @RequestBody PatientEntity patientEntity) throws RestValidationException {
        ResponseEntity<ApiResponse> patient = patientDownstream.update(id, patientEntity);
        return patient;
    }

    @PostMapping("/register")
//    @RolesAllowed("ROLE_PATIENT_REGISTRATION")
    public HttpEntity<ApiResponse> register(@RequestBody PatientEntity patientEntity) throws RestValidationException {
        ResponseEntity<ApiResponse> patient = patientDownstream.register(patientEntity);
        return patient;
    }
}
