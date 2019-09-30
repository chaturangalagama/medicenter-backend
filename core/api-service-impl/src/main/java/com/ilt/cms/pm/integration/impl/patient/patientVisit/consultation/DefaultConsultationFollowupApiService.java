package com.ilt.cms.pm.integration.impl.patient.patientVisit.consultation;

import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.downstream.patient.patientVisit.consultation.ConsultationFollowupApiService;
import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationFollowupService;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.consultation.ConsultationFollowUpMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultConsultationFollowupApiService implements ConsultationFollowupApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConsultationFollowupApiService.class);
    private ConsultationFollowupService consultationfollowupService;

    public DefaultConsultationFollowupApiService(ConsultationFollowupService consultationfollowupService) {
        this.consultationfollowupService = consultationfollowupService;
    }

    @Override
    public ResponseEntity<ApiResponse> searchById(String followUpId) {
        try {
            ConsultationFollowup followup = consultationfollowupService.searchById(followUpId);
            logger.info("ConsultationFollowup found for [id]:[" + followUpId + "]");
            return httpApiResponse(new HttpApiResponse(ConsultationFollowUpMapper.mapToEntity(followup)));
        } catch (CMSException e) {
            logger.error("ConsultationFollowup search error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> createFollowup(ConsultationFollowUp followUp) {
        try {
            ConsultationFollowup createdFollowup = consultationfollowupService.createFollowup(ConsultationFollowUpMapper.mapToCore(followUp));
            logger.info("ConsultationFollowup created [id]:[" + createdFollowup.getId() + "]");
            return httpApiResponse(new HttpApiResponse(ConsultationFollowUpMapper.mapToEntity(createdFollowup)));
        } catch (CMSException e) {
            logger.error("ConsultationFollowup creating error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateFollowup(String followUpId, ConsultationFollowUp followUp) {
        try {
            ConsultationFollowup updatedFollowup = consultationfollowupService.updateFollowup(followUpId, ConsultationFollowUpMapper.mapToCore(followUp));
            logger.info("ConsultationFollowup updated [id]:[" + updatedFollowup.getId() + "]");
            return httpApiResponse(new HttpApiResponse(ConsultationFollowUpMapper.mapToEntity(updatedFollowup)));
        } catch (CMSException e) {
            logger.error("ConsultationFollowup updating error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

}
