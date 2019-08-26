package com.ilt.cms.inventory.db.service.interfaces;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UOMMaster;

import java.util.List;
import java.util.Optional;

public interface UOMDatabaseService {

    UOMMaster saveUom(UOMMaster uom);

    Optional<UOMMaster> findUomByCode(String Code);

    List<UOMMaster> findAll();
}
