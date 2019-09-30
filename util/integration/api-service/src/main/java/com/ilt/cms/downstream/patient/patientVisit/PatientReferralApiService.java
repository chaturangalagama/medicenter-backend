package com.ilt.cms.downstream.patient.patientVisit;

import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface PatientReferralApiService {

    ResponseEntity<ApiResponse> searchById(String referralId);

    ResponseEntity<ApiResponse> create(PatientReferral patientReferral);

    ResponseEntity<ApiResponse> update(String referralId, PatientReferral patientReferral);
}
