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

/**
 * <p>
 * <code>{@link ClaimManagementController}</code> - The REST service interface for CRUD operations
 * and custom pagination backed listing of Claims.
 * </p>
 *
 * @author prabath.
 */
@RestController
@RequestMapping("/claim")
//@RolesAllowed({"ROLE_VIEW_CLAIM"})
public class ClaimManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimManagementController.class);

    @Autowired
    @Qualifier("defaultClaimServiceDownstream")
    private ClaimServiceDownstream claimServiceDownstream;

    //------------------------- balance -------------------------//


    //    @PostMapping("/balance/{clinicId}/{planId}/{patientNric}")
    public ResponseEntity balanceCheck(Principal principal,
                                       @PathVariable("planId") String planId,
                                       @PathVariable("patientNric") String patientNric,
                                       @PathVariable("clinicId") String clinicId) {
        //todo need to implement the logic properly so that the user can check the usage balance for manual claims as well
        logger.info("Checking for MHCP balance plan[" + planId + "], patientNric[" + patientNric + "]");
        return claimServiceDownstream.checkBalanceForNric(clinicId, planId, patientNric);
    }

    //------------------------- listing -------------------------//

    @RolesAllowed("ROLE_CLAIM_LIST")
    @PostMapping("/list/{clinicId}/{medicalCoverageId}/{patientNric}/{status}/{startDate}/{endDate}")
    public ResponseEntity listClaimByClinic(Principal principal,
                                            @PathVariable("clinicId") String clinicId,
                                            @PathVariable("medicalCoverageId") String medicalCoverageId,
                                            @PathVariable("patientNric") String patientNric,
                                            @PathVariable("status") String status,
                                            @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                            @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("Listing all claims for clinic[" + clinicId + "] [" + medicalCoverageId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsByClinic(principal.getName(), medicalCoverageId, patientNric, status, startDate, endDate, clinicId);
    }

    @RolesAllowed("ROLE_CLAIM_LIST")
    @PostMapping("/list/{clinicId}/{medicalCoverageId}/{status}/{startDate}/{endDate}")
    public ResponseEntity listClaimByClinic(Principal principal,
                                            @PathVariable("clinicId") String clinicId,
                                            @PathVariable("medicalCoverageId") String medicalCoverageId,
                                            @PathVariable("status") String status,
                                            @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                            @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("Listing all claims for clinic[" + clinicId + "] [" + medicalCoverageId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsByClinic(principal.getName(), medicalCoverageId, null, status, startDate, endDate, clinicId);
    }

    @RolesAllowed("ROLE_CLAIM_LIST_HQ")
    @PostMapping("/list/{medicalCoverageId}/{status}/{startDate}/{endDate}")
    public ResponseEntity listAllClinicClaims(Principal principal,
                                              @PathVariable("medicalCoverageId") String medicalCoverageId,
                                              @PathVariable("status") String status,
                                              @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                              @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("Listing all claims for [" + medicalCoverageId + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listAllClinicClaims(medicalCoverageId, null, status, startDate, endDate);
    }

    @RolesAllowed("ROLE_CLAIM_LIST")
    @PostMapping("/list-by-type/{clinicId}/{medicalCoverageType}/{patientNric}/{status}/{startDate}/{endDate}")
    public ResponseEntity listClaimByClinicByType(Principal principal,
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


    @RolesAllowed("ROLE_CLAIM_LIST_HQ")
    @PostMapping("/list-by-type/{medicalCoverageType}/{patientNric}/{status}/{startDate}/{endDate}")
    public ResponseEntity listClaimByTypeForGivenClinics(Principal principal,
                                                         @PathVariable("medicalCoverageType") String medicalCoverageType,
                                                         @PathVariable("patientNric") String patientNric,
                                                         @PathVariable("status") String status,
                                                         @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                                         @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate,
                                                         @RequestBody List<String> clinicIdList) {

        logger.info("Listing all claims for [" + medicalCoverageType + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        return claimServiceDownstream.listClaimsForClinicList(clinicIdList,
                MedicalCoverage.CoverageType.valueOf(medicalCoverageType), patientNric, status, startDate, endDate);

    }


    @RolesAllowed("ROLE_CLAIM_LIST_HQ")
    @PostMapping("/list-by-type/{medicalCoverageType}/{status}/{startDate}/{endDate}")
    public ResponseEntity listClaimByType(Principal principal,
                                          @PathVariable("medicalCoverageType") String medicalCoverageType,
                                          @PathVariable("status") String status,
                                          @PathVariable("startDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate startDate,
                                          @PathVariable("endDate") @DateTimeFormat(pattern = JSON_DATE_FORMAT) LocalDate endDate,
                                          @RequestBody(required = false) List<String> clinicIdList) {

        logger.info("Listing all claims for [" + medicalCoverageType + "] startDate[" + startDate + "] endDate[" + endDate + "]");
        if (clinicIdList != null && clinicIdList.size() > 0) {
            return claimServiceDownstream.listClaimsForClinicList(clinicIdList, MedicalCoverage.CoverageType.valueOf(medicalCoverageType), status, startDate, endDate);
        } else {
            return claimServiceDownstream.listClaimsByType(MedicalCoverage.CoverageType.valueOf(medicalCoverageType), status, startDate, endDate);
        }
    }


    //------------------------- save -------------------------//

//    @RolesAllowed("ROLE_CLAIM_SAVE")
    @PostMapping("/save/{claimId}")
    public ResponseEntity saveClaim(Principal principal,
                                    @RequestBody ClaimViewEntity claimRequest,
                                    @PathVariable("claimId") String claimId) {

        logger.info("Updating claim [" + claimId + "] by[" + principal.getName() + "]");
        return claimServiceDownstream.saveClaim(claimId, claimRequest);
    }


    //------------------------- update-status -------------------------//

    @RolesAllowed("ROLE_CLAIM_SAVE")
    @PostMapping("/update-status/{claimId}/{updateStatusTo}")
    public ResponseEntity updateStatus(Principal principal,
                                       @RequestBody(required = false) ClaimViewEntity claimRequest,
                                       @PathVariable("claimId") String claimId,
                                       @PathVariable("updateStatusTo") Claim.ClaimStatus updateStatusTo) {

        logger.info("Updating status of claim [" + claimId + "] to [" + updateStatusTo + "]  by[" + principal.getName() + "]");
        return claimServiceDownstream.updateStatus(claimId, updateStatusTo, claimRequest);
    }


    //------------------------- reject-claim -------------------------//

//    @RolesAllowed("ROLE_CLAIM_SAVE")
    @PostMapping("/reject/{claimId}/{rejectAs}")
    public ResponseEntity rejectClaim(Principal principal,
                                      @PathVariable("claimId") String claimId,
                                      @PathVariable("rejectAs") Claim.ClaimStatus rejectAs) {

        logger.info("rejecting claim [" + claimId + "] by[" + principal.getName() + "]");
        return claimServiceDownstream.rejectClaim(claimId, rejectAs);
    }

}
