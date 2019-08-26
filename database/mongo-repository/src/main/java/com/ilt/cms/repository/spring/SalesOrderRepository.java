package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.SalesOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesOrderRepository extends MongoRepository<SalesOrder, String> {

    @Query(value = "{'invoices':{ $exists: true, $ne: [] }, 'invoices.visitId': {$in :?0}, 'invoices.invoiceTime':{ $gte: ?1, $lt: ?2} }")
    List<SalesOrder> findSalesOrdersWithInvoicesCreatedInSearchPeriodWithVisitIds(List<String> invoiceVisitIdList,
                                                                                  LocalDateTime searchStartDate,
                                                                                  LocalDateTime searchEndDate);


    @Query("{'invoices.planId' : { $in : ?0}, " +
            "'invoices.invoiceTime' : { $gte: ?1, $lt: ?2},  " +
            "'invoices.claim.claimStatus' : {$in : ['PENDING']}" +
            " }")
    List<SalesOrder> listClaimForSubmission(List<String> autoSubmitMedicalCoverageId, LocalDateTime invoiceDateStart, LocalDateTime invoiceDateEnd);

    @Query("{'invoices.planId' : { $in : ?0}, " +
            "'invoices.invoiceTime' : { $lt: ?1},  " +
            "'invoices.claim.claimStatus' : {$in : ?2}" +
            " }")
    List<SalesOrder> listClaimForStatusCheck(List<String> autoSubmitPlanIdList, LocalDateTime invoiceDate, List<Claim.ClaimStatus> singletonList);
}
