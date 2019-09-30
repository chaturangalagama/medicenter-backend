package com.ilt.cms.pm.integration.impl.clinic.billing;

import com.ilt.cms.api.entity.charge.InvoiceView;
import com.ilt.cms.core.entity.sales.Invoice;
import com.ilt.cms.core.entity.sales.PaymentInfo;
import com.ilt.cms.downstream.clinic.billing.ChargingApiService;
import com.ilt.cms.pm.business.service.clinic.billing.InvoiceService;
import com.ilt.cms.pm.integration.mapper.clinic.billing.InvoiceMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

/**
 * <p>
 * <code>{@link DefaultChargingApiService}</code> -
 * Default implementation for the charging request adaptation interface.
 * </p>
 *
 * @author prabath.
 */
@Service
public class DefaultChargingApiService implements ChargingApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultChargingApiService.class);

    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;

    public DefaultChargingApiService(InvoiceService invoiceService,
                                     InvoiceMapper invoiceMapper) {
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

}
