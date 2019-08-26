package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * <code>{@link CaseRepositoryImpl}</code> -
 * <code>{@link CaseRepositoryCustom}</code> default implementation.
 * </p>
 *
 * @author prabath.
 */
@Repository
public class CaseRepositoryImpl implements CaseRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(CaseRepositoryImpl.class);

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void createClaimByInvoiceId(String invoiceNumber, Claim claim) {
        final Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("salesOrder.invoices.invoiceNumber").is(invoiceNumber));
        final Update claimUpdate = new Update();
        claimUpdate.set("salesOrder.invoices.$.claim", claim);
        mongoOperations.upsert(updateQuery, claimUpdate, Case.class);
    }

    @Override
    public void updateClaimByInvoiceIdAndClaimId(String invoiceNumber, Claim claim) {
        final Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("invoices")
                .elemMatch(Criteria.where("invoiceNumber").is(invoiceNumber))
                .andOperator(Criteria.where("invoices.claim.claimId").is(claim.getClaimId())));
        final Update claimUpdate = new Update();
        claimUpdate.set("invoices.$.claim", claim);
        mongoOperations.updateFirst(updateQuery, claimUpdate, SalesOrder.class);
    }

    @Override
    public void updateInvoiceBySalesOrderIdAndInvoiceNumber(String salesOrderId, String invoiceNumber, Invoice invoice) {
        final Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("_id").is(salesOrderId)
                .andOperator(Criteria.where("invoices")
                        .elemMatch(Criteria.where("invoiceNumber").is(invoiceNumber))));
        final Update invoiceUpdate = new Update();
        invoiceUpdate.set("invoices.$", invoice);
        mongoOperations.updateFirst(updateQuery, invoiceUpdate, SalesOrder.class);

    }

    @Override
    public void addNewInvoiceToSalesOrderWithId(String salesOrderId, Invoice invoice) {
        final Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("_id").is(salesOrderId));
        final Update invoiceUpdate = new Update();
        invoiceUpdate.addToSet("invoices", invoice);
        mongoOperations.upsert(updateQuery, invoiceUpdate, SalesOrder.class);
    }

    @Override
    public Optional<Claim> findClaimByInvoiceId(String invoiceNumber) {
        return findInvoiceById(invoiceNumber).map(Invoice::getClaim);
    }

    @Override
    public Optional<Invoice> findInvoiceById(String invoiceNumber) {
        if (Objects.isNull(invoiceNumber)) {
            return Optional.empty();
        }
        final Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("invoices")
                .elemMatch(Criteria.where("invoiceNumber").is(invoiceNumber)));
        SalesOrder eligibleSalesOrder = mongoOperations.findOne(findQuery, SalesOrder.class);
        if (eligibleSalesOrder != null && eligibleSalesOrder.getInvoices() != null) {
            return eligibleSalesOrder.getInvoices().stream()
                    .filter(invoice -> invoiceNumber.equals(invoice.getInvoiceNumber()))
                    .findFirst();
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Claim> findClaimByClaimId(String claimId) {
        if (Objects.isNull(claimId)) {
            return Optional.empty();
        }
        final Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("invoices")
                .elemMatch(Criteria.where("claim.claimId").is(claimId)));
        SalesOrder eligibleSalesOrder = mongoOperations.findOne(findQuery, SalesOrder.class);
        if (Objects.nonNull(eligibleSalesOrder) && Objects.nonNull(eligibleSalesOrder.getInvoices())) {
            return eligibleSalesOrder.getInvoices().stream()
                    .filter(invoice -> invoice.getClaim() != null)
                    .map(Invoice::getClaim)
                    .filter(claim -> claimId.equals(claim.getClaimId()))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Invoice> findAllInvoicesForGivenTimePeriodByPatientId(String patientId, LocalDateTime searchStartDate,
                                                                      LocalDateTime searchEndDate) {
        final Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("patientId").is(patientId));
        findQuery.fields().include("salesOrder");
        List<Case> cases = mongoOperations.find(findQuery, Case.class);
        List<Invoice> invoiceList = cases.parallelStream()
                .filter(aCase -> aCase.getSalesOrder() != null && aCase.getSalesOrder().getInvoices() != null)
                .map(aCase -> aCase.getSalesOrder())
                .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .filter(claimableInvoice -> claimableInvoice.getInvoiceTime().isAfter(searchStartDate)
                        && claimableInvoice.getInvoiceTime().isBefore(searchEndDate))
                .collect(Collectors.toList());
        return invoiceList;
    }

    @Override
    public Optional<Invoice> findFirstInvoiceOfPlanForPatient(String patientId, String planId) {
        final Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("patientId").is(patientId));
        findQuery.fields().include("salesOrder");
        List<Case> cases = mongoOperations.find(findQuery, Case.class);
        return cases.parallelStream()
                .filter(aCase -> aCase.getSalesOrder() != null && aCase.getSalesOrder().getInvoices() != null)
                .flatMap(aCase -> aCase.getSalesOrder().getInvoices().stream())
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .filter(creditInvoice -> planId.equals(creditInvoice.getPlanId()))
                .min(Comparator.comparing(Invoice::getInvoiceTime));
    }

    @Override
    public Case findCaseByClaimNo(String claimNumber) {
        SalesOrder salesOrder = retrieveSalesOrderIdWithGivenCriteriaForClaim(claimNumber, "submissionResult.claimNo");
        if (salesOrder == null)
            return null;
        return retrieveCaseWithGivenSalesOrderId(salesOrder.getId());
    }

    @Override
    public Case findCaseByClaimId(String claimId) {
        SalesOrder salesOrder = retrieveSalesOrderIdWithGivenCriteriaForClaim(claimId, "claimId");
        if (salesOrder == null)
            return null;
        return retrieveCaseWithGivenSalesOrderId(salesOrder.getId());

    }

    @Override
    public String findClinicIdByClaimId(String claimId) {
        SalesOrder salesOrder = retrieveSalesOrderIdWithGivenCriteriaForClaim(claimId, "claimId");
        if (salesOrder == null)
            return null;
        final Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("salesOrder").is(salesOrder.getId()));
        findQuery.fields().include("clinicId");
        Case caseForClaim = mongoOperations.findOne(findQuery, Case.class);
        if (caseForClaim != null) {
            return caseForClaim.getClinicId();
        } else {
            return null;
        }
    }

    private Case retrieveCaseWithGivenSalesOrderId(String salesOrderId) {
        final Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("salesOrder").is(salesOrderId));
        return mongoOperations.findOne(findQuery, Case.class);
    }

    private SalesOrder retrieveSalesOrderIdWithGivenCriteriaForClaim(String value, String criteriaField) {
        final Query findSalesOrderQuery = new Query();
        findSalesOrderQuery.addCriteria(Criteria.where("invoices")
                .elemMatch(Criteria.where("claim."+criteriaField).is(value)))
                .fields().include("_id");
        return mongoOperations.findOne(findSalesOrderQuery, SalesOrder.class);
    }

    @Override
    public List<Case> findCasesByMedicalCoveragePlanIdsIn(List<String> medicalCoveragePlanIds, LocalDateTime invoiceDateStart, LocalDateTime invoiceDateEnd) {
        List<SalesOrder> salesOrder = retrieveRelevantSalesOrderIdListInGivenPeriod(invoiceDateStart, invoiceDateEnd);
        if (salesOrder.size() == 0)
            return null;
        final Query findQuery = new Query();
        findQuery
                .addCriteria(Criteria.where("attachedMedicalCoverages").elemMatch(Criteria.where("planId").in(medicalCoveragePlanIds)))
                .addCriteria(Criteria.where("salesOrder").in(salesOrder.stream().map(SalesOrder::getId).collect(Collectors.toList())));
        return mongoOperations.find(findQuery, Case.class);
    }

    @Override
    public List<Case> findCasesByClinicIdAndMedicalCoveragePlanIds(String clinicId, List<String> medicalCoveragePlanIds, LocalDateTime invoiceDateStart, LocalDateTime invoiceDateEnd) {
        List<SalesOrder> salesOrder = retrieveRelevantSalesOrderIdListInGivenPeriod(invoiceDateStart, invoiceDateEnd);
        if (salesOrder.size() == 0)
            return null;
        final Query findQuery = new Query();
        findQuery
                .addCriteria(Criteria.where("clinicId").is(clinicId))
                .addCriteria(Criteria.where("attachedMedicalCoverages").elemMatch(Criteria.where("planId").in(medicalCoveragePlanIds)))
                .addCriteria(Criteria.where("salesOrder").in(salesOrder.stream().map(SalesOrder::getId).collect(Collectors.toList())));
        return mongoOperations.find(findQuery, Case.class);
    }

    private List<SalesOrder> retrieveRelevantSalesOrderIdListInGivenPeriod(LocalDateTime invoiceDateStart, LocalDateTime invoiceDateEnd) {
        final Query findSalesOrderQuery = new Query();
        findSalesOrderQuery.addCriteria(Criteria.where("invoices")
                .elemMatch(Criteria.where("invoiceTime").gt(invoiceDateStart).andOperator(Criteria.where("invoiceTime").lt(invoiceDateEnd))))
                .fields().include("_id");
        return mongoOperations.find(findSalesOrderQuery, SalesOrder.class);
    }
}
