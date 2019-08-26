package com.ilt.cms.downstream;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SupplierDownstream {
    ResponseEntity<ApiResponse> listAll();
}