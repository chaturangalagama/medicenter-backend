package com.ilt.cms.pm.rest.controller.clinic;


import com.ilt.cms.api.entity.doctor.DoctorEntity;
import com.ilt.cms.downstream.clinic.DoctorApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
//@RolesAllowed("ROLE_VIEW_DOCTOR")
public class DoctorRestController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorRestController.class);

    private DoctorApiService doctorApiService;

    public DoctorRestController(DoctorApiService doctorApiService) {
        this.doctorApiService = doctorApiService;
    }

    @PostMapping("/list/all/{clinicId}")
    public ResponseEntity listAllDoctorsByClinic(@PathVariable("clinicId") String clinicId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing all doctors for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = doctorApiService.listAll(clinicId);
        return (serviceResponse);
    }

    @PostMapping("/list/all")
    public ResponseEntity listAllDoctors() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing all doctors for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = doctorApiService.listAll();
        return serviceResponse;
    }

    @PostMapping("/search/{doctorId}")
    public ResponseEntity searchById(@PathVariable String doctorId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing doctor for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = doctorApiService.searchById(doctorId);
        return serviceResponse;
    }

//    @RolesAllowed("ROLE_DOCTOR_MANAGE")
    @PostMapping("/add")
    public ResponseEntity addDoctor(@RequestBody DoctorEntity doctor) throws NoSuchFieldException, IllegalAccessException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("adding a new Doctor by[" + name + "] doctor details [" + doctor + "]");
        ResponseEntity<ApiResponse> serviceResponse = doctorApiService.addNewDoctor(doctor);
        return serviceResponse;
    }

//    @RolesAllowed("ROLE_DOCTOR_MANAGE")
    @PostMapping("/modify/{doctorId}")
    public ResponseEntity modifyClinic(@PathVariable String doctorId, @RequestBody DoctorEntity doctor)
            throws NoSuchFieldException, IllegalAccessException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("modifying doctor [" + doctorId + "] by[" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = doctorApiService.modify(doctorId, doctor);
        return serviceResponse;
    }

    //todo write the remove doctor api
}
