package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.label.LabelEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.HttpEntity;

public interface LabelDownstream {
    HttpEntity<ApiResponse> listAll();

    HttpEntity<ApiResponse> findByName(String name);

    HttpEntity<ApiResponse> modify(String labelId, LabelEntity labelEntity);

    HttpEntity<ApiResponse> add(LabelEntity labelEntity);

}