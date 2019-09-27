package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.allergy.AllergyGroupEntity;
import com.ilt.cms.downstream.AllergyDownstream;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lippo.commons.web.api.ApiResponse;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/allergy-management")
//@RolesAllowed("ROLE_ALLERGY_MANAGEMENT")
public class AllergyGroupRestController {

    private static final Logger logger = LoggerFactory.getLogger(AllergyGroupRestController.class);
    private AllergyDownstream allergyDownstream;

    public AllergyGroupRestController(AllergyDownstream allergyDownstream) {
        this.allergyDownstream = allergyDownstream;
   }

    @PostMapping("/check/allergies/{patientId}")
    public HttpEntity<ApiResponse> checkAllergies(@PathVariable("patientId") String patientId,
                                                  @RequestBody List<String> drugIds) {
        ResponseEntity<ApiResponse> serviceResponse = allergyDownstream.checkAllergies(patientId, drugIds);
        return serviceResponse;
    }

    @PostMapping("/list/groups")
    public HttpEntity<ApiResponse> listGroups() {
        ResponseEntity<ApiResponse> serviceResponse = allergyDownstream.listGroups();
        return serviceResponse;
    }

    @PostMapping("/create/group")
    @RolesAllowed("ROLE_PATIENT_MEDICAL_ALERTS_MODIFY")
    public HttpEntity<ApiResponse> createAllergyGroup(@RequestBody AllergyGroupEntity allergyGroup) {
        logger.info("Creating a new allergy group called");
        ResponseEntity<ApiResponse> serviceResponse = allergyDownstream.createAllergyGroup(allergyGroup);
        return serviceResponse;
    }

    @PostMapping("/modify/group/{allergyGroupId}")
    @RolesAllowed("ROLE_PATIENT_MEDICAL_ALERTS_MODIFY")
    public HttpEntity<ApiResponse> modifyAllergyGroup(@RequestBody AllergyGroupEntity allergyGroup,
                                             @PathVariable("allergyGroupId") String allergyGroupId) {
        logger.info("modifying an allergy group [" + allergyGroup + "]");
        ResponseEntity<ApiResponse> serviceResponse = allergyDownstream.modifyAllergyGroup(allergyGroup, allergyGroupId);
        return serviceResponse;
    }

    @PostMapping("/delete/group/{allergyGroupId}")
    @RolesAllowed("ROLE_PATIENT_MEDICAL_ALERTS_MODIFY")
    public HttpEntity<ApiResponse> deleteAllergyGroup(@PathVariable("allergyGroupId") String allergyGroupId) {
        logger.info("deleting an allergy group [" + allergyGroupId + "]");
        ResponseEntity<ApiResponse> serviceResponse = allergyDownstream.deleteAllergyGroup(allergyGroupId);
        return serviceResponse;
    }
}