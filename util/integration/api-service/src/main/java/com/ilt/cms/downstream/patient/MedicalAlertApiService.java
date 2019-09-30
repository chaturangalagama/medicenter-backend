package com.ilt.cms.downstream.patient;

import com.ilt.cms.api.entity.patient.MedicalAlertEntity;
import com.lippo.commons.util.exception.RestValidationException;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface MedicalAlertApiService {
    ResponseEntity<ApiResponse> medicalAlertList(String patientId);

    ResponseEntity<ApiResponse> add(String patientId, List<MedicalAlertEntity.MedicalAlertDetails> details);

    ResponseEntity<ApiResponse> delete(List<String> medicalAlertId);

}
