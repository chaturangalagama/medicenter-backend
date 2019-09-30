package com.ilt.cms.pm.rest.controller.clinic;

import com.ilt.cms.downstream.clinic.FileManagementApiService;
import com.lippo.cms.util.CMSConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/document-management")
//@RolesAllowed("ROLE_FILE_LIST")
public class FileManagementRestController {

    private static final Logger logger = LoggerFactory.getLogger(FileManagementRestController.class);
    private FileManagementApiService fileManagementApiService;

    public FileManagementRestController(FileManagementApiService fileManagementApiService) {
        this.fileManagementApiService = fileManagementApiService;
    }

    @PostMapping("/upload/visit/{visitId}/{uploadTo}")
    @RolesAllowed("ROLE_FILE_UPLOAD")
    public ResponseEntity uploadToVisit(Principal principal, @PathVariable("visitId") String visitId,
                                        @PathVariable("uploadTo") String uploadTo,
                                        @RequestParam("name") String name,
                                        @RequestParam("fileName") String fileName,
                                        @RequestParam("clinicId") String clinicId,
                                        @RequestParam("description") String description,
                                        @RequestParam("file") MultipartFile uploadFile) {
        logger.info("Uploading document to visit [visitId]:[" + visitId + "] for user [" + principal.getName() +
                "] filename [" + fileName + "] name [" + name + "] clinic [" + clinicId + "] description [" + description + "]");
        return fileManagementApiService.uploadToVisit(uploadTo, visitId, principal.getName(), uploadFile, name, fileName,
                clinicId, description);
    }

    @PostMapping("/list/visit/{visitId}")
    public ResponseEntity listFileByVisit(Principal principal, @PathVariable("visitId") String visitId) {
        logger.info("Listing documents for visit [visitId]:[" + visitId + "] for user [" + principal + "]");
        return fileManagementApiService.listFilesByVisit(visitId);
    }

    @PostMapping("/download/visit/{visitId}/{fileId}")
    @RolesAllowed("ROLE_FILE_DOWNLOAD")
    public ResponseEntity downloadFileFromVisit(Principal principal, @PathVariable("visitId") String visitId, @PathVariable("fileId") String fileId) {
        logger.info("Downloading document [fileId]:[" + fileId + "] for visit [visitId]:[" + visitId + "] for user [" + principal + "]");
        return fileManagementApiService.downloadFileFormVisit(visitId, fileId);
    }

    /*patient*/
    @RequestMapping("/upload/patient/{patientId}")
    @RolesAllowed("ROLE_FILE_UPLOAD")
    public ResponseEntity uploadToPatient(Principal principal,
                                          @PathVariable("patientId") String patientId,
                                          @RequestParam("clinicId") String clinicId,
                                          @RequestParam("name") String name,
                                          @RequestParam("fileName") String fileName,
                                          @RequestParam("description") String description,
                                          @RequestParam("file") MultipartFile uploadFile) {

        logger.info("uploading new file to patient by [" + principal.getName() + "] patientId[" + patientId + "] name["
                + name + "] fileName[" + fileName + "] description[" + description + "]");
        return fileManagementApiService.uploadToPatient(principal, name, clinicId, patientId, fileName, description, uploadFile);
    }

    @RequestMapping("/list/patient/{patientId}/{startDate}/{endDate}")
    @RolesAllowed("ROLE_FILE_LIST")
    public ResponseEntity listAll(Principal principal, @PathVariable("patientId") String patientId,
                                  @PathVariable("startDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                  @PathVariable("endDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate) {

        logger.info("listing all documents for patientId[" + patientId + "] by[" + principal.getName() + "]");
        return fileManagementApiService.listFilesByPatient(patientId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    @RequestMapping("/download/patient/{patientId}/{fileId}")
    @RolesAllowed("ROLE_FILE_DOWNLOAD")
    public ResponseEntity downloadPatient(Principal principal,
                                              @PathVariable("patientId") String patientId,
                                              @PathVariable("fileId") String fileId){

        logger.info("downloading file from patient [" + patientId + "] fileId[" + fileId + "] by[" + principal + "]");
        return fileManagementApiService.downloadFileFromPatient(patientId, fileId);
    }
}
