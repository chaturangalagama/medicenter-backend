package com.ilt.cms.pm.rest.controller.patient.patientVisit.consultation;

import com.ilt.cms.downstream.patient.patientVisit.consultation.ConsultationDownstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/consultation")
//@RolesAllowed("ROLE_VIEW_CONSULTATION")
public class ConsultationRestController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationRestController.class);
    private ConsultationDownstream consultationDownstream;

    public ConsultationRestController(ConsultationDownstream consultationDownstream) {
        this.consultationDownstream = consultationDownstream;
    }

    @PostMapping("/search/{consultationId}")
    public ResponseEntity searchConsultationById(Principal principal, @PathVariable("consultationId") String consultationId) {
        logger.info("Getting Consultation id:[" + consultationId + "] for user [" + principal.getName() + "]");
        return consultationDownstream.searchById(consultationId);
    }

    @PostMapping("/list/{patientId}")
    public ResponseEntity searchConsultationByPatientId(Principal principal, @PathVariable("patientId") String patentId) {
        logger.info("Getting Consultations for patient id:[" + patentId + "] for user [" + principal.getName() + "]");
        return consultationDownstream.searchByPatient(patentId);
    }
}
