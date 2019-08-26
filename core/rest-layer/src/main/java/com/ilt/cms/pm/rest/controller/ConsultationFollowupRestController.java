package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.downstream.ConsultationFollowupDownstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
@RequestMapping("/consultation-followup")
//@RolesAllowed("ROLE_VIEW_CONSULTATION_FOLLOWUP")
public class ConsultationFollowupRestController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationFollowupRestController.class);
    private ConsultationFollowupDownstream consultationFollowupDownstream;

    public ConsultationFollowupRestController(ConsultationFollowupDownstream consultationFollowupDownstream) {
        this.consultationFollowupDownstream = consultationFollowupDownstream;
    }

    @PostMapping("/search/{followUpId}")
    public ResponseEntity searchConsultationFollowupById(Principal principal, @PathVariable("followUpId") String followUpId) {
        logger.info("Getting ConsultationFollowup id:[" + followUpId + "] for user [" + principal.getName() + "]");
        return consultationFollowupDownstream.searchById(followUpId);
    }

    @PostMapping("/create")
    @RolesAllowed("ROLE_CONSULTATION_FOLLOWUP_MANAGE")
    public ResponseEntity createConsultationFollowup(Principal principal, @RequestBody ConsultationFollowUp followUp) {
        logger.info("Creating ConsultationFollowup for user [" + principal.getName() + "]");
        return consultationFollowupDownstream.createFollowup(followUp);
    }

    @PostMapping("/update/{followUpId}")
    @RolesAllowed("ROLE_CONSULTATION_FOLLOWUP_MANAGE")
    public ResponseEntity updateConsultationFollowup(Principal principal, @PathVariable("followUpId") String followUpId, @RequestBody ConsultationFollowUp followUp) {
        logger.info("Updating ConsultationFollowup id:[" + followUpId + "] for user [" + principal.getName() + "]");
        return consultationFollowupDownstream.updateFollowup(followUpId, followUp);
    }
}
