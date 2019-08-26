package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * <code>{@link CaseRepositoryCustom}</code> -
 * Repository for finner grain handling of invoices and claims.
 * </p>
 *
 * @author prabath.
 */
public interface CaseRepositoryCustom {

    @Deprecated
    void createClaimByInvoiceId(String invoiceId, Claim claim);

    void updateClaimByInvoiceIdAndClaimId(String invoiceId, Claim claim);

    void updateInvoiceBySalesOrderIdAndInvoiceNumber(String salesOrderId, String invoiceNumber, Invoice invoice);

    void addNewInvoiceToSalesOrderWithId(String salesOrderId, Invoice invoice);

    Optional<Claim> findClaimByClaimId(String claimId);

    Optional<Claim> findClaimByInvoiceId(String invoiceId);

    Optional<Invoice> findInvoiceById(String invoiceId);

    List<Invoice> findAllInvoicesForGivenTimePeriodByPatientId(String patientId, LocalDateTime searchStartDate, LocalDateTime searchEndDate);

    Optional<Invoice> findFirstInvoiceOfPlanForPatient(String patientId, String planId);

    Case findCaseByClaimNo(String claimNumber);

    Case findCaseByClaimId(String claimId);

    String findClinicIdByClaimId(String claimId);

    List<Case> findCasesByMedicalCoveragePlanIdsIn(List<String> medicalCoveragePlanIds, LocalDateTime invoiceDateStart, LocalDateTime invoiceDateEnd);

    List<Case> findCasesByClinicIdAndMedicalCoveragePlanIds(String clinicId, List<String> medicalCoveragePlanIds, LocalDateTime invoiceDateStart, LocalDateTime invoiceDateEnd);
}
