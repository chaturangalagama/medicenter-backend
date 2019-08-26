package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.patientVisitRegistry.VisitPurposeEntity;
import com.ilt.cms.core.entity.visit.VisitPurpose;
import com.ilt.cms.downstream.VisitPurposeDownstream;
import com.ilt.cms.pm.business.service.VisitPurposeService;
import com.ilt.cms.pm.integration.mapper.VisitPurposeMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultVisitPurposeDownstream implements VisitPurposeDownstream {
    private static final Logger logger = LoggerFactory.getLogger(DefaultAllergyDownstream.class);

    private VisitPurposeService visitPurposeService;
    public DefaultVisitPurposeDownstream(VisitPurposeService visitPurposeService){
        this.visitPurposeService = visitPurposeService;
    }
    @Override
    public ResponseEntity<ApiResponse> listAll() {
        List<VisitPurpose> visitPurposes = visitPurposeService.listAll();
        return httpApiResponse(new HttpApiResponse(visitPurposes));
    }

    @Override
    public ResponseEntity<ApiResponse> remove(String visitPurposeId) {
        boolean success = visitPurposeService.remove(visitPurposeId);
        if(success) {
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        }else{
            return httpApiResponse(new HttpApiResponse(StatusCode.I5000));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addNew(VisitPurposeEntity visitPurposeEntity) {
        try {
            VisitPurpose visitPurpose = visitPurposeService.addNew(VisitPurposeMapper.mapToCore(visitPurposeEntity));
            return httpApiResponse(new HttpApiResponse(VisitPurposeMapper.mapToEntity(visitPurpose)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> modify(String visitPurposeId, VisitPurposeEntity visitPurposeEntity) {
        try {
            VisitPurpose visitPurpose = visitPurposeService.modify(visitPurposeId, VisitPurposeMapper.mapToCore(visitPurposeEntity));
            return httpApiResponse(new HttpApiResponse(VisitPurposeMapper.mapToEntity(visitPurpose)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
