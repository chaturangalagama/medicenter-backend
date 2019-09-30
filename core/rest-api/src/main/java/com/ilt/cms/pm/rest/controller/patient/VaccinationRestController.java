package com.ilt.cms.pm.rest.controller.patient;

import com.ilt.cms.api.entity.vaccination.VaccinationEntity;
import com.ilt.cms.downstream.patient.VaccinationApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/vaccination")
//@RolesAllowed("ROLE_VACCINATION")
public class VaccinationRestController {
    private static final Logger logger = LoggerFactory.getLogger(VaccinationRestController.class);

    private VaccinationApiService vaccinationApiService;

    public VaccinationRestController(VaccinationApiService vaccinationApiService) {
        this.vaccinationApiService = vaccinationApiService;
    }

    @PostMapping("/list/{page}/{size}")
    public ResponseEntity listVaccinations(
                                           @PathVariable("page") int page, @PathVariable("size") int size) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing vaccination for [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = vaccinationApiService.listVaccines(page, size);
        return serviceResponse;
    }

    @PostMapping("/list/all")
    public ResponseEntity listAllVaccinations() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing vaccination for [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = vaccinationApiService.listAllVaccines();
        return serviceResponse;
    }

    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody VaccinationEntity vaccinationEntity) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Adding a new vaccination to the system vaccination [" + vaccinationEntity + "] by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = vaccinationApiService.addVaccination(vaccinationEntity);
        return serviceResponse;
    }

    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/patient/association/add/{patientId}")
    public ResponseEntity addVaccinationToPatient(
                                                  @PathVariable("patientId") String patientId,
                                                  @RequestBody com.ilt.cms.api.entity.patient.PatientVaccination vaccination) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Adding a new vaccination to patient[" + patientId + "] vaccination [" + vaccination
//                + "] by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = vaccinationApiService.addVaccinationToPatient(patientId, vaccination);
        return serviceResponse;
    }


    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/patient/association/remove/{patientId}/{associationVaccinationId}")
    public ResponseEntity removeVaccinationFromPatient(
                                                       @PathVariable("patientId") String patientId,
                                                       @PathVariable("associationVaccinationId") String associationVaccinationId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("removing vaccination from patient ["
//                + patientId + "] associationVaccinationId[" + associationVaccinationId + "] by [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = vaccinationApiService.removeVaccinationFromPatient(patientId, associationVaccinationId);
        return serviceResponse;
    }

}
