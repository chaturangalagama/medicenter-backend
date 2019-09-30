package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.core.entity.file.FileMetaData;
import com.ilt.cms.downstream.clinic.FileManagementApiService;
import com.ilt.cms.pm.business.service.clinic.FileManagementService;
import com.ilt.cms.pm.integration.mapper.patient.PatientMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultFileManagementApiService implements FileManagementApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFileManagementApiService.class);
    private FileManagementService fileManagementService;

    public DefaultFileManagementApiService(FileManagementService fileManagementService) {
        this.fileManagementService = fileManagementService;
    }

    @Override
    public ResponseEntity<ApiResponse> uploadToVisit(String uploadTo, String visitId, String username, MultipartFile multipartFile,
                                                     String name, String fileName, String clinicId, String description) {
        try {
            FileMetaData metadata = new FileMetaData( name, fileName, username, clinicId, description);
            FileMetaData fileMetaData = fileManagementService.uploadToVisit(metadata, uploadTo, visitId, username, multipartFile);
            logger.info("Document uploaded to PatientVisitRegistry [id]:[" + visitId + "]");
            return httpApiResponse(new HttpApiResponse(PatientMapper.mapToFileMetaDataEntity(fileMetaData)));
        } catch (CMSException e) {
            logger.error("Document upload error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        } catch (IOException e) {
            logger.error("Document upload error :" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(StatusCode.I5000, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listFilesByVisit(String visitId) {
        try {
            List<FileMetaData> fileMetaData = fileManagementService.listFilesByVisit(visitId);
            logger.info("Document list found for PatientVisitRegistry [id]:[" + visitId + "]");
            return httpApiResponse(new HttpApiResponse(fileMetaData.stream()
                    .map(PatientMapper::mapToFileMetaDataEntity)
                    .collect(Collectors.toList())));
        } catch (CMSException e) {
            logger.error("Document list getting error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFileFormVisit(String visitId, String fileId) {
        try {
            FileManagementService.FileDownloadResponse downloadResponse = fileManagementService.downloadFileFromVisit(visitId, fileId);
            logger.info("Document [fileId]:[" + fileId + "] downloaded for PatientVisitRegistry [id]:[" + visitId + "]");
            logger.info("file downloaded");
            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.add("Content-Transfer-Encoding", "binary");
            headers.add("Content-Disposition", "attachment; filename=\"" + downloadResponse.getFileName() + "\"");
            return new ResponseEntity<>(downloadResponse.getOutputStream().toByteArray(), headers, HttpStatus.OK);
        } catch (CMSException e) {
            logger.error("Document [fileId]:[" + fileId + "] download error [" + e.getStatusCode() + "]:" + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Document download error :" + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> uploadToPatient(Principal principal, String name, String clinicId, String patientId, String fileName, String description, MultipartFile uploadFile) {
        try {
            FileMetaData fileMetaData = fileManagementService.uploadToPatient(principal, name, clinicId, patientId, fileName, description, uploadFile);
            logger.info("Document uploaded to Patient [id]:[" + patientId + "]");
            return httpApiResponse(new HttpApiResponse(PatientMapper.mapToFileMetaDataEntity(fileMetaData)));
        } catch (CMSException e) {
            logger.error("Document [fileId]:[" + fileName + "] upload error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        } catch (IOException e) {
            logger.error("Document download error :" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(StatusCode.I5000, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listFilesByPatient(String patientId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<FileMetaData> fileMetaData = fileManagementService.listFilesByPatient(patientId, startDate, endDate);
            logger.info("Document list found for Patient [id]:[" + patientId + "]");
            return httpApiResponse(new HttpApiResponse(fileMetaData.stream()
                    .map(PatientMapper::mapToFileMetaDataEntity)
                    .collect(Collectors.toList())));
        } catch (CMSException e) {
            logger.error("Document list getting error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFileFromPatient(String patientId, String fileId) {
        try {
            FileManagementService.FileDownloadResponse downloadResponse = fileManagementService.downloadFileFromPatient(patientId, fileId);
            logger.info("Document [fileId]:[" + fileId + "] downloaded for Patient [id]:[" + patientId + "]");
            logger.info("file downloaded");
            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.add("Content-Transfer-Encoding", "binary");
            headers.add("Content-Disposition", "attachment; filename=\"" + downloadResponse.getFileName() + "\"");
            return new ResponseEntity<>(downloadResponse.getOutputStream().toByteArray(), headers, HttpStatus.OK);
        } catch (CMSException e) {
            logger.error("Document  download error [fileId]:[" + fileId + "] download error [" + e.getStatusCode() + "]:" + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Document download error :" + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.OK);
        }

    }
}
