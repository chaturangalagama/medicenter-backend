package com.ilt.cms.pm.rest.controller.patient;

import com.ilt.cms.api.entity.patient.MedicalAlertEntity;
import com.ilt.cms.downstream.patient.MedicalAlertApiService;
import com.lippo.commons.util.exception.RestValidationException;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/medical-alerts")
//@RolesAllowed("ROLE_PATIENT_MEDICAL_ALERTS")
public class MedicalAlertRestController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalAlertRestController.class);
    private MedicalAlertApiService medicailAlertDownstream;

    public MedicalAlertRestController(MedicalAlertApiService medicailAlertDownstream) {
        this.medicailAlertDownstream = medicailAlertDownstream;
    }

    @PostMapping("/list/{patientId}")
    public HttpEntity<ApiResponse> medicalAlerts(@PathVariable("patientId") String patientId)
            throws RestValidationException {
        logger.info("Retrieving medical alerts for [" + patientId + "] ");
        ResponseEntity<ApiResponse> serviceResponse = medicailAlertDownstream.medicalAlertList(patientId);
        return serviceResponse;
    }

    @PostMapping("/add/{patientId}")
    @RolesAllowed("ROLE_PATIENT_MEDICAL_ALERTS_MODIFY")
    public HttpEntity<ApiResponse> addMedicalAlerts(@PathVariable("patientId") String patientId,
                                           @RequestBody List<MedicalAlertEntity.MedicalAlertDetails> details) throws RestValidationException {
        logger.info("Retrieving medical alerts for [" + patientId + "]" );
        ResponseEntity<ApiResponse> serviceResponse = medicailAlertDownstream.add(patientId, details);
        return serviceResponse;
    }

    @PostMapping("/delete")
    @RolesAllowed("ROLE_PATIENT_MEDICAL_ALERTS_MODIFY")
    public HttpEntity<ApiResponse> deleteMedicalAlerts(@RequestBody List<String> medicalAlertId) throws RestValidationException {
        logger.info("deleting medical alert id [" + medicalAlertId + "]");
        ResponseEntity<ApiResponse> serviceResponse  = medicailAlertDownstream.delete(medicalAlertId);
        return serviceResponse;
    }
}
