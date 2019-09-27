package com.ilt.cms.pm.business.service.billing;

import com.ilt.cms.core.entity.CopayAmount;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeDetailResponse;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesItem;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.pm.business.service.coverage.PolicyHolderLimitService;
import com.ilt.cms.repository.spring.CaseRepository;
import com.ilt.cms.repository.spring.ClinicRepository;
import com.ilt.cms.repository.spring.PatientRepository;
import com.ilt.cms.repository.spring.PatientVisitRegistryRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.Calculations;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewCaseService {

    private static final Logger logger = LoggerFactory.getLogger(NewCaseService.class);
    private CaseRepository caseRepository;
    private PatientRepository patientRepository;
    private ClinicRepository clinicRepository;
    private RunningNumberService runningNumberService;
    private PatientVisitRegistryRepository visitRepository;
    private SalesOrderService salesOrderService;
    private MedicalCoverageRepository medicalCoverageRepository;
    private PriceCalculationService priceCalculationService;
    private PolicyHolderLimitService policyHolderLimitService;

    @Value("${system.gst.value:7}")
    private int systemGstValue;

    public NewCaseService(CaseRepository caseRepository, PatientRepository patientRepository,
                          ClinicRepository clinicRepository, RunningNumberService runningNumberService,
                          PatientVisitRegistryRepository visitRepository, SalesOrderService salesOrderService,
                          MedicalCoverageRepository medicalCoverageRepository, PriceCalculationService priceCalculationService,
                          PolicyHolderLimitService policyHolderLimitService) {

        this.caseRepository = caseRepository;
        this.patientRepository = patientRepository;
        this.clinicRepository = clinicRepository;
        this.runningNumberService = runningNumberService;
        this.visitRepository = visitRepository;
        this.salesOrderService = salesOrderService;
        this.medicalCoverageRepository = medicalCoverageRepository;
        this.priceCalculationService = priceCalculationService;
        this.policyHolderLimitService = policyHolderLimitService;
    }

    public Case createNewCase(Case aCase, boolean singleVisitCase) throws CMSException {
        return createNewCase(aCase, singleVisitCase, null);
    }

    public Case createNewCase(Case aCase, boolean singleVisitCase, String visitId) throws CMSException {
        logger.info("creating new case [" + aCase + "]");
        if (!aCase.areParametersValid()
                || !(patientRepository.existsById(aCase.getPatientId()) || clinicRepository.existsById(aCase.getClinicId()))) {
            logger.error("Case parameters are not valid [" + aCase + "]");
            throw new CMSException(StatusCode.E2000, "Case parameters are not valid");
        }

        return saveNewCase(aCase, singleVisitCase, visitId);
    }

    public Case attachedVisitToCase(String caseId, String visitId) throws CMSException {
        logger.info("attaching visit [" + visitId + "] to case [" + caseId + "]");
        Case aCase = loadCase(caseId);
        if (aCase.isSingleVisit() && aCase.getVisitIds().size() > 0) {
            logger.error("Case id [" + caseId + "] is a single visit case which already has another visit attached to it");
            throw new CMSException(StatusCode.E2000, "Case ID is not allowd to have muliple visits attached");
        }
        PatientVisitRegistry visitRegistry = validateAndLoadPatientVisit(visitId);
        visitRegistry.setAttachedToCase(true);
        visitRegistry.setCaseId(aCase.getId());
        visitRepository.save(visitRegistry);
        aCase.getVisitIds().add(visitId);
        return caseRepository.save(aCase);
    }

    public List<Invoice> generateAndSaveInvoices(String caseId, String visitId, Map<String, Integer> planMaxUsage) throws CMSException {

        logger.info("Generating new invoices [" + caseId + "] visit[" + visitId + "] planMaxUsage[" + planMaxUsage + "]");
        Case aCase = loadCase(caseId);
        SalesOrder salesOrder = aCase.getSalesOrder();
        List<Invoice> invoices = salesOrder.getInvoices();

        if (planMaxUsage == null) {
            logger.warn("Plan max usage is null setting it as empty");
            planMaxUsage = new HashMap<>();
        }
        List<Invoice> invoiceBreakdown = invoiceBreakdown(aCase, new ItemChargeDetailResponse(planMaxUsage, salesOrder.getPurchaseItems().stream()
                .map(salesItem -> new ItemChargeDetail(salesItem.getItemRefId(), salesItem.getPurchaseQty(),
                        new Charge(salesItem.getSellingPrice().getPrice(), salesItem.getSellingPrice().isTaxIncluded()),
                        salesItem.getItemPriceAdjustment(), salesItem.getExcludedCoveragePlanIds()))
                .collect(Collectors.toList())));

        for (Invoice invoice : invoiceBreakdown) {
            invoice.setVisitId(visitId);
        }
        invoices.addAll(invoiceBreakdown);
        SalesOrder updateSalesOrder = salesOrderService.updateSalesOrder(salesOrder);
        return updateSalesOrder.getInvoices();
    }

    public List<Invoice> removeInvoicesForVisit(String caseId, String visitId) throws CMSException {
        logger.info("removing invoices from sales order for visit [" + visitId + "] case [" + caseId + "]");
        Case aCase = loadCase(caseId);
        SalesOrder salesOrder = aCase.getSalesOrder();
        List<Invoice> invoices = salesOrder.getInvoices();

        invoices = invoices.stream()
                .filter(invoice -> !invoice.getVisitId().equals(visitId))
                .collect(Collectors.toList());

        logger.info("original invoice count [" + salesOrder.getInvoices().size() + "] new invoice count [" + invoices.size() + "]");
        salesOrder.setInvoices(invoices);

        return salesOrderService.updateSalesOrder(salesOrder).getInvoices();

    }


    private Case loadCase(String caseId) throws CMSException {
        return caseRepository.findById(caseId).orElseThrow(() -> {
            logger.error("Case id [" + caseId + "] does not exists");
            return new CMSException(StatusCode.E2000, "Invalid case id");
        });
    }

    public PatientVisitRegistry deattachVisitFromCase(String visitId) throws CMSException {
        logger.info("deattaching visit [" + visitId + "] from case");
        PatientVisitRegistry visitRegistry = visitRepository.findById(visitId).orElseThrow(() -> {
            logger.error("Given visit id [" + visitId + "] does not exists in db");
            return new CMSException(StatusCode.E2000, "Invalid visit id");
        });

        Case aCase = caseRepository.findByVisitIdsContains(visitId);
        if (aCase == null) {
            logger.error("Cannot find the case which the visit is attached to [" + visitId + "]");
            throw new CMSException(StatusCode.E2000, "Case not found");
        }
        aCase.setVisitIds(aCase.getVisitIds().stream()
                .filter(s -> !s.equals(visitId))
                .collect(Collectors.toList()));

        logger.debug("visit removed from case, persisting case and visit");
        caseRepository.save(aCase);
        visitRegistry.setAttachedToCase(false);
        return visitRepository.save(visitRegistry);
    }

    public void updateSalesOrder(String caseId, List<SalesItem> salesItems) throws CMSException {
        logger.info("updating case id [" + caseId + "] with [" + salesItems.size() + "] items");
        Case aCase = loadCase(caseId);
        if (aCase.getStatus() == Case.CaseStatus.CLOSED) {
            logger.error("Trying to edit a closed case [" + aCase + "]");
            throw new CMSException(StatusCode.E2000, "Case already closed");
        }
        SalesOrder so = salesOrderService.updateSalesItems(aCase.getSalesOrder().getId(), salesItems);
        logger.debug("Sales order updated [{}]", so);
    }

    public List<Invoice> findCaseInvoices(String caseId) throws CMSException {
        logger.info("finding invoices for case [" + caseId + "]");
        Case aCase = loadCase(caseId);
        List<Invoice> invoices = aCase.getSalesOrder().getInvoices();
        logger.debug("in total [" + invoices.size() + "] loaded");
        return invoices;
    }

    public void processCaseClosure(String caseId) throws CMSException {
        Case aCase = loadCase(caseId);
        SalesOrder salesOrder = aCase.getSalesOrder();
        List<Invoice> invoices = salesOrder.getInvoices();
        if (aCase.isSingleVisit()) {
            int payableAmount = 0;
            int paidAmount = 0;
            int cashRounding = 0;
            boolean allCash = true;
            for (Invoice invoice : invoices) {
                if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                    payableAmount += invoice.getPayableAmount();
                    paidAmount += invoice.getPaidAmount();
                    cashRounding += invoice.getCashAdjustmentRounding();
                    allCash = invoice.getPaymentInfos().stream()
                            .allMatch(paymentInfo -> paymentInfo.getBillMode() == Invoice.PaymentMode.CASH);
                }
            }
            logger.info("Checking fully payable and paid amount for single visit case payableAmount[" + payableAmount
                    + "] paidAmount[" + paidAmount + "] cashRounding[" + cashRounding + "]");

            if (payableAmount == paidAmount) {
                logger.info("fully paid closing case");
                salesOrder.setStatus(SalesOrder.SalesStatus.CLOSED);
                aCase.setStatus(Case.CaseStatus.CLOSED);
                salesOrderService.updateSalesOrder(salesOrder);
                caseRepository.save(aCase);
            } else if (allCash && (payableAmount == (paidAmount + cashRounding))) {
                logger.info("fully cash payment with rounding of [" + cashRounding + "] closing case");
                aCase.setStatus(Case.CaseStatus.CLOSED);
                salesOrder.setStatus(SalesOrder.SalesStatus.CLOSED);
                salesOrderService.updateSalesOrder(salesOrder);
                caseRepository.save(aCase);
            }
        } else {
            logger.info("Case is not a single visit case so not closing");
        }
    }


    public List<Invoice> invoiceBreakdown(String caseId, ItemChargeRequest itemChargeDetails) throws CMSException {
        Case aCase = loadCase(caseId);
        ItemChargeDetailResponse itemChargingMetadata = priceCalculationService.calculateSalesPrice(aCase.getAttachedMedicalCoverages(),
                itemChargeDetails, aCase.getClinicId());
        return invoiceBreakdown(aCase, itemChargingMetadata);
    }

    private List<Invoice> invoiceBreakdown(Case aCase, ItemChargeDetailResponse chargingMetadata) {

        Map<String, Integer> availableLimits = policyHolderLimitService.findAvailableLimits(aCase.getAttachedMedicalCoverages(), aCase.getPatientId());
        logger.info("breaking invoices for items[" + chargingMetadata + "]");

        Map<String, Invoice> invoices = new HashMap<>();

        Set<String> existingInvoices = aCase.getSalesOrder().getInvoices().stream()
                .filter(invoice -> invoice.getInvoiceState() != Invoice.InvoiceState.DELETED)
                .map(Invoice::getPlanId)
                .collect(Collectors.toSet());

        logger.info("There are already [" + existingInvoices.size() + "] exist for Sales Order");
        Invoice directPayment = new Invoice(runningNumberService.generateInvoiceNumber(), Invoice.InvoiceType.DIRECT, null);

        int itemPayableAmount = 0;
        int cashPaidAmount = 0;

        cashPaidAmount = aCase.getSalesOrder().getInvoices().stream()
                .filter(invoice -> invoice.getInvoiceState() != Invoice.InvoiceState.DELETED)
                .filter(invoice -> invoice.getInvoiceState() != Invoice.InvoiceState.INITIAL)
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT)
                .mapToInt(Invoice::getPaidAmount)
                .sum();


        for (ItemChargeDetail itemChargeDetail : chargingMetadata.getChargeDetails()) {

            itemPayableAmount += Calculations.multiplyWithHalfRoundUp(itemChargeDetail.getQuantity(),
                    itemChargeDetail.calculateAdjustedPrice());

            for (AttachedMedicalCoverage coverage : aCase.getAttachedMedicalCoverages()) {

                if (shouldExcludeCoverage(chargingMetadata, existingInvoices, itemChargeDetail, coverage, availableLimits.get(coverage.getPlanId()))) {
                    logger.debug("Item excluded or part of existing invoice so ignoring from calculation or value is set as -1 ["
                            + availableLimits.get(coverage.getPlanId()) + "]");
                    continue;
                }
                Invoice planInvoice = invoices.computeIfAbsent(coverage.getPlanId(),
                        (planId) -> new Invoice(runningNumberService.generateInvoiceNumber(), Invoice.InvoiceType.CREDIT, coverage.getPlanId()));

                int maxLimit = chargingMetadata.getPlanMaxUsage()
                        .computeIfAbsent(coverage.getPlanId(), availableLimits::get);

                itemPayableAmount = calculatePayable(planInvoice, itemPayableAmount, maxLimit);
            }
        }
        if (itemPayableAmount > 0) {
            directPayment.setPayableAmount(directPayment.getPayableAmount() + itemPayableAmount);
        }

        Map<String, CoveragePlan> planMap = aCase.getAttachedMedicalCoverages().stream()
                .map(attachedMedicalCoverage -> medicalCoverageRepository
                        .findMedicalCoverageByPlanId(attachedMedicalCoverage.getPlanId())
                        .findPlan(attachedMedicalCoverage.getPlanId()))
                .collect(Collectors.toMap(CoveragePlan::getId, plan -> plan));

        computeCoPayAndTax(invoices, directPayment, planMap);
        directPayment.priceAdjustmentForCash();
        directPayment.setPayableAmount(directPayment.getPayableAmount() - cashPaidAmount);
        invoices.put("DIRECT", directPayment);
        return new ArrayList<>(invoices.values());
    }

    private boolean shouldExcludeCoverage(ItemChargeDetailResponse chargingMetadata, Set<String> existingInvoices,
                                          ItemChargeDetail itemChargeDetail, AttachedMedicalCoverage coverage,
                                          int coveragePlanAvailableLimit) {
        return itemChargeDetail.getExcludedPlans().contains(coverage.getPlanId())
                || existingInvoices.contains(coverage.getPlanId())
                || chargingMetadata.getPlanMaxUsage().getOrDefault(coverage.getPlanId(), coveragePlanAvailableLimit) < 0;
    }

    private void computeCoPayAndTax(Map<String, Invoice> invoices, Invoice directPayment, Map<String, CoveragePlan> planMap) {

        for (Map.Entry<String, Invoice> invoiceEntry : invoices.entrySet()) {

            CoveragePlan coveragePlan = planMap.get(invoiceEntry.getKey());

            if (coveragePlan.getCopayment() != null) {
                if (coveragePlan.getCopayment().getPaymentType() == CopayAmount.PaymentType.DOLLAR) {
                    invoiceEntry.getValue().setPayableAmount(invoiceEntry.getValue().getPayableAmount() - coveragePlan.getCopayment().getValue());
                    directPayment.setPayableAmount(directPayment.getPayableAmount() + coveragePlan.getCopayment().getValue());
                } else {
                    int copayAmount = Calculations.calculatePercentage(invoiceEntry.getValue().getPayableAmount(),
                            coveragePlan.getCopayment().getValue());
                    invoiceEntry.getValue().setPayableAmount(invoiceEntry.getValue().getPayableAmount() - copayAmount);
                    directPayment.setPayableAmount(directPayment.getPayableAmount() + copayAmount);
                }
            }
            invoiceEntry.getValue().setPlanName(coveragePlan.getName());
            int taxAmount = Calculations.calculatePercentage(invoiceEntry.getValue().getPayableAmount(), systemGstValue);
            invoiceEntry.getValue().setTaxAmount(taxAmount);
            invoiceEntry.getValue().setPayableAmount(invoiceEntry.getValue().getPayableAmount() + taxAmount);
        }

        int taxAmount = Calculations.calculatePercentage(directPayment.getPayableAmount(), systemGstValue);
        directPayment.setTaxAmount(taxAmount);
        directPayment.setPayableAmount(directPayment.getPayableAmount() + taxAmount);
    }

    private int calculatePayable(Invoice planInvoice, int itemPayableAmount, int maxLimit) {

        int currentAmount = planInvoice.getPayableAmount();

        if ((currentAmount + itemPayableAmount > maxLimit) && maxLimit != 0) {
            planInvoice.setPayableAmount(maxLimit);
            return (currentAmount + itemPayableAmount) - maxLimit;
        } else {
            planInvoice.setPayableAmount(currentAmount + itemPayableAmount);
            return 0;
        }
    }


    private Case saveNewCase(Case aCase, boolean singleVisitCase, String visitId) throws CMSException {
        aCase.setCaseNumber(runningNumberService.generateCaseNumber());
        aCase.setSingleVisit(singleVisitCase);
        if (visitId != null) {
            PatientVisitRegistry visitRegistry = validateAndLoadPatientVisit(visitId);
            visitRegistry.setAttachedToCase(true);
            aCase.setSalesOrder(salesOrderService.generateNewSalesOrder(systemGstValue));
            Case persistedCase = caseRepository.save(aCase);
            visitRegistry.setCaseId(persistedCase.getId());
            visitRepository.save(visitRegistry);
            return persistedCase;
        } else {
            aCase.setSalesOrder(salesOrderService.generateNewSalesOrder(systemGstValue));
            return caseRepository.save(aCase);

        }
    }

    private PatientVisitRegistry validateAndLoadPatientVisit(String visitId) throws CMSException {
        PatientVisitRegistry visitRegistry = visitRepository.findById(visitId).orElseThrow(() -> {
            logger.error("Given visit id [" + visitId + "] does not exists in db");
            return new CMSException(StatusCode.E2000, "Invalid visit id");
        });
        if (visitRegistry.isAttachedToCase()) {
            Case ifAlreadyAvailable = caseRepository.findByVisitIdsContains(visitId);
            if (ifAlreadyAvailable != null) {
                logger.error("Visit already attached to case [" + ifAlreadyAvailable.getId() + "]");
                throw new CMSException(StatusCode.E2000, "Visit already attached to another case");
            }
        }
        return visitRegistry;
    }

    public void setSystemGstValue(int systemGstValue) {
        this.systemGstValue = systemGstValue;
    }

    public Case findByCaseId(String caseId) throws CMSException {
        return loadCase(caseId);
    }

    private int computeOriginalPlanPaidAmount(int copay, int policyLimit) {
        return (policyLimit * 100) / (100 - copay);
    }
}
