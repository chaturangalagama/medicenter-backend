package com.ilt.cms.pm.integration.impl.clinic.system;

import com.ilt.cms.core.entity.TemporaryStore;
import com.ilt.cms.downstream.clinic.system.TemporaryStoreApiService;
import com.ilt.cms.pm.business.service.clinic.system.TemporaryStoreService;
import com.ilt.cms.pm.integration.mapper.clinic.system.TemporaryStoreMapper;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultTemporaryStoreApiService implements TemporaryStoreApiService {

    private TemporaryStoreService temporaryStoreService;

    public DefaultTemporaryStoreApiService(TemporaryStoreService temporaryStoreService){
        this.temporaryStoreService = temporaryStoreService;
    }
    @Override
    public ResponseEntity<ApiResponse> store(String key, String body) {
        TemporaryStore temporaryStore = temporaryStoreService.storeValue(key, body);
        return httpApiResponse(new HttpApiResponse(TemporaryStoreMapper.mapToEntity(temporaryStore)));
    }

    @Override
    public ResponseEntity<ApiResponse> retrieve(String key) {
        TemporaryStore temporaryStore = temporaryStoreService.retrieveValue(key);
        return httpApiResponse(new HttpApiResponse(TemporaryStoreMapper.mapToEntity(temporaryStore)));
    }
}
