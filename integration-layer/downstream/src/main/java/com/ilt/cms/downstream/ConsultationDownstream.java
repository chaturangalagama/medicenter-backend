package com.ilt.cms.downstream;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ConsultationDownstream {

    ResponseEntity<ApiResponse> searchById(String consultationId);

    ResponseEntity<ApiResponse> searchByPatient(String patientId);
}
