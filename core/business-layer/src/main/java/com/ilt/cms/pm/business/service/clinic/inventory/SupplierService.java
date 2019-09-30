package com.ilt.cms.pm.business.service.clinic.inventory;

import com.ilt.cms.core.entity.supplier.Supplier;
import com.ilt.cms.database.clinic.inventory.SupplierDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);
    private SupplierDatabaseService supplierDatabaseService;

    public SupplierService(SupplierDatabaseService supplierDatabaseService) {
        this.supplierDatabaseService = supplierDatabaseService;
    }

    public List<Supplier> listAll() {
        logger.debug("listing all the supplier within the system");
        List<Supplier> suppliers = supplierDatabaseService.listAll();
        logger.info("found [" + suppliers.size() + "]");
        return suppliers;
    }
}
