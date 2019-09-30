package com.ilt.cms.pm.business.service.clinic.billing;

import com.ilt.cms.core.entity.sales.Invoice;
import com.ilt.cms.core.entity.sales.PaymentInfo;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.ilt.cms.database.clinic.inventory.ItemDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
import com.ilt.cms.repository.clinic.billing.SalesOrderRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ilt.cms.core.entity.sales.Invoice.InvoiceState.DELETED;

@Service
public class InvoiceService {


    private static final Logger logger = LogManager.getLogger(InvoiceService.class);

//    @Value("${system.gst.value:7}")
    private int systemGstValue = 7;

    private SalesOrderRepository salesOrderRepository;
    private RunningNumberService runningNumberService;
    private PatientVisitRepository patientVisitRepository;
    private ItemDatabaseService itemRepository;

    public InvoiceService(
            SalesOrderRepository salesOrderRepository,
            RunningNumberService runningNumberService,
            PatientVisitRepository patientVisitRepository,
            ItemDatabaseService itemRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.runningNumberService = runningNumberService;
        this.patientVisitRepository = patientVisitRepository;
        this.itemRepository = itemRepository;
    }

    public List<Invoice> deleteInvoice(String caseId, List<String> invoiceIds, String reason) throws CMSException {
        logger.info("Deleting invoices [" + invoiceIds + "] of the Case [" + caseId + "] with a reason [" + reason + "]");

//        SalesOrder salesOrder = salesOrderRepository.findById(caseOfInvoice.getSalesOrder().getId())
//                .orElseThrow(() -> {
//                    logger.error("The sales order for the case id [" + caseId + "] cannot be found. " +
//                            "Therefore cannot proceed further and throwing an error.");
//                    return new CMSException(StatusCode.E2004, "No matching SalesOrder found for the  Id [" + caseOfInvoice.getSalesOrder().getId() + "]");
//                });
//        List<Invoice> invoices = salesOrder.getInvoices();
//
//        for (String invoiceId : invoiceIds) {
//            Optional<Invoice> invoiceToBeDeleted = invoices.stream()
//                    .filter(invoice -> invoice.getInvoiceNumber() != null && invoice.getInvoiceNumber().equals(invoiceId))
//                    .findFirst();
//            if (invoiceToBeDeleted.isPresent()) {
//                Invoice invoice = invoiceToBeDeleted.get();
//                logger.debug("Found the invoice to be deleted [{}]", invoice);
//                if (isEligibleForDeletion(invoice)) {
//                    logger.info("Invoice [" + invoiceId + "] state changed from [" + invoice.getInvoiceState() + "] to DELETED");
//                    invoice.setInvoiceState(DELETED);
//                    invoice.setReasonForDelete(reason);
//                } else {
//                    logger.info("The invoice [" + invoiceId + "] is not eligible for deletion");
//                }
//            } else {
//                logger.info("No invoice found for the invoice id [" + invoiceId + "]");
//            }
//        }
//        salesOrderRepository.save(salesOrder);
//        return invoices;
        return null;
    }

    public int calculateDueAmountForCase(String caseId) throws CMSException {
//        logger.info("Calculating due amount for case [" + caseId + "]");
//        Case caseOfInvoice = retrieveValidCase(caseId);
//
//        List<Invoice> invoices = salesOrderRepository.findById(caseOfInvoice.getSalesOrder().getId())
//                .orElseThrow(() -> {
//                    logger.error("The sales order for the case id [" + caseId + "] cannot be found. " +
//                            "Therefore cannot proceed further and throwing an error.");
//                    return new CMSException(StatusCode.E2004, "No matching SalesOrder found for the  Id [" + caseOfInvoice.getSalesOrder().getId() + "]");
//                }).getInvoices();
//        return calculateDueAmount(invoices);
        return 0;
    }

    public Invoice.PaymentMode[] getPaymentModes() {
        Invoice.PaymentMode[] values = Invoice.PaymentMode.values();
        logger.info("Returning payment modes :" + Arrays.toString(values));
        return values;
    }

    public List<Invoice> makeDirectPaymentForCase(String caseId, List<PaymentInfo> paymentInfos) throws CMSException {

//        for (PaymentInfo paymentInfo : paymentInfos) {
//            if (!paymentInfo.areParametersValid()) {
//                logger.error("Payment Info is not valid [" + paymentInfo + "]");
//                throw new CMSException(StatusCode.E2004, "Payment Info is not valid");
//            }
//        }
//        Case caseForVisit = retrieveValidCase(caseId);
//
//        Map<String, Invoice> allDirectInvoices =
//                caseForVisit.getSalesOrder().getInvoices().stream()
//                        .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT)
//                        .collect(Collectors.toMap(Invoice::getInvoiceNumber, o -> o));
//
//
//        for (PaymentInfo paymentInfo : paymentInfos) {
//
//            if (paymentInfo.getInvoiceNumber() == null || paymentInfo.getInvoiceNumber().isEmpty()) {
//                for (Map.Entry<String, Invoice> entry : allDirectInvoices.entrySet()) {
//                    int dueAmount = entry.getValue().getPayableAmount() - entry.getValue().getPaidAmount();
//                    if ((dueAmount > 0) && dueAmount == paymentInfo.getAmount()) {
//                        makeInvoicePayment(paymentInfo, entry.getValue());
//                    }
//                }
//
//            } else {
//                Invoice invoice = allDirectInvoices.get(paymentInfo.getInvoiceNumber());
//                if (invoice == null) {
//                    logger.error("No invoice found for invoice number [" + paymentInfo.getInvoiceNumber() + "]");
//                    throw new CMSException(StatusCode.E2004, "Invoice number is invalid");
//                }
//                makeInvoicePayment(paymentInfo, invoice);
//            }
//        }
//
//        salesOrderRepository.save(caseForVisit.getSalesOrder());
//        return caseForVisit.getSalesOrder().getInvoices();
        return null;
    }

    private void makeInvoicePayment(PaymentInfo paymentInfo, Invoice invoice) throws CMSException {
        int dueAmount = invoice.getPayableAmount() - invoice.getPaidAmount();
        int paidAmount = paymentInfo.getAmount();

        if (paidAmount > dueAmount) {
            logger.error("The payment amount [" + paidAmount + "] exceeds the total due for the invoice [" + dueAmount
                    + "] Creating a direct payment for the due amount");
            throw new CMSException(StatusCode.E2004, "Paid amount is greater than the payable amount");
        }
        logger.info("making payment for [" + paidAmount + "]");

        invoice.setPaidAmount(invoice.getPaidAmount() + paidAmount);
        paymentInfo.setBillTransactionId(CommonUtils.idGenerator());
        invoice.getPaymentInfos().add(paymentInfo);
    }

    private int calculateDueAmount(List<Invoice> invoices) {
        return invoices.stream()
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT)
                .mapToInt(value -> value.getPayableAmount() - value.getPaidAmount())
                .sum();
    }

//    private Case retrieveValidCase(String caseId) throws CMSException {
//        Case caseOfInvoice = caseDatabaseService.findByCaseId(caseId);
//        if (caseOfInvoice == null) {
//            logger.error("The case for the case id [" + caseId + "] cannot be found. " +
//                    "Therefore cannot proceed further and throwing an error.");
//            throw new CMSException(StatusCode.E2004, "No matching Case found for the Case Id [" + caseId + "]");
//        }
//        return caseOfInvoice;
//    }

    private Invoice retrieveValidInvoice(List<Invoice> invoices, String invoiceNumber) throws CMSException {
        Invoice validInvoice = invoices.stream()
                .filter(invoice -> invoice.getInvoiceNumber() == invoiceNumber)
                .findAny()
                .orElse(null);
        if (validInvoice == null) {
            logger.error("The invoice number [" + invoiceNumber + "] cannot be found.");
            throw new CMSException(StatusCode.E1005, "No matching Invoice found for invoice number[" + invoiceNumber + "]");
        }
        if (Invoice.InvoiceType.DIRECT != validInvoice.getInvoiceType()) {
            logger.error("The invoice is not a Direct payment type.");
            throw new CMSException(StatusCode.E1005, "The invoice is not a Direct payment type");
        }
        return validInvoice;
    }

    private boolean isEligibleForDeletion(Invoice invoice) {
        if (invoice.getInvoiceState() == DELETED) {
            return false;
        }
        return invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT;
//                || (invoice.getClaim() == null ||
//                invoice.getClaim().getClaimStatus().isUpdatable());
    }
}
