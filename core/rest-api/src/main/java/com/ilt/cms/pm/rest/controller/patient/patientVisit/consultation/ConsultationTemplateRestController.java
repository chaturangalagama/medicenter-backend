package com.ilt.cms.pm.rest.controller.patient.patientVisit.consultation;


import com.ilt.cms.downstream.patient.patientVisit.consultation.ConsultationTemplateApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/consultation-template")
//@RolesAllowed("ROLE_CONSULTATION_TEMPLATE")
public class ConsultationTemplateRestController {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationTemplateRestController.class);
    private ConsultationTemplateApiService consultationTemplateApiService;

    public ConsultationTemplateRestController(ConsultationTemplateApiService consultationTemplateApiService) {
        this.consultationTemplateApiService = consultationTemplateApiService;
    }

    @PostMapping("/load/{templateType}/{templateId}/{doctorId}/{patientId}")
    public HttpEntity<ApiResponse> load(@PathVariable("templateType") String templateType,
                                        @PathVariable("templateId") String templateId,
                                        @PathVariable("doctorId") String doctorId,
                                        @PathVariable("patientId") String patientId) throws IOException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("loading template by [" + name + "] type[" + templateType + "] templateId[" + templateId + "]");

        ResponseEntity<ApiResponse> serviceResponse = consultationTemplateApiService.loadTemplate(templateType, templateId, doctorId, patientId);
        return serviceResponse;
    }

    @PostMapping("/list/{doctorId}")
    public HttpEntity<ApiResponse> listAll(@PathVariable("doctorId") String doctorId) throws IOException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("loading template by [" + name + "] doctorId[" + doctorId + "]");

        ResponseEntity<ApiResponse> serviceResponse =
                consultationTemplateApiService.listTemplate(doctorId);
        return serviceResponse;
    }
}
