package com.ilt.cms.pm.business.service.claim;

import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.claim.ClaimViewCore;
import com.ilt.cms.core.entity.claim.ClaimsBalance;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.lippo.cms.exception.CMSException;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * <code>{@link ClaimService}</code> -
 * Claim Service is the interface implementation for creating, editing, listing, deleting and managing Claims.
 * </p>
 *
 * @author prabath.
 */
public interface ClaimService {

    SalesOrder populateClaims(SalesOrder salesOrder) throws CMSException;


    ClaimsBalance checkBalanceForNric(String clinicId, String planId, String patientNric) throws CMSException;

    List<ClaimViewCore> listClaimsByClinic(String name, String medicalCoverageId, String patientNric, String status, LocalDate startDate, LocalDate endDate, String clinicId) throws CMSException;

    List<ClaimViewCore> listClaimsByClinicByType(String clinicId, String name, MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate) throws CMSException;

    List<ClaimViewCore> listClaimsByTypeForClinicList(List<String> clinicIdList, String name, MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate) throws CMSException;

    ClaimViewCore submitClaim(String claimId, ClaimViewCore claimRequest) throws CMSException;

    ClaimViewCore saveClaim(String claimId, ClaimViewCore claimRequest) throws CMSException;

    ClaimViewCore rejectClaim(String claimId, Claim.ClaimStatus rejectAs) throws CMSException;

    List<ClaimViewCore> checkClaimStatus(String clinicId, List<String> claimIdList) throws CMSException;

    List<ClaimViewCore> checkClaimStatusForClinics(List<String> clinicIdList) throws CMSException;

    ClaimViewCore updateStatus(String claimId,Claim.ClaimStatus updateStatusTo, ClaimViewCore claimRequest) throws CMSException;
}
