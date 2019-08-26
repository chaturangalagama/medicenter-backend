package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalCoverageEntity;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.downstream.MedicalCoverageDownstream;
import com.ilt.cms.pm.business.service.MedicalCoverageService;
import com.ilt.cms.pm.integration.mapper.Mapper;
import com.ilt.cms.pm.integration.mapper.MedicalCoverageMapper;
import com.ilt.cms.repository.spring.MedicalCoverageItemRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;
@Service
public class DefaultMedicalCoverageDownstream implements MedicalCoverageDownstream {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMedicalCoverageDownstream.class);

    private MedicalCoverageService medicalCoverageService;
    private MedicalCoverageItemRepository coverageItemRepostiory;

    public DefaultMedicalCoverageDownstream(MedicalCoverageService medicalCoverageService, MedicalCoverageItemRepository coverageItemRepostiory){
        this.medicalCoverageService = medicalCoverageService;
        this.coverageItemRepostiory = coverageItemRepostiory;
    }

    @Override
    public ResponseEntity<ApiResponse> removeCoverage(String medicalCoverageId) {
        try {
            medicalCoverageService.removeCoverage(medicalCoverageId);

            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));

        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchCoverage(String searchValue, Boolean includePolicyHolderCount) {
        List<MedicalCoverage> medicalCoverages = medicalCoverageService.searchCoverage(searchValue, includePolicyHolderCount);
        return httpApiResponse(new HttpApiResponse(medicalCoverages.stream().map(MedicalCoverageMapper::mapToEntity).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> listAll(Boolean includePolicyHolderCount) {
        List<MedicalCoverage> medicalCoverages = medicalCoverageService.listAll(includePolicyHolderCount);
        return httpApiResponse(new HttpApiResponse(medicalCoverages.stream().map(MedicalCoverageMapper::mapToEntity).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> listByPage(int page, int size, Boolean includePolicyHolderCount) {
        HashMap<String, Object> medicalCoverageMap = medicalCoverageService.list(page, size, includePolicyHolderCount);
        List<MedicalCoverage> medicalCoverages = (List<MedicalCoverage>) medicalCoverageMap.get(CMSConstant.PAYLOAD_KEY_CONTENT);
        medicalCoverageMap.put(CMSConstant.PAYLOAD_KEY_CONTENT, medicalCoverages.stream().map(MedicalCoverageMapper::mapToEntity).collect(Collectors.toList()));
        return httpApiResponse(new HttpApiResponse(medicalCoverageMap));
    }

    @Override
    public ResponseEntity<ApiResponse> listByClinic(String clinicId) {
        List<MedicalCoverage> medicalCoverages = medicalCoverageService.listByClinic(clinicId);
        return httpApiResponse(new HttpApiResponse(medicalCoverages.stream().map(MedicalCoverageMapper::mapToEntity).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> addPlan(String medicalCoverageId, CoveragePlanEntity coveragePlan) {
        try {
            MedicalCoverage medicalCoverage = medicalCoverageService.addNewPlan(medicalCoverageId, MedicalCoverageMapper.mapToCoveragePlanCore(coveragePlan));
            return httpApiResponse(new HttpApiResponse(MedicalCoverageMapper.mapToEntity(medicalCoverage)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> removePlan(String medicalCoverageId, String planId) {
        try {
            medicalCoverageService.removePlan(medicalCoverageId, planId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> modifyCoverage(String medicalCoverageId, MedicalCoverageEntity medicalCoverageEntity) {
        try {
            MedicalCoverage medicalCoverage = medicalCoverageService.modifyMedicalCoverage(medicalCoverageId, MedicalCoverageMapper.mapToCore(medicalCoverageEntity));
            return httpApiResponse(new HttpApiResponse(MedicalCoverageMapper.mapToEntity(medicalCoverage)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatePlan(String medicalCoverageId, String planId, CoveragePlanEntity coveragePlan) {
        try {
            MedicalCoverage medicalCoverage = medicalCoverageService.updateMedicalPlan(medicalCoverageId, planId, MedicalCoverageMapper.mapToCoveragePlanCore(coveragePlan));
            return httpApiResponse(new HttpApiResponse(MedicalCoverageMapper.mapToEntity(medicalCoverage)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

//    @Override
//    public ResponseEntity<ApiResponse> addSchemeToPlan(String medicalCoverageId, String planId, MedicalServiceSchemeEntity medicalServiceScheme) {
//        try {
//            medicalCoverageService.addNewSchemeToPlan(medicalCoverageId, planId, MedicalCoverageMapper.mapToMedicalServiceSchemeCore(medicalServiceScheme));
//            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
//        } catch (CMSException e) {
//            logger.error(e.getCode() + ":"+e.getMessage());
//            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
//        }
//    }

//    @Override
//    public ResponseEntity<ApiResponse> replaceSchemeToPlan(String medicalCoverageId, String planId, List<MedicalServiceSchemeEntity> medicalServiceScheme, boolean included) {
//        try {
//            medicalCoverageService.replaceSchemesOfPlan(medicalCoverageId, planId, medicalServiceScheme.stream().map(MedicalCoverageMapper::mapToMedicalServiceSchemeCore).collect(Collectors.toList()), included);
//            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
//        } catch (CMSException e) {
//            logger.error(e.getCode() + ":"+e.getMessage());
//            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
//        }
//    }

    @Override
    public ResponseEntity<ApiResponse> removeSchemeFromPlan(String medicalCoverageId, String planId, String schemeId) {
        try {
            medicalCoverageService.removeSchemeFromPlan(medicalCoverageId, planId, schemeId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }



    @Override
    public ResponseEntity<ApiResponse> addCoverage(MedicalCoverageEntity medicalCoverageEntity) {
        try {
            MedicalCoverage medicalCoverage = medicalCoverageService.addANewCoverage(MedicalCoverageMapper.mapToCore(medicalCoverageEntity));
            return httpApiResponse(new HttpApiResponse(MedicalCoverageMapper.mapToEntity(medicalCoverage)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public boolean doesPlanExists(String insuranceId, String planId) {
        return medicalCoverageService.doesPlanExists(insuranceId, planId);
    }

    @Override
    public ResponseEntity<ApiResponse> searchByPlanId(String planId) {
        try {
            CoveragePlan medicalCoverage =  medicalCoverageService.findPlanByPlanId(planId);
            return httpApiResponse(new HttpApiResponse(Mapper.mapToCoveragePlanEntity(medicalCoverage)));
        } catch (CMSException e) {
            logger.error("Error while finding coverage plans", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
}
