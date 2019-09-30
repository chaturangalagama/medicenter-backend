package com.ilt.cms.pm.rest.controller.clinic;

import com.ilt.cms.api.entity.clinic.ClinicEntity;
import com.ilt.cms.downstream.clinic.ClinicApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/clinic")
//@RolesAllowed("ROLE_VIEW_CLINIC")
public class ClinicRestController {

    private static final Logger logger = LoggerFactory.getLogger(ClinicRestController.class);

    private ClinicApiService clinicApiService;

    public ClinicRestController(ClinicApiService clinicApiService) {
        this.clinicApiService = clinicApiService;
    }

    @PostMapping("/list/all")
    public HttpEntity<ApiResponse> listAll(ClinicEntity clinicEntity) {
        logger.info("Listing all clinics");
        ResponseEntity<ApiResponse> response = clinicApiService.listAll();
        return response;
    }

    @PostMapping("/search/{clinicId}")
    public HttpEntity<ApiResponse> searchById(@PathVariable String clinicId) {
        logger.info("Searching clinic with clinicId [" + clinicId + "]");
        ResponseEntity<ApiResponse> response = clinicApiService.searchById(clinicId);
        return response;
    }

    @RolesAllowed("ROLE_CLINIC_MANAGE")
    @PostMapping("/add")
    public HttpEntity<ApiResponse> addClinic(@RequestBody ClinicEntity clinic) {
        logger.info("Adding a new clinic [" + clinic + "]");
        ResponseEntity<ApiResponse> response = clinicApiService.addNewClinic(clinic);
        return response;
    }

    @RolesAllowed("ROLE_CLINIC_MANAGE")
    @PostMapping("/modify/{clinicId}")
    public HttpEntity<ApiResponse> modifyClinic(@PathVariable String clinicId, @RequestBody ClinicEntity clinic) {
        logger.info("Modifying clinic with clinicId [" + clinicId + "]");
        ResponseEntity<ApiResponse> response = clinicApiService.modify(clinicId, clinic);
        return response;
    }

    @RolesAllowed("ROLE_CLINIC_MANAGE")
    @PostMapping("/remove/{clinicId}")
    public HttpEntity<ApiResponse> removeClinic(@PathVariable String clinicId) {
        logger.info("Removing clinic with clinicId [" + clinicId + "]");
        ResponseEntity<ApiResponse> response = clinicApiService.remove(clinicId);
        return response;
    }
}
