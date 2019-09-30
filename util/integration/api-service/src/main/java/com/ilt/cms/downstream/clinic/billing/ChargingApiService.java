package com.ilt.cms.downstream.clinic.billing;

import com.ilt.cms.core.entity.sales.PaymentInfo;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * <code>{@link ChargingApiService}</code> -
 * The adapter interface for handling Charging related requests.
 * </p>
 *
 * @author prabath.
 */
public interface ChargingApiService {

    ResponseEntity<ApiResponse> makeDirectPaymentForCase(String caseId, List<PaymentInfo> paymentInfos);

    ResponseEntity<ApiResponse> calculateDueAmountForCase(String caseId);

    ResponseEntity<ApiResponse> deleteInvoice(String caseId, String invoiceIdList, String reason);

    ResponseEntity<ApiResponse> getPaymentModes();
}
