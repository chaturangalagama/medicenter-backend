package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.charge.InvoiceView;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.PaymentInfo;
import com.ilt.cms.downstream.ChargingServiceDownstream;
import com.ilt.cms.pm.business.service.billing.InvoiceService;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.pm.business.service.billing.NewCaseService;
import com.ilt.cms.pm.integration.mapper.InvoiceMapper;
import com.ilt.cms.repository.spring.PatientVisitRegistryRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

/**
 * <p>
 * <code>{@link DefaultChargingServiceDownstream}</code> -
 * Default implementation for the charging request adaptation interface.
 * </p>
 *
 * @author prabath.
 */
@Service
public class DefaultChargingServiceDownstream implements ChargingServiceDownstream {

    private static final Logger logger = LoggerFactory.getLogger(DefaultChargingServiceDownstream.class);

    private NewCaseService newCaseService;
    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;

    public DefaultChargingServiceDownstream(NewCaseService newCaseService, InvoiceService invoiceService,
                                            InvoiceMapper invoiceMapper) {
        this.newCaseService = newCaseService;
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    public ResponseEntity<ApiResponse> deleteInvoice(String caseId, String invoiceIdList, String reason) {
        logger.info("Request received to the invoices [" + invoiceIdList + "] for the Case Id [" + caseId + "] " +
                "with a reason [" + reason + "]");
        try {
            List<Invoice> invoiceList = invoiceService.deleteInvoice(caseId, Arrays.asList(invoiceIdList.split(",")), reason);
            List<InvoiceView> invoiceViews = invoiceList.stream().map(invoiceMapper::mapToApiEntity).collect(Collectors.toList());
            return httpApiResponse(new HttpApiResponse(invoiceViews));
        } catch (CMSException e) {
            logger.error("Error occurred while deleting the invoice [" + invoiceIdList + "]", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getPaymentModes() {
        logger.info("Request received to retrieve payment modes");
        return httpApiResponse(new HttpApiResponse(invoiceService.getPaymentModes()));
    }

    @Override
    public ResponseEntity<ApiResponse> makeDirectPaymentForCase(String caseId, List<PaymentInfo> paymentInfos) {

        logger.info("Request received to make a direct payment to the Case Id [" + caseId + "]. " +
                "Paying [" + paymentInfos + "]");
        try {
            List<Invoice> invoiceList = invoiceService.makeDirectPaymentForCase(caseId, paymentInfos);
            return httpApiResponse(new HttpApiResponse(invoiceList));
        } catch (CMSException e) {
            logger.error("Error occurred while making a direct payment", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> calculateDueAmountForCase(String caseId) {
        logger.info("Request received to calculate the due amount for the Case [" + caseId + "]");
        try {
            int dueAmountForCase = invoiceService.calculateDueAmountForCase(caseId);
            return httpApiResponse(new HttpApiResponse(dueAmountForCase));
        } catch (CMSException e) {
            logger.error("Error occurred while calculating the due amount ", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> findInvoiceBreakdownForCase(String caseId) {
        try {

            List<Invoice> invoiceBreakdownForItems = newCaseService.findCaseInvoices(caseId);
            return httpApiResponse(new HttpApiResponse(invoiceBreakdownForItems));
        } catch (CMSException e) {
            logger.error("Error processing invoices", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> findInvoiceBreakdownForCase(String caseId, ItemChargeRequest itemChargeRequest) {
        try {
            for (ItemChargeDetail itemChargeDetail : itemChargeRequest.getChargeDetails()) {
                if (!CommonUtils.isStringValid(itemChargeDetail.getItemId())) {
                    logger.error("Invalid item id for [" + itemChargeDetail + "]");
                    return httpApiResponse(new HttpApiResponse(StatusCode.E2000, "Invalid item id"));
                }
            }
            List<Invoice> invoiceBreakdownForItems = newCaseService.invoiceBreakdown(caseId, itemChargeRequest);
            return httpApiResponse(new HttpApiResponse(invoiceBreakdownForItems));
        } catch (CMSException e) {
            logger.error("Error processing invoices", e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
}
