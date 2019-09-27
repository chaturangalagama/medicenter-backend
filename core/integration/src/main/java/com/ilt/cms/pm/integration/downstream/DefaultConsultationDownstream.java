package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.downstream.ConsultationDownstream;
import com.ilt.cms.pm.business.service.doctor.ConsultationService;
import com.ilt.cms.pm.integration.mapper.ConsultationMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultConsultationDownstream implements ConsultationDownstream {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConsultationDownstream.class);
    private ConsultationService consultationService;

    public DefaultConsultationDownstream(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @Override
    public ResponseEntity<ApiResponse> searchById(String consultationId) {
        try {
            Consultation consultation = consultationService.searchById(consultationId);
            logger.info("Consultation found for [id]:[" + consultationId + "]");
            return httpApiResponse(new HttpApiResponse(ConsultationMapper.mapToEntity(consultation)));
        } catch (CMSException e) {
            logger.error("Consultation search error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchByPatient(String patientId) {
        try {
            List<Consultation> consultations = consultationService.searchByPatientId(patientId);
            List<ConsultationEntity> consultationEntities = new ArrayList<>();
            for (Consultation consultation : consultations) {
                consultationEntities.add(ConsultationMapper.mapToEntity(consultation));
            }
            logger.info("Consultations " + consultations.size() + " found for patient [id]:[" + patientId + "]");
            return httpApiResponse(new HttpApiResponse(consultationEntities));
        } catch (CMSException e) {
            logger.error("Consultations search for patient error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
}
