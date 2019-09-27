package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.core.entity.TemporaryStore;
import com.ilt.cms.downstream.TemporaryStoreDownstream;
import com.ilt.cms.pm.business.service.clinic.TemporaryStoreService;
import com.ilt.cms.pm.integration.mapper.TemporaryStoreMapper;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultTemporaryStoreDownstream implements TemporaryStoreDownstream {

    private TemporaryStoreService temporaryStoreService;

    public DefaultTemporaryStoreDownstream(TemporaryStoreService temporaryStoreService){
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
