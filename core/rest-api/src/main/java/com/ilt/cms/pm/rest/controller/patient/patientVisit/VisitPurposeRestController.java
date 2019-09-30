package com.ilt.cms.pm.rest.controller.patient.patientVisit;

import com.ilt.cms.api.entity.patientVisitRegistry.VisitPurposeEntity;
import com.ilt.cms.downstream.patient.patientVisit.VisitPurposeApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visit-purpose")
//@RolesAllowed("ROLE_VIEW_PURPOSE")
public class VisitPurposeRestController {

    private static final Logger logger = LoggerFactory.getLogger(VisitPurposeRestController.class);
    private VisitPurposeApiService visitPurposeApiService;

    public VisitPurposeRestController(VisitPurposeApiService visitPurposeApiService) {
        this.visitPurposeApiService = visitPurposeApiService;
    }

    @PostMapping("/list/all")
    public HttpEntity<ApiResponse> listAll() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing all purpose for user [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse  = visitPurposeApiService.listAll();
        return serviceResponse;
    }

//    @RolesAllowed("ROLE_VISIT_PURPOSE_MANAGE")
    @PostMapping("/add")
    public HttpEntity<ApiResponse> addClinic(@RequestBody VisitPurposeEntity visitPurposeEntity) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("adding a new visit purpose by[" + name + "] purpose [" + visitPurposeEntity + "]");
        ResponseEntity<ApiResponse> serviceResponse = visitPurposeApiService.addNew(visitPurposeEntity);
        return serviceResponse;
    }

//    @RolesAllowed("ROLE_VISIT_PURPOSE_MANAGE")
    @PostMapping("/modify/{visitPurposeId}")
    public HttpEntity<ApiResponse> modifyClinic(@PathVariable String visitPurposeId, @RequestBody VisitPurposeEntity visitPurposeEntity) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("modifying visitPurpose [" + visitPurposeId + "] by[" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = visitPurposeApiService.modify(visitPurposeId, visitPurposeEntity);
        return serviceResponse;
    }

//    @RolesAllowed("ROLE_VISIT_PURPOSE_MANAGE")
    @PostMapping("/remove/{visitPurposeId}")
    public HttpEntity<ApiResponse> removeClinic(@PathVariable String visitPurposeId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("removing visitPurpose [" + visitPurposeId + "] by[" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = visitPurposeApiService.remove(visitPurposeId);
        return serviceResponse;
    }
}
