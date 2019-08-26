package com.ilt.cms.inventory.db.service;

import com.ilt.cms.inventory.db.repository.spring.common.UOMMatrixRepository;
import com.ilt.cms.inventory.db.service.interfaces.UOMMatrixDatabaseService;
import com.ilt.cms.inventory.model.common.UomMatrix;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoUOMMatrixDatabaseService implements UOMMatrixDatabaseService {

    private UOMMatrixRepository uomMatrixRepository;

    public MongoUOMMatrixDatabaseService(UOMMatrixRepository uomMatrixRepository){
        this.uomMatrixRepository = uomMatrixRepository;
    }
    @Override
    public UomMatrix saveUOMatrix(UomMatrix uomMatrix) {
        return uomMatrixRepository.save(uomMatrix);
    }

    @Override
    public Optional<UomMatrix> findUOMatrixByUomCode(String uomCode) {
        return uomMatrixRepository.findByUomCode(uomCode);
        //return Optional.of(uomMatrix);
    }
}
