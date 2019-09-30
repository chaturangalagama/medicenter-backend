package com.ilt.cms.pm.rest.controller.clinic;

import com.ilt.cms.api.entity.common.Status;
import com.ilt.cms.api.entity.patient.PatientNoteEntity;
import com.ilt.cms.downstream.clinic.PatientNoteDownstream;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/patient-note")
//@RolesAllowed("ROLE_VIEW_PATIENT_NOTES")
public class PatientNoteRestController {
    private static final Logger logger = LoggerFactory.getLogger(PatientNoteRestController.class);
    private PatientNoteDownstream patientNoteDownstream;

    public PatientNoteRestController(PatientNoteDownstream patientNoteDownstream) {
        this.patientNoteDownstream = patientNoteDownstream;
    }

    @PostMapping("/load/{patientId}")
    public HttpEntity<ApiResponse> listAll(@PathVariable("patientId") String patientId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing patient notes by[" + name + "] patientId[" + patientId + "]");

        ResponseEntity<ApiResponse> serviceResponse = patientNoteDownstream.patientNote(patientId);
        return serviceResponse;
    }

    @RolesAllowed("ROLE_MANAGE_PATIENT_NOTES")
    @PostMapping("/new-note/{patientId}")
    public HttpEntity<ApiResponse> newNote(@PathVariable("patientId") String patientId,
                                  @RequestBody PatientNoteEntity.PatientNoteDetails patientNoteDetails) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("adding a new note by[" + name + "] patientId[" + patientId + "] patientNoteDetails[" + patientNoteDetails + "]");

        ResponseEntity<ApiResponse> serviceResponse = patientNoteDownstream.addNewNote(patientId, patientNoteDetails);
        return serviceResponse;
    }

    @RolesAllowed("ROLE_MANAGE_PATIENT_NOTES")
    @PostMapping("/change-state/{patientId}/{patientNoteDetailId}/{status}")
    public HttpEntity<ApiResponse> changeState(@PathVariable("patientId") String patientId,
                                      @PathVariable("patientNoteDetailId") String patientNoteDetailId, @PathVariable("status") Status status) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("changing state by[" + name + "] patientId[" + patientId + "] patientNoteDetailId["
//                + patientNoteDetailId + "] status[" + status + "]");

        ResponseEntity<ApiResponse> serviceResponse = patientNoteDownstream.changeNoteDetailState(patientId, patientNoteDetailId, status);
        return serviceResponse;
    }
}
