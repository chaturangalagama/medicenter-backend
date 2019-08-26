package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.casem.CaseEntity;
import com.ilt.cms.api.entity.casem.SalesOrderEntity;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.lippo.cms.container.CaseSearchParams;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CaseDownstream {

    ResponseEntity<ApiResponse> listAll();

    ResponseEntity<ApiResponse> listAll(String clinicId, CaseSearchParams searchParams);

    ResponseEntity<ApiResponse> listAll(String clinicId, int page, int size, CaseSearchParams searchParams);

    ResponseEntity<ApiResponse> findByCaseId(String caseId);

    ResponseEntity<ApiResponse> createCase(CaseEntity caseEntity);

    ResponseEntity<ApiResponse> updateCase(String caseId, CaseEntity caseEntity);

    ResponseEntity<ApiResponse> getCasePackage(String caseId);

    ResponseEntity<ApiResponse> updateCasePackage(String caseId, String packageItemId);

    ResponseEntity<ApiResponse> getSalesOrder(String caseId);

    ResponseEntity<ApiResponse> updateSalesOrder(String caseId, SalesOrderEntity salesOrderEntity);

    ResponseEntity<ApiResponse> closeCase(String caseId);

    ResponseEntity<ApiResponse> updateCaseSingleVisit(String caseId, String state);

    ResponseEntity<ApiResponse> getItemChangePriceCalculation(String caseId, ItemChargeDetail.ItemChargeRequest caseItemPriceRequests);

    ResponseEntity<ApiResponse> updateCaseMedicalCoverages(String caseId, List<String> planIds);
}