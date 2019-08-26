package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.common.UserId;
import com.ilt.cms.api.entity.coverage.PolicyHolderEntity;
import com.ilt.cms.downstream.PolicyHolderDownstream;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/policyholder")
//@RolesAllowed("ROLE_MEDICAL_VIEW_POLICY_HOLDER")
public class PolicyHolderRestController {

    private static final Logger logger = LoggerFactory.getLogger(AllergyGroupRestController.class);
    private PolicyHolderDownstream policyHolderDownstreamDownstream;

    public PolicyHolderRestController(PolicyHolderDownstream policyHolderDownstreamDownstream){
        this.policyHolderDownstreamDownstream = policyHolderDownstreamDownstream;
    }

    @RolesAllowed("ROLE_MEDICAL_MANAGE_POLICY_HOLDER")
    @PostMapping("/add/{coverageType}")
    public HttpEntity<ApiResponse> addCoverage(@PathVariable("coverageType") String coverageType,
                                               @RequestBody PolicyHolderEntity policyHolder) {
        logger.info("Assigning policy holder to a coverage[" + "] type[" + coverageType + "]");
        ResponseEntity<ApiResponse> serviceResponse = policyHolderDownstreamDownstream.addPolicyHolderToCoverage(coverageType, policyHolder);
        return serviceResponse;
    }

    @PostMapping("/search-holder-id/{policyHolderId}/{coverageType}")
    public HttpEntity<ApiResponse> searchPolicyHolder(@PathVariable("policyHolderId") String policyHolderId,
                                             @PathVariable("coverageType") String coverageType) {
        logger.info("Finding policy holder id [" + policyHolderId + "]");
        ResponseEntity<ApiResponse> serviceResponse = policyHolderDownstreamDownstream.searchPolicyHolder(policyHolderId, coverageType);
        return serviceResponse;
    }

    @PostMapping("/search-by-user-id")
    public HttpEntity<ApiResponse> searchPolicyHolder(@RequestBody UserId userId) {
        logger.info("Finding policy user [" + userId + "]");
        ResponseEntity<ApiResponse> serviceResponse = policyHolderDownstreamDownstream.searchPolicyHolder(userId);
        return serviceResponse;
    }


    @RolesAllowed("ROLE_MEDICAL_MANAGE_POLICY_HOLDER")
    @PostMapping("/remove/{holderId}/{coverageType}/{medicalCoverageId}/{planId}")
    public HttpEntity<ApiResponse> removeCoverage(@PathVariable("holderId") String holderId,
                                         @PathVariable("coverageType") String coverageType,
                                         @PathVariable("medicalCoverageId") String medicalCoverageId,
                                         @PathVariable("planId") String planId) {

        logger.info("Removing policy holder from coverage holderId[" + holderId
                + "] coverageType[" + coverageType
                + "] medicalCoverageId[" + medicalCoverageId
                + "] planId[" + planId + "]");

        //todo might be able to remove all parameters except the holderId, need to get back to this logic
        ResponseEntity<ApiResponse> serviceResponse = policyHolderDownstreamDownstream.removePolicyHolderToCoverage(holderId, coverageType, medicalCoverageId, planId);
        return serviceResponse;
    }
}
