package com.ilt.cms.downstream;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TemporaryStoreDownstream {
    ResponseEntity<ApiResponse> store(String key, String body);

    //ResponseEntity<ApiResponse> removeValue(String key);

    ResponseEntity<ApiResponse> retrieve(String key);
}