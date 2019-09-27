package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.vaccination.AssociatedCoverageVaccinationEntity;
import com.ilt.cms.api.entity.vaccination.VaccinationEntity;
import com.ilt.cms.downstream.VaccinationDownstream;
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

    private VaccinationDownstream vaccinationDownstream;

    public VaccinationRestController(VaccinationDownstream vaccinationDownstream) {
        this.vaccinationDownstream = vaccinationDownstream;
    }

    @PostMapping("/list/{page}/{size}")
    public ResponseEntity listVaccinations(
                                           @PathVariable("page") int page, @PathVariable("size") int size) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing vaccination for [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.listVaccines(page, size);
        return serviceResponse;
    }

    @PostMapping("/list/all")
    public ResponseEntity listAllVaccinations() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing vaccination for [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.listAllVaccines();
        return serviceResponse;
    }

    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody VaccinationEntity vaccinationEntity) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Adding a new vaccination to the system vaccination [" + vaccinationEntity + "] by [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.addVaccination(vaccinationEntity);
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
        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.addVaccinationToPatient(patientId, vaccination);
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

        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.removeVaccinationFromPatient(patientId, associationVaccinationId);
        return serviceResponse;
    }



    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/coverage/association/add")
    public ResponseEntity addVaccinationAssociation(
                                                    @RequestBody AssociatedCoverageVaccinationEntity associatedCoverageVaccinationEntity) {
        logger.info("adding a new association to vaccination [" + associatedCoverageVaccinationEntity.getMedicalCoverageId() + "]");
        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.vaccinationAssociation(associatedCoverageVaccinationEntity, false);
        return serviceResponse;


    }

    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/coverage/association/update")
    public ResponseEntity updateVaccinationAssociation(
                                                       @RequestBody AssociatedCoverageVaccinationEntity associatedCoverageVaccinationEntity) {
        logger.info("adding a new association to vaccination [" + associatedCoverageVaccinationEntity.getMedicalCoverageId() + "]");
        ResponseEntity<ApiResponse> serviceResponse = vaccinationDownstream.vaccinationAssociation(associatedCoverageVaccinationEntity, true);
        return serviceResponse;


    }

    @RolesAllowed("ROLE_VACCINATION_MODIFY")
    @PostMapping("/coverage/association/remove/{medicalCoverageId}/{associationCoverageId}")
    public ResponseEntity removeVaccinationAssociation(
                                                       @PathVariable("medicalCoverageId") String medicalCoverageId,
                                                       @PathVariable("associationCoverageId") String associationCoverageId) {
        logger.info("removing association from medical coverage [" + medicalCoverageId + "] associationCoverageId[" + associationCoverageId + "]");
        ResponseEntity<ApiResponse> serviceResponse= vaccinationDownstream.removeAssociation(medicalCoverageId, associationCoverageId);
        return serviceResponse;

    }
}