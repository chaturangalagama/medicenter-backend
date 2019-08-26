package com.ilt.cms.inventory.db.service.interfaces;

import com.ilt.cms.inventory.model.inventory.StockTake;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeApproveStatus;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeStatus;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface StockTakeDatabaseService {
    StockTake saveStockTake(StockTake stockTake);

    List<StockTake> findStockTakeByClinicId(String clinicId, int size, String sortField, Sort.Direction direction);

    Optional<StockTake> findStockTakeById(String stockTakeId);

    Optional<StockTake> findStockTakeByStockTakeName(String stockTakeName);

    List<StockTake> findStockTakeByStockTakeStatusOrApproveStatus(StockTakeStatus stockTakeStatus,
                                                                         StockTakeApproveStatus stockTakeApproveStatus,
                                                                         int size, String sortField, Sort.Direction direction);
}
