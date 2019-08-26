package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.downstream.DiagnosisDownstream;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/diagnosis")
//@RolesAllowed("ROLE_VIEW_DIAGNOSIS")
public class DiagnosisRestController {
    private static final Logger logger = LoggerFactory.getLogger(DiagnosisRestController.class);
    private DiagnosisDownstream diagnosisDownstream;

    public DiagnosisRestController(DiagnosisDownstream diagnosisDownstream) {
        this.diagnosisDownstream = diagnosisDownstream;
    }

    @PostMapping("/search")
    public HttpEntity<ApiResponse> searchById(@RequestBody List<String> diagnosisIds) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("searching for diagnosis [" + name + "] by id [" + diagnosisIds + "]");
        ResponseEntity<ApiResponse> serviceResponse = diagnosisDownstream.searchById(diagnosisIds);
        return serviceResponse;
    }

    @PostMapping("/search/{term}")
    public HttpEntity<ApiResponse> listAll(@PathVariable("term") String term, @RequestBody List<String> planIds) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("searching for diagnosis [" + name + "] name [" + term + "]");
        ResponseEntity<ApiResponse> serviceResponse = diagnosisDownstream.search(planIds, term);
        return serviceResponse;
    }
}
