package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.core.entity.supplier.Supplier;
import com.ilt.cms.downstream.SupplierDownstream;
import com.ilt.cms.pm.business.service.SupplierService;
import com.ilt.cms.pm.integration.mapper.SuppliersMapper;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultSupplierDownstream implements SupplierDownstream {

    private SupplierService supplierService;

    public DefaultSupplierDownstream(SupplierService supplierService){
        this.supplierService = supplierService;
    }
    @Override
    public ResponseEntity<ApiResponse> listAll() {
        List<Supplier> suppliers = supplierService.listAll();
        return httpApiResponse(new HttpApiResponse(suppliers.stream().map(SuppliersMapper::mapToEntity).collect(Collectors.toList())));
    }
}
