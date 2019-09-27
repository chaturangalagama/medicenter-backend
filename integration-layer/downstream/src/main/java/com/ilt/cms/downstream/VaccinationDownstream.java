package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.patient.PatientVaccination;
import com.ilt.cms.api.entity.vaccination.AssociatedCoverageVaccinationEntity;
import com.ilt.cms.api.entity.vaccination.VaccinationEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface VaccinationDownstream {
    ResponseEntity<ApiResponse> listVaccines(int page, int size);

    ResponseEntity<ApiResponse> listAllVaccines();

    ResponseEntity<ApiResponse> removeVaccinationFromPatient(String patientId, String associationVaccinationId);

    ResponseEntity<ApiResponse> removeAssociation(String medicalCoverageId, String associationCoverageId);

    ResponseEntity<ApiResponse> addVaccination(VaccinationEntity vaccination);

    ResponseEntity<ApiResponse> addVaccinationToPatient(String patientId, PatientVaccination vaccination);

    ResponseEntity<ApiResponse> vaccinationAssociation(AssociatedCoverageVaccinationEntity associatedCoverageVaccinationEntity, boolean b);
}