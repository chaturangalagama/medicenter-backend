package com.ilt.cms.downstream.clinic.system;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface SystemConfigurationApiService {

    ResponseEntity<ApiResponse> listRelationships();

    ResponseEntity<ApiResponse> listMaritalStatus();

    ResponseEntity<ApiResponse> listMedicalTestLaboratories();

    ResponseEntity<ApiResponse> listClinicGroups();

    ResponseEntity<ApiResponse> listInstructions();

    ResponseEntity<ApiResponse> listPrescriptionDose();

    ResponseEntity<ApiResponse> listDefaultLabel();

    ResponseEntity<ApiResponse> listDoctorBySpeciality();

    ResponseEntity<ApiResponse> findPostcode(String code);

    ResponseEntity<ApiResponse> validateIdentification(String type, String idNumber);

    ResponseEntity<ApiResponse> listVitals();

    ResponseEntity<ApiResponse> listVitalsDefault();
}
