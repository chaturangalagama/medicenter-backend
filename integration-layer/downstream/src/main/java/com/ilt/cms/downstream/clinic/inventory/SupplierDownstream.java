package com.ilt.cms.downstream.clinic.inventory;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SupplierDownstream {
    ResponseEntity<ApiResponse> listAll();
}