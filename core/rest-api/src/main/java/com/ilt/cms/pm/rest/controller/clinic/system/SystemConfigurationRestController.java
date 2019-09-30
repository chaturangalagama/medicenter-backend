package com.ilt.cms.pm.rest.controller.clinic.system;


import com.ilt.cms.downstream.clinic.system.SystemConfigurationApiService;
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

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/system-config")
//@RolesAllowed("ROLE_VIEW_SYSTEM_CONFIG")
public class SystemConfigurationRestController {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigurationRestController.class);

    private SystemConfigurationApiService systemConfigurationApiService;

    public SystemConfigurationRestController(SystemConfigurationApiService systemConfigurationApiService) {
        this.systemConfigurationApiService = systemConfigurationApiService;
    }


    //todo move everything to a configuration file

    @PostMapping("/list/relationships")
    public HttpEntity<ApiResponse> listRelationships() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.debug("Listing all relationships for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = systemConfigurationApiService.listRelationships();
        return serviceResponse;
    }

    @PostMapping("/list/marital-status")
    public HttpEntity<ApiResponse> listMaritalStatus() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.debug("Listing all marital status for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = systemConfigurationApiService.listMaritalStatus();
        return serviceResponse;
    }

    @PostMapping("/list/medical-test-laboratories")
    public HttpEntity<ApiResponse> listMedicalTestLaboratories() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.debug("Listing all relationships for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = systemConfigurationApiService.listMedicalTestLaboratories();
        return serviceResponse;
    }

    @PostMapping("/list/clinic-groups")
    public HttpEntity<ApiResponse> listClinicGroups() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.debug("Listing all clinic groups for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = systemConfigurationApiService.listClinicGroups();
        return serviceResponse;
    }

    @PostMapping("/list/default-labels")
    public HttpEntity<ApiResponse> listDefaultLabel() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.debug("Listing default labels [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = systemConfigurationApiService.listDefaultLabel();
        return serviceResponse;
    }

    @RolesAllowed("ROLE_VIEW_DOCTOR")
    @PostMapping("/list/speciality")
    public HttpEntity<ApiResponse> listDoctorBySpeciality() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.debug("Listing speciality labels [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse
                = systemConfigurationApiService.listDoctorBySpeciality();

        return serviceResponse;
    }

    @PostMapping("/postcode/{code}")
    public HttpEntity<ApiResponse> findPostcode(@PathVariable("code") String code) {
        logger.debug("Finding address for code [" + code + "]");

        ResponseEntity<ApiResponse> serviceResponse
                = systemConfigurationApiService.findPostcode(code);

        return serviceResponse;
    }

    @PostMapping("/list/vitals")
    public HttpEntity<ApiResponse> listVitals() {
        return systemConfigurationApiService.listVitals();
    }

    @PostMapping("/list/vitals/default")
    public HttpEntity<ApiResponse> listDefaultVitals() {
        return systemConfigurationApiService.listVitalsDefault();
    }

    @PostMapping("/list/instructions")
    public HttpEntity<ApiResponse> listInstructions() {
        return systemConfigurationApiService.listInstructions();
    }

    @PostMapping("/list/prescription-dose")
    public HttpEntity<ApiResponse> listPrescriptionDose() {
        return systemConfigurationApiService.listPrescriptionDose();
    }


    @PostMapping("/validate/{type}/{idNumber}")
    public HttpEntity<ApiResponse> validateIdentification(@PathVariable String type, @PathVariable String idNumber) {
        logger.debug("Validating number type[" + type + "] idNumber[" + idNumber + "]");

        ResponseEntity<ApiResponse> serviceResponse = systemConfigurationApiService.validateIdentification(type, idNumber);
        return serviceResponse;
    }

}
