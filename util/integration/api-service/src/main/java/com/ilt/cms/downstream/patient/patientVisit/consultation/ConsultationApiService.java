package com.ilt.cms.downstream.patient.patientVisit.consultation;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ConsultationApiService {

    ResponseEntity<ApiResponse> searchById(String consultationId);

    ResponseEntity<ApiResponse> searchByPatient(String patientId);
}
