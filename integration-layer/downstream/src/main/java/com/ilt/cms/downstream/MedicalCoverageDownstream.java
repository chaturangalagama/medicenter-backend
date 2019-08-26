package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalCoverageEntity;
import com.ilt.cms.api.entity.coverage.MedicalServiceSchemeEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalCoverageDownstream {

    ResponseEntity<ApiResponse> removeCoverage(String medicalCoverageId);

    ResponseEntity<ApiResponse> searchCoverage(String searchValue, Boolean includePolicyHolderCount);

    ResponseEntity<ApiResponse> listAll(Boolean includePolicyHolderCount);

    ResponseEntity<ApiResponse> listByPage(int page, int size, Boolean includePolicyHolderCount);

    ResponseEntity<ApiResponse> listByClinic(String clinicId);

    ResponseEntity<ApiResponse> addPlan(String medicalCoverageId, CoveragePlanEntity coveragePlan);

    ResponseEntity<ApiResponse> removePlan(String medicalCoverageId, String planId);

    ResponseEntity<ApiResponse> modifyCoverage(String medicalCoverageId, MedicalCoverageEntity medicalCoverage);

    ResponseEntity<ApiResponse> updatePlan(String medicalCoverageId, String planId, CoveragePlanEntity coveragePlan);

//    ResponseEntity<ApiResponse> addSchemeToPlan(String medicalCoverageId, String planId, MedicalServiceSchemeEntity medicalServiceScheme);

//    ResponseEntity<ApiResponse> replaceSchemeToPlan(String medicalCoverageId, String planId, List<MedicalServiceSchemeEntity> medicalServiceScheme, boolean included);

    ResponseEntity<ApiResponse> removeSchemeFromPlan(String medicalCoverageId, String planId, String schemeId);

    ResponseEntity<ApiResponse> addCoverage(MedicalCoverageEntity medicalCoverage);

    boolean doesPlanExists(String insuranceId, String planId);

    ResponseEntity<ApiResponse> searchByPlanId(String planId);
}