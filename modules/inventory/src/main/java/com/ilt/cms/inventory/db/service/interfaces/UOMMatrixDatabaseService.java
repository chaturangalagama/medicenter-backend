package com.ilt.cms.inventory.db.service.interfaces;

import com.ilt.cms.inventory.model.common.UomMatrix;

import java.util.Optional;

public interface UOMMatrixDatabaseService {
    UomMatrix saveUOMatrix(UomMatrix uomMatrix);

    Optional<UomMatrix> findUOMatrixByUomCode(String uomCode);
}
