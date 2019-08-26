package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.casem.ItemPriceRequest;
import com.ilt.cms.api.entity.casem.SalesItemEntity;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.casem.PaymentInfo;
import com.ilt.cms.downstream.ChargingServiceDownstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 * <code>{@link ChargingServiceController}</code> -
 * Covers the API for handling charging requests, including invoice creation after visit, item price break down,
 * invoice creation for a given set of items, invoice creation for a given cash or card amount etc.
 * </p>
 *
 * @author prabath.
 */
@RestController
@RequestMapping("/charging")
//@RolesAllowed({"ROLE_VIEW_INVOICE"})
public class ChargingServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ChargingServiceController.class);

    @Autowired
    private ChargingServiceDownstream chargingServiceDownstream;

    @PostMapping("/invoice/breakdown/{caseId}")
    @RolesAllowed({"ROLE_CREATE_INVOICE"})
    public ResponseEntity findInvoiceBreakdownForCase(@PathVariable("caseId") String caseId, Principal principal) {
        logger.info("Request received for calculating invoices for Case Id [" + caseId + "]" + " User [" + principal.getName() + "]");
        return chargingServiceDownstream.findInvoiceBreakdownForCase(caseId);
    }

    @PostMapping("/invoice/breakdown-dynamic/{caseId}")
    @RolesAllowed({"ROLE_CREATE_INVOICE"})
    public ResponseEntity invoiceBreakdownForCase(@RequestBody ItemChargeDetail.ItemChargeRequest itemChargeRequest,
                                                  @PathVariable("caseId") String caseId,
                                                  Principal principal) {
        logger.info("Request received for dynamic calculating invoices for items  [" + itemChargeRequest + "] by [" + principal.getName() + "]" +
                " User [" + principal.getName() + "]");
        return chargingServiceDownstream.findInvoiceBreakdownForCase(caseId, itemChargeRequest);
    }

    @PostMapping("/payment/direct/{caseId}")
    @RolesAllowed({"ROLE_CREATE_INVOICE"})
    public ResponseEntity makePaymentForCase(@PathVariable("caseId") String caseId,@RequestBody List<PaymentInfo> paymentInfos,
                                             Principal principal) {
        logger.info("Request received to make a direct payment to the Case [" + caseId + "]." +
                " paid amount [" + paymentInfos + "]" +
                "User [" + principal.getName() + "]");
        return chargingServiceDownstream.makeDirectPaymentForCase(caseId, paymentInfos);
    }

    @RequestMapping("/payment/due/{caseId}")
    public ResponseEntity calculateDueAmount(@PathVariable("caseId") String caseId,
                                             Principal principal) {
        logger.info("Request received to calculate the due amount for the Case [" + caseId + "]. User [" + principal.getName() + "]");
        return chargingServiceDownstream.calculateDueAmountForCase(caseId);
    }

    @PostMapping("/invoice/delete/{caseId}/{invoiceIds}/{reason}")
    @DeleteMapping("/invoice/{caseId}/{invoiceIds}/{reason}")
    @RolesAllowed({"ROLE_DELETE_INVOICE"})
    public ResponseEntity deleteInvoice(@PathVariable("caseId") String caseId,
                                        @PathVariable("invoiceIds") String invoiceIds,
                                        @PathVariable("reason") String reason,
                                        Principal principal) {
        logger.info("Request received to Delete invoices [" + invoiceIds + "] for the Case [" + caseId + "]. " +
                "User [" + principal.getName() + "]");
        return chargingServiceDownstream.deleteInvoice(caseId, invoiceIds, reason);
    }

    @PostMapping("/payment/modes")
    public ResponseEntity getPaymentModes(Principal principal) {
        logger.info("Request received to retrieve payment modes. User [" + principal.getName() + "]");
        return chargingServiceDownstream.getPaymentModes();
    }

}
