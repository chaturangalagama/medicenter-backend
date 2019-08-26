package com.ilt.cms.pm.rest.controller;


import com.ilt.cms.api.entity.claim.ClaimViewEntity;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.downstream.ClaimServiceDownstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static com.lippo.cms.util.CMSConstant.JSON_DATE_FORMAT;

@RestController
@RequestMapping("/mhcp-claim")
//@RolesAllowed({"ROLE_MHCP_CLAIM"})
public class MhcpClaimManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimManagementController.class);

    @Autowired
    @Qualifier("defaultClaimServiceDownstream")
    private ClaimServiceDownstream claimServiceDownstream;

    //------------------------- balance -------------------------//

    @PostMapping("/balance/{clinicId}/{planId}/{patientNric}")
    public ResponseEntity mhcpBalanceCheck(Principal principal,
                                           @PathVariable("planId") String planId,
                                           @PathVariable("patientNric") String patientNric,
                                           @PathVariable("clinicId") String clinicId) {
        //todo need to integrate the PolicyHolderLimitService properly
        logger.info("Checking for MHCP balance plan[" + planId + "], patientNric[" + patientNric + "]");
        return claimServiceDownstream.checkBalanceForNric(clinicId, planId, patientNric);
    }

    //------------------------- listing -------------------------//

    @RolesAllowed("ROLE_MHCP_LIST")
    @PostMapping("/list/{clinicId}/{medicalCoverageId}/{patientNric}/{status}/{startDate}/{endDate}")
    public ResponseEntity mhcpListClaimByClinic(Principal principal,
                                            @PathVariable("clinicId") String clinicId,
                                            @PathVariable("medicalCoverageId") String medicalCoverageId,
                                            @PathVariable("patientNric") String patientNric,
                                            @PathVariable("status") String status,
                                            @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                            @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("Listing all claims for clinic[" + clinicId + "] [" + medicalCoverageId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsByClinic(principal.getName(), medicalCoverageId, patientNric, status, startDate, endDate, clinicId);
    }

    @RolesAllowed("ROLE_MHCP_LIST")
    @PostMapping("/list/{clinicId}/{medicalCoverageId}/{status}/{startDate}/{endDate}")
    public ResponseEntity mhcpListClaimByClinic(Principal principal,
                                                @PathVariable("clinicId") String clinicId,
                                                @PathVariable("medicalCoverageId") String medicalCoverageId,
                                                @PathVariable("status") String status,
                                                @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                                @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("Listing all mhcp-claims for clinic[" + clinicId + "] [" + medicalCoverageId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsByClinic(principal.getName(), medicalCoverageId, null, status, startDate, endDate, clinicId);
    }


    @RolesAllowed("ROLE_MHCP_LIST_HQ")
    @PostMapping("/list/{medicalCoverageId}/{status}/{startDate}/{endDate}")
    public ResponseEntity mhcpListAllClinicClaims(Principal principal,
                                                  @PathVariable("medicalCoverageId") String medicalCoverageId,
                                                  @PathVariable("status") String status,
                                                  @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                                  @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("Listing all mhcp-claims for [" + medicalCoverageId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listAllClinicClaims(medicalCoverageId, null, status, startDate, endDate);
    }

    @RolesAllowed("ROLE_MHCP_LIST")
    @PostMapping("/list-by-type/{clinicId}/{medicalCoverageType}/{patientNric}/{status}/{startDate}/{endDate}")
    public ResponseEntity mhcpListClaimByClinicByType(Principal principal,
                                                  @PathVariable("clinicId") String clinicId,
                                                  @PathVariable("medicalCoverageType") String medicalCoverageType,
                                                  @PathVariable("patientNric") String patientNric,
                                                  @PathVariable("status") String status,
                                                  @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                                  @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {

        logger.info("Listing all claims for clinic[" + clinicId + "] [" + medicalCoverageType + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsByClinicByType(principal.getName(),
                MedicalCoverage.CoverageType.valueOf(medicalCoverageType), patientNric, clinicId, status, startDate, endDate);
    }

    @RolesAllowed("ROLE_MHCP_LIST")
    @PostMapping("/list-by-type/{clinicId}/{medicalCoverageType}/{status}/{startDate}/{endDate}")
    public ResponseEntity mhcpListClaimByClinicByType(Principal principal,
                                                      @PathVariable("clinicId") String clinicId,
                                                      @PathVariable("medicalCoverageType") String medicalCoverageType,
                                                      @PathVariable("status") String status,
                                                      @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                                      @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {

        logger.info("Listing all mhcp-claims for clinic[" + clinicId + "] [" + medicalCoverageType + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsByClinicByType(principal.getName(),
                MedicalCoverage.CoverageType.valueOf(medicalCoverageType), null, clinicId, status, startDate, endDate);
    }

    @RolesAllowed("ROLE_MHCP_LIST_HQ")
    @PostMapping("/list-by-type/{medicalCoverageType}/{status}/{startDate}/{endDate}")
    public ResponseEntity mhcpListClaimByType(Principal principal,
                                              @PathVariable("medicalCoverageType") String medicalCoverageType,
                                              @PathVariable("status") String status,
                                              @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                              @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate,
                                              @RequestBody(required = false) List<String> clinicIdList) {

        logger.info("Listing all mhcp-claims for [" + medicalCoverageType + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        if (clinicIdList != null && clinicIdList.size() > 0) {
            return claimServiceDownstream.listClaimsForClinicList(clinicIdList, MedicalCoverage.CoverageType.valueOf(medicalCoverageType), status, startDate, endDate);
        } else {
            return claimServiceDownstream.listClaimsByType(MedicalCoverage.CoverageType.valueOf(medicalCoverageType), status, startDate, endDate);
        }
    }


    //------------------------- submit -------------------------//

    @RolesAllowed("ROLE_MHCP_CLAIM")
    @PostMapping("/submit/{claimId}")
    public ResponseEntity mhcpSubmitClaim(Principal principal,
                                          @RequestBody(required = false) ClaimViewEntity claimRequest,
                                          @PathVariable("claimId") String claimId) {

        logger.info("Submitting claim[" + claimId + "] by[" + principal.getName() + "]");
        return claimServiceDownstream.submitClaim(claimId, claimRequest);
    }


    //------------------------- save -------------------------//

    @RolesAllowed("ROLE_MHCP_CLAIM_SAVE")
    @PostMapping("/save/{claimId}")
    public ResponseEntity mhcpSaveClaim(Principal principal,
                                        @RequestBody ClaimViewEntity claimRequest,
                                        @PathVariable("claimId") String claimId) {

        logger.info("Updating claim [" + claimId + "] by[" + principal.getName() + "]");
        return claimServiceDownstream.saveClaim(claimId, claimRequest);
    }


    //------------------------- update-status -------------------------//

    @RolesAllowed("ROLE_MHCP_CLAIM_SAVE")
    @PostMapping("/update-status/{claimId}/{updateStatusTo}")
    public ResponseEntity mhcpUpdateStatus(Principal principal,
                                           @RequestBody(required = false) ClaimViewEntity claimRequest,
                                           @PathVariable("claimId") String claimId,
                                           @PathVariable("updateStatusTo") Claim.ClaimStatus updateStatusTo) {

        logger.info("Updating status of claim [" + claimId + "] to [" + updateStatusTo + "] by[" + principal.getName() + "]");
        return claimServiceDownstream.updateStatus(claimId, updateStatusTo, claimRequest);
    }


    //------------------------- reject-claim -------------------------//

    @RolesAllowed("ROLE_MHCP_CLAIM_SAVE")
    @PostMapping("/reject/{claimId}/{rejectAs}")
    public ResponseEntity mhcpRejectClaim(Principal principal,
                                          @PathVariable("claimId") String claimId,
                                          @PathVariable("rejectAs") Claim.ClaimStatus rejectAs) {

        logger.info("rejecting claim [" + claimId + "] by[" + principal.getName() + "]");
        return claimServiceDownstream.rejectClaim(claimId, rejectAs);
    }


    //------------------------- claim-status-check -------------------------//

    @RolesAllowed("ROLE_MHCP_CLAIM_SAVE")
    @PostMapping("/status/{clinicId}")
    public ResponseEntity mhcpCheckClaimStatusForClinic(Principal principal,
                                                        @PathVariable("clinicId") String clinicId,
                                                        @RequestBody List<String> claimIdList) {

        logger.info("checking claim status in clinic Id [" + clinicId + "] for [" + claimIdList + "] by [" + principal.getName() + "]");
        return claimServiceDownstream.checkClaimStatus(clinicId, claimIdList);
    }

    @RolesAllowed("ROLE_MHCP_CLAIM_SAVE")
    @PostMapping("/status")
    public ResponseEntity mhcpCheckClaimStatusForClinics(Principal principal,
                                                         @RequestBody List<String> clinicIdList) {

        logger.info("checking claim status by [" + principal.getName() + "]");
        return claimServiceDownstream.checkClaimStatusForClinics(clinicIdList);
    }


}
