package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalCoverageEntity;
import com.ilt.cms.api.entity.coverage.MedicalServiceSchemeEntity;
import com.ilt.cms.downstream.MedicalCoverageDownstream;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.util.List;


@RestController
@RequestMapping("/coverage")
//@RolesAllowed("ROLE_MEDICAL_COVERAGE_MANAGEMENT")
public class MedicalCoverageRestController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalCoverageRestController.class);

    private MedicalCoverageDownstream medicalCoverageDownstream;

    public MedicalCoverageRestController(MedicalCoverageDownstream medicalCoverageDownstream) {
        this.medicalCoverageDownstream = medicalCoverageDownstream;
    }

    @PostMapping("/add")
    public ResponseEntity addCoverage(@RequestBody MedicalCoverageEntity medicalCoverage) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Adding a new coverage to the system by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.addCoverage(medicalCoverage);
        return serviceResponse;
    }

    /**
     * You can only remove a medical coverage if its not used by any patients
     *
     * @param medicalCoverageId
     * @return
     */
    @PostMapping("/remove/{medicalCoverageId}")
    public HttpEntity<ApiResponse> removeCoverage(@PathVariable("medicalCoverageId") String medicalCoverageId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("removing coverage from the system by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.removeCoverage(medicalCoverageId);
        return serviceResponse;
    }

    /**
     * Searches by ID or name
     *
     * @param searchValue
     * @return
     */
    @PostMapping("/search/{searchValue}/{includePolicyHolderCount}")
    public HttpEntity<ApiResponse> searchCoverage(@PathVariable("searchValue") String searchValue,
                                         @PathVariable("includePolicyHolderCount") Boolean includePolicyHolderCount) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("finding coverage from the system by [" + name + "] searchValue[" + searchValue + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.searchCoverage(searchValue, includePolicyHolderCount);
        return serviceResponse;
    }

    @PostMapping("/list/all/{includePolicyHolderCount}")
    public HttpEntity<ApiResponse> listAll(@PathVariable("includePolicyHolderCount") Boolean includePolicyHolderCount) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("finding coverage from the system by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.listAll(includePolicyHolderCount);
        return serviceResponse;
    }

    @PostMapping("/list/{page}/{size}/{includePolicyHolderCount}")
    public HttpEntity<ApiResponse> listByPage(@PathVariable("page") int page,
                                     @PathVariable("size") int size,
                                     @PathVariable("includePolicyHolderCount") Boolean includePolicyHolderCount) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("finding coverage from the system by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.listByPage(page, size, includePolicyHolderCount);
        return serviceResponse;
    }
    @PostMapping("/list/clinic/{clinicId}")
    public HttpEntity<ApiResponse> listByClinic(@PathVariable("clinicId") String clinicId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("finding coverage from the system by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.listByClinic(clinicId);
        return serviceResponse;
    }

    @PostMapping("/add/plan/{medicalCoverageId}")
    public HttpEntity<ApiResponse> addPlan(@PathVariable("medicalCoverageId") String medicalCoverageId,
                                  @RequestBody CoveragePlanEntity coveragePlan) {
        logger.info("Adding a new plan to medical coverage [" + medicalCoverageId + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.addPlan(medicalCoverageId, coveragePlan);
        return serviceResponse;
    }

    @PostMapping("/remove/plan/{medicalCoverageId}/{planId}")
    public HttpEntity<ApiResponse> removePlan(@PathVariable("medicalCoverageId") String medicalCoverageId,
                                     @PathVariable("planId") String planId) {
        logger.info("removing plan for medicalCoverageId[" + medicalCoverageId + "] planId[" + planId + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.removePlan(medicalCoverageId, planId);
        return serviceResponse;
    }

    @PostMapping("/update/{medicalCoverageId}")
    public HttpEntity<ApiResponse> modifyCoverage(@PathVariable("medicalCoverageId") String medicalCoverageId,
                                         @RequestBody MedicalCoverageEntity medicalCoverage) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Modifying the medical coverage[" + medicalCoverageId + "] by user[" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.modifyCoverage(medicalCoverageId, medicalCoverage);
        return serviceResponse;

    }

    @PostMapping("/update/plan/{medicalCoverageId}/{coveragePlanId}")
    public HttpEntity<ApiResponse> updatePlan(@PathVariable("medicalCoverageId") String medicalCoverageId,
                                     @PathVariable("coveragePlanId") String planId,
                                     @RequestBody CoveragePlanEntity coveragePlan) {
        logger.info("updating plan[" + planId + "] medicalCoverageId[" + medicalCoverageId + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.updatePlan(medicalCoverageId, planId, coveragePlan);
        return serviceResponse;

    }

//    @PostMapping("/add/plan/scheme/{medicalCoverageId}/{coveragePlanId}")
//    public HttpEntity<ApiResponse> addSchemeToPlan(@PathVariable("medicalCoverageId") String medicalCoverageId,
//                                          @PathVariable("planId") String planId,
//                                          @RequestBody MedicalServiceSchemeEntity medicalServiceScheme) {
//
//        logger.info("Adding new medicalServiceScheme medicalCoverageId[" + medicalCoverageId + "] planId[" + planId + "] medicalServiceScheme[" + medicalServiceScheme + "]");
//        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.addSchemeToPlan(medicalCoverageId, planId, medicalServiceScheme);
//        return serviceResponse;
//
//    }
//
//    @PostMapping("/replace/plan/scheme/{medicalCoverageId}/{coveragePlanId}/{included}")
//    public HttpEntity<ApiResponse> replaceSchemeToPlan(@PathVariable("medicalCoverageId") String medicalCoverageId,
//                                              @PathVariable("planId") String planId,
//                                              @RequestBody List<MedicalServiceSchemeEntity> medicalServiceScheme,
//                                              @PathVariable("included") boolean included) {
//
//        logger.info("replacing medical service scheme medicalCoverageId[" + medicalCoverageId + "] planId[" + planId
//                + "] medicalServiceScheme[" + medicalServiceScheme + "] included[" + included + "]");
//        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.replaceSchemeToPlan(medicalCoverageId, planId, medicalServiceScheme, included);
//        return serviceResponse;
//
//    }

    @PostMapping("/remove/plan/scheme/{medicalCoverageId}/{coveragePlanId}/{schemeId}")
    public HttpEntity<ApiResponse> removeSchemeFromPlan(@PathVariable("medicalCoverageId") String medicalCoverageId,
                                               @PathVariable("planId") String planId,
                                               @PathVariable("schemeId") String schemeId) {
        logger.info("Removing scheme from medicalCoverageId[" + medicalCoverageId + "] planId[" + planId + "] schemeID[" + schemeId + "]");
        ResponseEntity<ApiResponse> serviceResponse = medicalCoverageDownstream.removeSchemeFromPlan(medicalCoverageId, planId, schemeId);
        return serviceResponse;

    }

    @PostMapping("/search/{planId}")
    public ResponseEntity<ApiResponse> searchByPlanId(@PathVariable("planId") String planId) {
        logger.info("Removing scheme from planId [" + planId + "]");
        return medicalCoverageDownstream.searchByPlanId(planId);
    }
}
