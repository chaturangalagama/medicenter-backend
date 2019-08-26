package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.coverage.PolicyHolderEntity;

import com.ilt.cms.api.entity.common.UserId;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface PolicyHolderDownstream {

    ResponseEntity<ApiResponse> addPolicyHolderToCoverage(String coverageType, PolicyHolderEntity policyHolder);

    ResponseEntity<ApiResponse> searchPolicyHolder(String policyHolderId, String coverageType);

    ResponseEntity<ApiResponse> searchPolicyHolder(UserId userId);

    ResponseEntity<ApiResponse> removePolicyHolderToCoverage(String holderId, String coverageType, String medicalCoverageId, String planId);
}
