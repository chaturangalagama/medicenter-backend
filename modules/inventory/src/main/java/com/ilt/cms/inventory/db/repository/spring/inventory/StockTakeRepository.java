package com.ilt.cms.inventory.db.repository.spring.inventory;

import com.ilt.cms.inventory.model.inventory.StockTake;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface StockTakeRepository extends MongoRepository<StockTake, String> {
    List<StockTake> findAllByClinicId(String clinicId, Pageable pageable);

    Optional<StockTake> findByStockTakeName(String stockTakeName);

    List<StockTake> findAllByStockTakeStatusOrApproveStatus(String stockTakeStatusStr, String approveStatusStr, Pageable pageable);
}
