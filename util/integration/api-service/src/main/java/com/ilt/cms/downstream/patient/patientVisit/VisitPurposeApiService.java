package com.ilt.cms.downstream.patient.patientVisit;

import com.ilt.cms.api.entity.patientVisitRegistry.VisitPurposeEntity;
import com.ilt.cms.api.entity.vital.VitalEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface VisitPurposeApiService {
    ResponseEntity<ApiResponse> listAll();

    ResponseEntity<ApiResponse> remove(String visitPurposeId);

    ResponseEntity<ApiResponse> addNew(VisitPurposeEntity visitPurposeEntity);

    ResponseEntity<ApiResponse> modify(String visitPurposeId, VisitPurposeEntity visitPurposeEntity);
}