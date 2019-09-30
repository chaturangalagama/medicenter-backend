package com.ilt.cms.downstream.patient.patientVisit;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiagnosisDownstream {
    ResponseEntity<ApiResponse> searchById(List<String> diagnosisIds);

    ResponseEntity<ApiResponse> search(List<String> planIds, String term);
}