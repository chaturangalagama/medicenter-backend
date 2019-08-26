package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.claim.ClaimViewEntity;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * <code>{@link ClaimServiceDownstream}</code> -
 * The interface of the adaptation service for Claim related service functionality.
 * </p>
 *
 * @author prabath.
 */
public interface ClaimServiceDownstream {

    /**
     * <p>
     * Creates a Claim for a given invoice. If the invoice already has a claim then returns it.
     * </p>
     *
     * @param invoiceId
     * @return ClaimViewEntity
     */
//    ResponseEntity<ApiResponse> createClaimByInvoiceId(String invoiceId);

    /**
     * <p>
     * Submits a claim manually.
     * </p>
     *
     * @param claimId
     * @return The submitted claim with updated values and status
     */
    ResponseEntity<ApiResponse> submitClaim(String claimId);

    /**
     * <p>
     * Updates a claim by the Claim Id and the Claim to be updated.
     * </p>
     *
     * @param claimId
     * @param claimViewEntity
     * @return An HTTP Response with the updated claim
     */
    ResponseEntity<ApiResponse> updateClaim(String claimId, ClaimViewEntity claimViewEntity);

    /**
     * <p>
     * Finds claims by the invoice id.
     * There is no reason for having pagination related parameters as there's only one Claim per Invoice.
     * </p>
     *
     * @param invoiceId
     * @param startIndex
     * @param pageSize
     * @param searchParameters
     * @return An HTTP Response with the claim for the requested parameter.
     */
    ResponseEntity<ApiResponse> findClaimByInvoiceId(String invoiceId, int startIndex, int pageSize, String searchParameters);

    /**
     * <p>
     * Finds claims for a given set of search parameters. We may also provide a Case Id so we may retrieve Claims per Case.
     * </p>
     *
     * @param startIndex
     * @param pageSize
     * @param caseId
     * @param searchParameters
     * @return An HTTP Response with Claims in accordance with given parameter.
     */
    ResponseEntity<ApiResponse> findClaims(int startIndex, int pageSize, String caseId, String searchParameters);

    ResponseEntity<ApiResponse> checkBalanceForNric(String clinicId, String planId, String nric);

    ResponseEntity<ApiResponse> listClaimsByClinic(String name, String medicalCoverageId, String patientNric, String status, LocalDate startDate, LocalDate endDate, String clinicId);

    ResponseEntity<ApiResponse> listClaimsByClinicByType(String name, MedicalCoverage.CoverageType coverageType, String patientNric, String clinicId, String status, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listAllClinicClaims(String medicalCoverageId, String patientNric, String status, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listClaimsForClinicList(List<String> clinicIdList,MedicalCoverage.CoverageType coverageType, String status, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listClaimsForClinicList(List<String> clinicIdList,MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listClaimsByType(MedicalCoverage.CoverageType coverageType, String status, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> submitClaim(String claimId, ClaimViewEntity claimRequest);

    ResponseEntity<ApiResponse> saveClaim(String claimId, ClaimViewEntity claimRequest);

    ResponseEntity<ApiResponse> rejectClaim(String claimId, Claim.ClaimStatus rejectAs);

    ResponseEntity<ApiResponse> checkClaimStatus(String clinicId, List<String> claimIdList);

    ResponseEntity<ApiResponse> checkClaimStatusForClinics(List<String> clinicIdList);

    ResponseEntity<ApiResponse> updateStatus(String claimId, Claim.ClaimStatus updateStatusTo, ClaimViewEntity claimRequest);
}
