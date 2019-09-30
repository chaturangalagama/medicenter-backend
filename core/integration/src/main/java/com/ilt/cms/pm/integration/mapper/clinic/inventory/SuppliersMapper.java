package com.ilt.cms.pm.integration.mapper.clinic.inventory;

import com.ilt.cms.api.entity.supplier.SupplierEntity;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.supplier.Supplier;
import com.ilt.cms.pm.integration.mapper.Mapper;

import java.util.stream.Collectors;

public class SuppliersMapper extends Mapper {

    public static Supplier mapToCore(SupplierEntity supplierEntity){
        if(supplierEntity == null){
            return null;
        }
        Supplier supplier = new Supplier();

        supplier.setId(supplierEntity.getId());
        supplier.setName(supplierEntity.getName());
        if(supplierEntity.getStatus() != null) {
            supplier.setStatus(Status.valueOf(supplierEntity.getStatus().name()));
        }
        if(supplierEntity.getAddress() != null) {
            supplier.setAddress(mapToCorporateAddressCore(supplierEntity.getAddress()));
        }
        supplier.setContacts(supplierEntity.getContacts().stream().map(Mapper::mapToContactPersonCore).collect(Collectors.toList()));

        return supplier;
    }

    public static SupplierEntity mapToEntity(Supplier supplier){
        if(supplier == null){
            return null;
        }
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setId(supplier.getId());
        supplierEntity.setName(supplier.getName());
        if(supplier.getStatus() != null) {
            supplierEntity.setStatus(com.ilt.cms.api.entity.common.Status.valueOf(supplier.getStatus().name()));
        }
        if(supplier.getAddress() != null) {
            supplierEntity.setAddress(mapToCorporateAddressEntity(supplier.getAddress()));
        }
        supplierEntity.setContacts(supplier.getContacts().stream().map(Mapper::mapToContactPersonEntity).collect(Collectors.toList()));
        return supplierEntity;
    }
}
