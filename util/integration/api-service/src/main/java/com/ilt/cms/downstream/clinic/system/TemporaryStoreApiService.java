package com.ilt.cms.downstream.clinic.system;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TemporaryStoreApiService {
    ResponseEntity<ApiResponse> store(String key, String body);

    //ResponseEntity<ApiResponse> removeValue(String key);

    ResponseEntity<ApiResponse> retrieve(String key);
}