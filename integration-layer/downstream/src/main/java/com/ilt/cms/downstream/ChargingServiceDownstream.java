package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.casem.ItemPriceRequest;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.casem.PaymentInfo;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * <code>{@link ChargingServiceDownstream}</code> -
 * The adapter interface for handling Charging related requests.
 * </p>
 *
 * @author prabath.
 */
public interface ChargingServiceDownstream {

    /**
     * <p>
     * Creates invoices for a case.
     * </p>
     *
     * @param caseId
     * @return ResponseEntity<ApiResponse>
     */
    ResponseEntity<ApiResponse> findInvoiceBreakdownForCase(String caseId);

    /**
     * <p>
     * Creates an invoice for a given case.
     * </p>
     *
     * @param caseId
     * @return ResponseEntity<ApiResponse>
     */
    ResponseEntity<ApiResponse> makeDirectPaymentForCase(String caseId, List<PaymentInfo> paymentInfos);

    ResponseEntity<ApiResponse> calculateDueAmountForCase(String caseId);

    ResponseEntity<ApiResponse> deleteInvoice(String caseId, String invoiceIdList, String reason);

    ResponseEntity<ApiResponse> getPaymentModes();

    ResponseEntity<ApiResponse> findInvoiceBreakdownForCase(String caseId, ItemChargeDetail.ItemChargeRequest itemChargeRequest);
}
