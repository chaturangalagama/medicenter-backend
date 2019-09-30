package com.ilt.cms.database.clinic.inventory;

import com.ilt.cms.core.entity.supplier.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierDatabaseService {
    List<Supplier> listAll();

    boolean exists(String sId);

    List<Supplier> findAllByNameRegex(String nameRegex);
}
