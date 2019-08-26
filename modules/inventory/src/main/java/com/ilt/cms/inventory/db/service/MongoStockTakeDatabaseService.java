package com.ilt.cms.inventory.db.service;

import com.ilt.cms.inventory.db.repository.spring.inventory.StockTakeRepository;
import com.ilt.cms.inventory.db.service.interfaces.StockTakeDatabaseService;
import com.ilt.cms.inventory.model.inventory.StockTake;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeApproveStatus;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoStockTakeDatabaseService implements StockTakeDatabaseService {

    private StockTakeRepository stockTakeRepository;

    public MongoStockTakeDatabaseService(StockTakeRepository stockTakeRepository){
        this.stockTakeRepository = stockTakeRepository;
    }

    @Override
    public StockTake saveStockTake(StockTake stockTake) {
        return stockTakeRepository.save(stockTake);
    }


    @Override
    public List<StockTake> findStockTakeByClinicId(String clinicId, int size, String sortField, Sort.Direction direction){
        Pageable pageable = PageRequest.of(0, size, new Sort(direction, sortField));
        return stockTakeRepository.findAllByClinicId(clinicId, pageable);
    }

    @Override
    public Optional<StockTake> findStockTakeById(String stockTakeId) {
        return stockTakeRepository.findById(stockTakeId);
    }

    @Override
    public Optional<StockTake> findStockTakeByStockTakeName(String stockTakeName) {
        return stockTakeRepository.findByStockTakeName(stockTakeName);
    }

    @Override
    public List<StockTake> findStockTakeByStockTakeStatusOrApproveStatus(StockTakeStatus stockTakeStatus,
                                                                          StockTakeApproveStatus stockTakeApproveStatus,
                                                                          int size, String sortField, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(0, size, new Sort(direction, sortField));
        String stockTakeStatusStr = stockTakeStatus == null ? null : stockTakeStatus.name();
        String approveStatusStr = stockTakeApproveStatus == null ? null : stockTakeApproveStatus.name();
        return stockTakeRepository.findAllByStockTakeStatusOrApproveStatus(stockTakeStatusStr, approveStatusStr, pageable);
    }

}
