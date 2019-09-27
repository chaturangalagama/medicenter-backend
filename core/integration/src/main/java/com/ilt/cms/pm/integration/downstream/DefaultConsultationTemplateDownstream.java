package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.downstream.ConsultationTemplateDownstream;
import com.ilt.cms.pm.business.service.doctor.ConsultationTemplateService;
import com.ilt.cms.pm.integration.mapper.ConsultationTemplateMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultConsultationTemplateDownstream implements ConsultationTemplateDownstream {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConsultationTemplateDownstream.class);
    private ConsultationTemplateService consultationTemplateService;

    public DefaultConsultationTemplateDownstream(ConsultationTemplateService consultationTemplateService){
        this.consultationTemplateService = consultationTemplateService;
    }


    @Override
    public ResponseEntity<ApiResponse> loadTemplate(String templateType, String templateId, String id, String doctorId) {
        try {
            String str = consultationTemplateService.loadTemplate(templateType, templateId, id, doctorId);
            return httpApiResponse(new HttpApiResponse((Object)str));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return httpApiResponse(new HttpApiResponse(StatusCode.I5000));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listTemplate(String doctorId) {
        try {
            List<ConsultationTemplate> consultationTemplates = consultationTemplateService.listTemplate(doctorId);
            return httpApiResponse(new HttpApiResponse(consultationTemplates.stream().map(ConsultationTemplateMapper::mapToEntity).collect(Collectors.toList())));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
