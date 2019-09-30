package com.ilt.cms.db.service.clinic.inventory;

import com.ilt.cms.core.entity.supplier.Supplier;
import com.ilt.cms.database.clinic.inventory.SupplierDatabaseService;
import com.ilt.cms.repository.clinic.inventory.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoSupplierDatabaseService implements SupplierDatabaseService {

    private SupplierRepository supplierRepository;

    public MongoSupplierDatabaseService(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }
    @Override
    public List<Supplier> listAll() {
        return supplierRepository.findAll();
    }

    @Override
    public boolean exists(String sId) {
        return supplierRepository.existsById(sId);
    }

    @Override
    public List<Supplier> findAllByNameRegex(String nameRegex) {
        return supplierRepository.findAllByNameRegex(nameRegex);
    }

}
