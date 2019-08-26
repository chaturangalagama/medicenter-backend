package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.claim.ClaimViewEntity;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.claim.ClaimViewCore;
import com.ilt.cms.core.entity.claim.ClaimsBalance;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.downstream.ClaimServiceDownstream;
import com.ilt.cms.pm.business.service.claim.ClaimService;
import com.ilt.cms.pm.integration.mapper.ClaimMapper;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

/**
 * <p>
 * <code>{@link DefaultClaimServiceDownstream}</code> -
 * Default implementation of the <code>{@link ClaimServiceDownstream}</code>
 * </p>
 *
 * @author prabath.
 */
@Service
@Qualifier("defaultClaimServiceDownstream")
public class DefaultClaimServiceDownstream implements ClaimServiceDownstream {

    private static final Logger logger = LoggerFactory.getLogger(DefaultClaimServiceDownstream.class);

    private final ClaimService claimService;
    private final MedicalCoverageRepository medicalCoverageRepository;
    private ClaimMapper claimMapper;

    @Autowired
    public DefaultClaimServiceDownstream(ClaimService claimService,
                                         MedicalCoverageRepository medicalCoverageRepository,
                                         ClaimMapper claimMapper) {
        this.claimService = claimService;
        this.medicalCoverageRepository = medicalCoverageRepository;
        this.claimMapper = claimMapper;
    }

//

    @Override
    public ResponseEntity<ApiResponse> updateClaim(String claimId, ClaimViewEntity claimViewEntity) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> findClaimByInvoiceId(String invoiceId, int startIndex, int pageSize,
                                                            String searchParameters) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> findClaims(int startIndex, int pageSize, String caseId, String searchParameters) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> checkBalanceForNric(String clinicId, String planId, String nric) {
        logger.info("Request received to get claim balances on clinic [" + clinicId + "] for nric [" + nric + "] with plan [" + planId + "]");
        try {
            ClaimsBalance balance = claimService.checkBalanceForNric(clinicId, planId, nric);
            return httpApiResponse(new HttpApiResponse(balance));
        } catch (CMSException e) {
            logger.error("Error occurred while checking balance on clinic [" + clinicId + "] for nric ["
                    + nric + "] with plan [" + planId + "]", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listClaimsByClinic(String name, String medicalCoverageId, String patientNric, String status, LocalDate startDate, LocalDate endDate, String clinicId) {
        logger.info("Request received to get claims list by clinic, clinicId [" + clinicId + "]");
        try {
            List<ClaimViewCore> claimsByClinic = claimService.listClaimsByClinic(name, medicalCoverageId, patientNric, status, startDate, endDate, clinicId);
            List<ClaimViewEntity> collect = claimsByClinic.stream().map(claimMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(collect));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> listClaimsByClinicByType(String name, MedicalCoverage.CoverageType coverageType, String patientNric,
                                                                String clinicId, String status, LocalDate startDate, LocalDate endDate) {
        logger.info("Request received to get claims list by clinic, clinicId [" + clinicId + "]");
        try {
            List<ClaimViewCore> claimsByClinic = claimService.listClaimsByClinicByType(clinicId, name, coverageType, patientNric, status, startDate, endDate);
            List<ClaimViewEntity> collect = claimsByClinic.stream().map(claimMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(collect));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listAllClinicClaims(String medicalCoverageId, String patientNric, String status, LocalDate startDate, LocalDate endDate) {
        logger.info("Request received to get claims list for all clinics");
        try {
            List<ClaimViewCore> claimsByClinic = claimService.listClaimsByClinic(null, medicalCoverageId, patientNric, status, startDate, endDate, null);
            List<ClaimViewEntity> collect = claimsByClinic.stream().map(claimMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(collect));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listClaimsByType(MedicalCoverage.CoverageType coverageType, String status, LocalDate startDate, LocalDate endDate) {
        logger.info("Request received to get claims by coverage type [" + coverageType + "]");
        try {
            List<ClaimViewCore> claimsByClinic = claimService.listClaimsByClinicByType(null, null,
                    coverageType, null, status, startDate, endDate);
            List<ClaimViewEntity> collect = claimsByClinic.stream().map(claimMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(collect));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listClaimsForClinicList(List<String> clinicIdList, MedicalCoverage.CoverageType coverageType, String status, LocalDate startDate, LocalDate endDate) {
        logger.info("Request received to get claims by coverage type [" + coverageType + "] in the clinic list");
        try {
            List<ClaimViewCore> claimsByClinic = claimService.listClaimsByTypeForClinicList(clinicIdList, null,
                    coverageType, null, status, startDate, endDate);
            List<ClaimViewEntity> collect = claimsByClinic.stream().map(claimMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(collect));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listClaimsForClinicList(List<String> clinicIdList, MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate) {
        logger.info("Request received to get claims by coverage type [" + coverageType + "] in the clinic list");
        try {
            List<ClaimViewCore> claimsByClinic = claimService.listClaimsByTypeForClinicList(clinicIdList, null,
                    coverageType, patientNric, status, startDate, endDate);
            List<ClaimViewEntity> collect = claimsByClinic.stream().map(claimMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(collect));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> submitClaim(String claimId, ClaimViewEntity claimRequest) {
        logger.info("Request received to submit claim [" + claimId + "]");
        try {
            ClaimViewCore claimViewCore = claimService.submitClaim(claimId, claimMapper.mapToCore(claimRequest));
            return httpApiResponse(new HttpApiResponse(claimMapper.mapToApiEntity(claimViewCore)));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> saveClaim(String claimId, ClaimViewEntity claimRequest) {
        logger.info("Request received to save claim [" + claimId + "]");
        try {
            ClaimViewCore claimViewCore = claimService.saveClaim(claimId, claimMapper.mapToCore(claimRequest));
            return httpApiResponse(new HttpApiResponse(claimMapper.mapToApiEntity(claimViewCore)));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> rejectClaim(String claimId, Claim.ClaimStatus rejectAs) {
        logger.info("Request received to reject claim [" + claimId + "]");
        try {
            ClaimViewCore claimViewCore = claimService.rejectClaim(claimId, rejectAs);
            return httpApiResponse(new HttpApiResponse(claimMapper.mapToApiEntity(claimViewCore)));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> checkClaimStatus(String clinicId, List<String> claimIdList) {
        logger.info("Request received to check status of claims");
        try {
            List<ClaimViewCore> claimViewCoreList = claimService.checkClaimStatus(clinicId, claimIdList);
            return httpApiResponse(new HttpApiResponse(claimViewCoreList.stream()
                    .map(claimMapper::mapToApiEntity)
                    .collect(Collectors.toList())));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> checkClaimStatusForClinics(List<String> clinicIdList) {
        logger.info("Request received to check status of claims");
        try {
            List<ClaimViewCore> claimViewCoreList = claimService.checkClaimStatusForClinics(clinicIdList);
            return httpApiResponse(new HttpApiResponse(claimViewCoreList.stream()
                    .map(claimMapper::mapToApiEntity)
                    .collect(Collectors.toList())));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatus(String claimId, Claim.ClaimStatus updateStatusTo, ClaimViewEntity claimRequest) {
        logger.info("Request received to update the status of claim [" + claimId + "]");
        try {
            ClaimViewCore claimViewCore = claimService.updateStatus(claimId, updateStatusTo, claimMapper.mapToCore(claimRequest));
            return httpApiResponse(new HttpApiResponse(claimMapper.mapToApiEntity(claimViewCore)));
        } catch (CMSException e) {
            logger.error("Error", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> submitClaim(String claimId) {
        return null;
    }
}
