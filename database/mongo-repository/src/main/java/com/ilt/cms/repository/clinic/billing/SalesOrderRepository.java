package com.ilt.cms.repository.clinic.billing;

import com.ilt.cms.core.entity.sales.SalesOrder;
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

}
