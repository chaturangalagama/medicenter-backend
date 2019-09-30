package com.ilt.cms.downstream.clinic;

import com.ilt.cms.api.entity.file.FileMetadataEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;

public interface FileManagementApiService {

    ResponseEntity<ApiResponse> uploadToVisit(String uploadTo, String visitId, String username, MultipartFile multipartFile,
                                              String name, String fileName, String clinicId, String description);

    ResponseEntity<ApiResponse> listFilesByVisit(String visitId);

    ResponseEntity<byte[]> downloadFileFormVisit(String visitId, String fileId);

    ResponseEntity<ApiResponse> uploadToPatient(Principal principal, String name, String clinicId, String patientId,
                                                String fileName, String description, MultipartFile uploadFile);

    ResponseEntity<ApiResponse> listFilesByPatient(String patientId, LocalDateTime startDate, LocalDateTime endDate);

    ResponseEntity<byte[]> downloadFileFromPatient(String patientId, String fileId);
}
