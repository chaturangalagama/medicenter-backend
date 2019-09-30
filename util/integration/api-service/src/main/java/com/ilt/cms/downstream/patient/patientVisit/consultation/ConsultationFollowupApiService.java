package com.ilt.cms.downstream.patient.patientVisit.consultation;

import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ConsultationFollowupApiService {

    ResponseEntity<ApiResponse> searchById(String followUpId);

    ResponseEntity<ApiResponse> createFollowup(ConsultationFollowUp followUp);

    ResponseEntity<ApiResponse> updateFollowup(String followUpId, ConsultationFollowUp followUp);

}
