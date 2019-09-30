package com.ilt.cms.downstream.patient.patientVisit.consultation;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ConsultationTemplateApiService {
    //ResponseEntity<ApiResponse> listAll(String templateType, String templateId, String doctorId, String patientId);

    ResponseEntity<ApiResponse> loadTemplate(String templateType, String templateId, String id, String doctorId);

    ResponseEntity<ApiResponse> listTemplate(String doctorId);


}