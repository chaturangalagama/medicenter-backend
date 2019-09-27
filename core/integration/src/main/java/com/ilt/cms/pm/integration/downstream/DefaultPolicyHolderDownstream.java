package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.coverage.PolicyHolderEntity;


import com.ilt.cms.api.entity.common.UserId;
import com.ilt.cms.api.payload.ApiPolicyHolderResponse;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.PolicyHolder;

import com.ilt.cms.downstream.PolicyHolderDownstream;
import com.ilt.cms.pm.business.service.coverage.MedicalCoverageService;
import com.ilt.cms.pm.business.service.coverage.PolicyHolderService;
import com.ilt.cms.pm.integration.mapper.PolicyHolderMapper;
import com.ilt.cms.pm.integration.mapper.UserIdMapper;
import com.lippo.cms.exception.PolicyHolderException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultPolicyHolderDownstream implements PolicyHolderDownstream {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPolicyHolderDownstream.class);

    private PolicyHolderService policyHolderService;
    private MedicalCoverageService medicalCoverageService;

    public DefaultPolicyHolderDownstream(PolicyHolderService policyHolderService, MedicalCoverageService medicalCoverageService){
        this.policyHolderService = policyHolderService;
        this.medicalCoverageService = medicalCoverageService;
    }
    @Override
    public ResponseEntity<ApiResponse> addPolicyHolderToCoverage(String coverageType, PolicyHolderEntity policyHolderEntity) {
        PolicyHolder policyHolder = null;
        try {
            policyHolder = policyHolderService.addPolicyHolderToCoverage(coverageType, PolicyHolderMapper.mapToCore(policyHolderEntity));
            return httpApiResponse(new HttpApiResponse(PolicyHolderMapper.mapToEntity(policyHolder)));
        } catch (PolicyHolderException e) {
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchPolicyHolder(String policyHolderId, String coverageType) {
        try {
            PolicyHolder policyHolder = policyHolderService.searchPolicyHolder(policyHolderId, coverageType);
            return httpApiResponse(new HttpApiResponse(PolicyHolderMapper.mapToEntity(policyHolder)));
        } catch (PolicyHolderException e) {
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> searchPolicyHolder(UserId userId) {
        try {
            Map<MedicalCoverage.CoverageType, List<? extends PolicyHolder>> coverageTypeListMap = policyHolderService.searchPolicyHolder(UserIdMapper.mapToCore(userId));
            Map<MedicalCoverage.CoverageType, List<ApiPolicyHolderResponse>> coverageTypeApiPolicyHolderResponseMap = new HashMap<>();
            coverageTypeListMap.entrySet().stream().forEach(e ->{
                coverageTypeApiPolicyHolderResponseMap.put(e.getKey(), e.getValue().stream().map(this::convertToApiPolicyHolderResponse).collect(Collectors.toList()));

            });

            return httpApiResponse(new HttpApiResponse(coverageTypeApiPolicyHolderResponseMap));
        } catch (PolicyHolderException e) {
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> removePolicyHolderToCoverage(String holderId, String coverageType, String medicalCoverageId, String planId) {
        policyHolderService.removePolicyHolderToCoverage(holderId, coverageType, medicalCoverageId, planId);
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    private ApiPolicyHolderResponse convertToApiPolicyHolderResponse(PolicyHolder policyHolder) {
        MedicalCoverage medicalCoverage = medicalCoverageService.findPlanByMedicalCoverageId(policyHolder.getMedicalCoverageId());

        return new ApiPolicyHolderResponse(PolicyHolderMapper.mapToEntity(policyHolder), medicalCoverage.getName(),
                PolicyHolderMapper.mapToCorporateAddressEntity(medicalCoverage.getAddress()),
                PolicyHolderMapper.mapToCoveragePlanEntity(medicalCoverage.findPlan(policyHolder.getPlanId())), medicalCoverage.getContacts().stream().map(PolicyHolderMapper::mapToContactPersonEntity).collect(Collectors.toList()));

    }


}
