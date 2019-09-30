package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.api.entity.label.LabelEntity;
import com.ilt.cms.core.entity.label.Label;
import com.ilt.cms.downstream.clinic.LabelApiService;
import com.ilt.cms.pm.business.service.clinic.LabelService;
import com.ilt.cms.pm.integration.mapper.clinic.LabelMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultLabelApiService implements LabelApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultLabelApiService.class);
    private LabelService labelService;

    public DefaultLabelApiService(LabelService labelService){
        this.labelService = labelService;
    }
    @Override
    public HttpEntity<ApiResponse> listAll() {
        List<Label> labels = labelService.listAll();
        List<LabelEntity> labelEntities = new ArrayList<>();
        labels.stream().forEach(p->labelEntities.add(LabelMapper.mapToEntity(p)));
        return httpApiResponse(new HttpApiResponse(labelEntities));
    }

    @Override
    public HttpEntity<ApiResponse> findByName(String name) {
        Label label = labelService.findByName(name);
        return httpApiResponse(new HttpApiResponse(LabelMapper.mapToEntity(label)));
    }

    @Override
    public HttpEntity<ApiResponse> modify(String labelId, LabelEntity labelEntity) {
        try {
            Label modifyLabel = labelService.modify(labelId, LabelMapper.mapToCore(labelEntity));
            return httpApiResponse(new HttpApiResponse(LabelMapper.mapToEntity(modifyLabel)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }

    }

    @Override
    public HttpEntity<ApiResponse> add(LabelEntity labelEntity) {
        try {
            Label label = labelService.add(LabelMapper.mapToCore(labelEntity));
            return httpApiResponse(new HttpApiResponse(LabelMapper.mapToEntity(label)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
