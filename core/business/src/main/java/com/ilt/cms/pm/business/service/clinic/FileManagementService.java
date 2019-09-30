package com.ilt.cms.pm.business.service.clinic;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.lippo.cms.util.AWSConfig;
import com.ilt.cms.core.entity.file.FileMetaData;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.patient.patientVisit.PatientVisitDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.ConcurrencyLock;
import com.lippo.cms.util.UserInfoHelper;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileManagementService {

    private static final Logger logger = LoggerFactory.getLogger(FileManagementService.class);
    private List<String> byPassRole = Arrays.asList("ROLE_FILE_UPLOAD_ADMIN");
    private final ConcurrencyLock concurrencyLock;

    private AWSConfig awsConfig;

    private PatientVisitDatabaseService patientVisitDatabaseService;
    private PatientDatabaseService patientDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;

    public FileManagementService(AWSConfig awsConfig, PatientVisitDatabaseService patientVisitDatabaseService,
                                 PatientDatabaseService patientDatabaseService, ClinicDatabaseService clinicDatabaseService) {
        this.awsConfig = awsConfig;
        this.patientVisitDatabaseService = patientVisitDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
        concurrencyLock = new ConcurrencyLock();
    }

    public FileMetaData uploadToPatient(Principal principal, String name, String clinicId, String patientId, String fileName,
                                        String description, MultipartFile uploadFile) throws CMSException, IOException {

        List<String> userRoles = UserInfoHelper.getUserRoles(principal);
        Patient patient = patientDatabaseService.findPatientById(patientId)
                .orElseThrow(() -> {
                    logger.error("Patient [" + patientId + "] not valid");
                    return new CMSException(StatusCode.E2000, "Patient ID not valid");
                });
        if(uploadFile.getSize() > awsConfig.getMaxFileSize()){
            throw new CMSException(StatusCode.E1010, "File size[" + uploadFile.getSize() + "] is too big");
        }
        if (!CommonUtils.isStringValid(fileName, description)) {
            logger.error("invalid values name[" + fileName + "] description[" + description + "]");
            throw new CMSException(StatusCode.E2000, "File name and description cannot be empty");
        } else {

            boolean clinicAccess = ClinicService.validateClinicAccess(principal.getName(),
                    userRoles, clinicDatabaseService.findOne(clinicId).orElse(null), byPassRole);
            if (!clinicAccess) {
                logger.info("user has no access to the given clinic [" + principal.getName() + "]");
                throw new CMSException(StatusCode.E2000, "user has no access to the given clinic [" + principal.getName() + "]");
            }
            logger.info("saving file into s3");
            int lockNumber = 0;
            try {
                lockNumber = concurrencyLock.lock(patient.getId());
                logger.debug("Locking on number [" + lockNumber + "]");
                boolean nameDuplicate = patient.getFileMetaData().stream()
                        .anyMatch(fileMetaData1 -> fileMetaData1.getFileName().equals(fileName));
                if (nameDuplicate) {
                    logger.error("File name [" + fileName + "] duplicate");
                    throw new CMSException(StatusCode.E1004, "Name already exists");
                } else {
                    return uploadFileToS3(principal, name, clinicId, fileName, description, uploadFile, patient);
                }
            } finally {
                concurrencyLock.unlock(lockNumber);
            }
        }
    }

    private FileMetaData uploadFileToS3(Principal principal, String name, String clinicId, String fileName,
                                                            String description, MultipartFile uploadFile, Patient patient) throws IOException, CMSException {
        String fileId = formulatePatientFileId(patient);
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.addUserMetadata("clinic-id", clinicId);
        metadata.addUserMetadata("username", principal.getName());
        try {
            FileMetaData fileMetaData = uploadToS3(principal, name, clinicId, fileName, description, uploadFile, patient.getFileMetaData(), fileId, metadata);
            logger.info("updating local database");
            patientDatabaseService.save(patient);
            logger.info("Recoded added");
            return fileMetaData;
        } catch (AmazonServiceException e) {
            logger.error("Error adding to S3 bucket : ", e);
            logger.debug("S3 bucket["+awsConfig.getS3BucketName()+"], AWS access key["+awsConfig.getAwsAccessKey()
                    +"], security key["+awsConfig.getAwsSecretKey()+"]");
            throw new CMSException(StatusCode.I5000, "Error adding file");
        }
    }
    private FileMetaData uploadToS3(Principal principal, String name, String clinicId, String fileName, String description,
                                    MultipartFile uploadFile, List<FileMetaData> fileMetaData, String fileId,
                                    ObjectMetadata metadata) throws IOException {
        logger.info("upload file to S3 s3BucketName["+awsConfig.getS3BucketName()+"], fileId["+fileId+"], metadata["+metadata+"]");
        PutObjectResult result = awsConfig.getAmazonS3().putObject(awsConfig.getS3BucketName(), fileId, uploadFile.getInputStream(), metadata);
        logger.info("Added to S3 bucket [" + result + "]");
        String fileExtension = fineFileExtension(uploadFile);

        FileMetaData metaData = new FileMetaData(fileId, name, fileName, principal.getName(), clinicId, fileExtension,
                uploadFile.getSize(), description);
        fileMetaData.add(metaData);
        return metaData;
    }

    private String formulatePatientFileId(Patient patient) {
        return "patient/" + LocalDate.now().getYear() + "/" + patient.getId() + "/" + CommonUtils.idGenerator();
    }


    public FileMetaData uploadToVisit(FileMetaData fileMetaData, String uploadTo, String visitId, String username, MultipartFile uploadFile) throws CMSException, IOException {

        logger.debug("fileMetaData:" + fileMetaData);
        if(!fileMetaData.areParmetersValid()){
            logger.error("invalid values name["+fileMetaData.getName()+"],fileName["+fileMetaData.getFileName()+"],uploader["
                    +fileMetaData.getUploader()+"],clinicId["+fileMetaData.getClinicId()+"]");
            throw new CMSException(StatusCode.E1002, "Name, fileName, uploader and clinicId cannot be empty");
        }
        if(!CommonUtils.isStringValid(uploadTo)){
            logger.error("invalid uploadTo[" + uploadTo + "]");
            throw new CMSException(StatusCode.E1002, "uploadTo cannot be empty");
        }
        if(!patientVisitDatabaseService.exists(visitId)){
            logger.error("VisitId[" + visitId + "] cannot be found");
            throw new CMSException(StatusCode.E2000, "Visit not found");
        }

        if (!clinicDatabaseService.exists(fileMetaData.getClinicId())) {
            throw new CMSException(StatusCode.E2002);
        }
        if (uploadFile.getSize() > awsConfig.getMaxFileSize()) {
            throw new CMSException(StatusCode.E1010, "File size is too big");
        }

        PatientVisitRegistry visitRegistry = patientVisitDatabaseService.searchById(visitId);
        logger.debug("uploading file to patient visit registry");

        int lockNumber = 0;
        try {
            lockNumber = concurrencyLock.lock(visitRegistry.getId());
            logger.debug("Locking on number [" + lockNumber + "]");
            List<FileMetaData> currentFileMetaData = visitRegistry.getFileMetaData();
            String fileName = fileMetaData.getFileName();
            boolean fileNameDuplicate = currentFileMetaData.stream()
                    .anyMatch(fileMetaData1 -> fileMetaData1.getFileName().equals(fileName));
            if (fileNameDuplicate) {
                logger.error("File name [" + fileName + "] duplicate");
                throw new CMSException(StatusCode.E1004, "Name already exists");
            }
            String fileId = formulateVisitFileId(visitId);
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.addUserMetadata("clinic-id", fileMetaData.getClinicId());
            metadata.addUserMetadata("username", username);
            metadata.addUserMetadata("test-id", uploadTo);
            try {
                FileMetaData metaData = uploadToS3(fileMetaData, uploadFile, currentFileMetaData, fileId, metadata);
                logger.debug("updating patient visit");
                visitRegistry.setFileMetaData(currentFileMetaData);
                patientVisitDatabaseService.save(visitRegistry);
                logger.debug("Recoded added");
                return metaData;
            } catch (AmazonServiceException e) {
                logger.error("Error adding to S3 bucket : ", e);
                logger.debug("S3 bucket["+awsConfig.getS3BucketName()+"], AWS access key["+awsConfig.getAwsAccessKey()
                        +"], security key["+awsConfig.getAwsSecretKey()+"]");
                throw new CMSException(StatusCode.I5000, "Error adding file");
            }
        } finally {
            concurrencyLock.unlock(lockNumber);
        }
    }

    public List<FileMetaData> listFilesByVisit(String visitId) throws CMSException {
        logger.debug("Listing files for visit [id]:[{}]", visitId);
        PatientVisitRegistry visitRegistry = patientVisitDatabaseService.searchById(visitId);
        if (visitRegistry == null) {
            logger.debug("Patient visit not found for [id]:[{}]", visitId);
            throw new CMSException(StatusCode.E1002);
        }
        List<FileMetaData> visitData = (visitRegistry.getFileMetaData() == null)
                ? new ArrayList<>() : visitRegistry.getFileMetaData();

        return visitData;
    }

    public List<FileMetaData> listFilesByPatient(String patientId, LocalDateTime startDate, LocalDateTime endDate) throws CMSException {
        logger.debug("List files for patient [id]:[{}], [startDate]:[{}], [endDate]:[{}]", patientId, startDate, endDate);
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            logger.error("Patient info not found [" + patientId + "]");
            throw new CMSException(StatusCode.E2000, "Patient not found");
        }

        if(patientOpt.get().getFileMetaData() != null){
            return patientOpt.get().getFileMetaData();
        }

        return Collections.emptyList();

    }

    public FileDownloadResponse downloadFileFromPatient(String patientId, String fileId) throws IOException, CMSException {
        logger.info("downloading from patient [" + patientId + "]");
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            logger.error("Patient not found [" + patientId + "]");
            throw new CMSException(StatusCode.E2000, "Patient not found");
        } else {
            Patient patient = patientOpt.get();
            Optional<FileMetaData> fileMetaDataOptional = patient.getFileMetaData()
                    .stream().filter(fileMetaData -> fileMetaData.getFileId().equals(fileId)).findFirst();

            if (fileMetaDataOptional.isPresent()) {
                logger.debug("Downloading file [" + fileId + "] from s3");
                ByteArrayOutputStream outputStream = downloadFileFromS3(fileMetaDataOptional.get());
                return new FileDownloadResponse(fileMetaDataOptional.get().getFileName(), outputStream);
            } else {
                logger.error("No file found for record [" + fileId + "]");
                throw new CMSException(StatusCode.E2000, "File not found");
            }

        }
    }

    public class FileDownloadResponse {
        private String fileName;
        private ByteArrayOutputStream outputStream;


        public FileDownloadResponse(String fileName, ByteArrayOutputStream outputStream) {
            this.fileName = fileName;
            this.outputStream = outputStream;
        }

        public String getFileName() {
            return fileName;
        }

        public ByteArrayOutputStream getOutputStream() {
            return outputStream;
        }
    }


    public FileDownloadResponse downloadFileFromVisit(String visitId, String fileId) throws IOException, CMSException {
        logger.debug("Downloading file for visit [id]:[{}] fileId [id]:[{}]", visitId, fileId);
        PatientVisitRegistry visitRegistry = patientVisitDatabaseService.searchById(visitId);
        if (visitRegistry == null) {
            logger.debug("Patient visit not found [id]:[{}]", visitId);
            throw new CMSException(StatusCode.E1002);
        } else {
            Optional<FileMetaData> metaDataOptional = visitRegistry.getFileMetaData()
                    .stream()
                    .filter(fileMetaData -> fileMetaData.getFileId().equals(fileId)).findFirst();

            if (metaDataOptional.isPresent()) {
                FileMetaData fileMetaData = metaDataOptional.get();
                ByteArrayOutputStream outputStream = downloadFileFromS3(fileMetaData);
                return new FileDownloadResponse(fileMetaData.getFileName(), outputStream);
            } else {
                logger.error("No file records found in the visit");
                throw new CMSException(StatusCode.E1002, "No file found for download");
            }
        }
    }

    private String formulateVisitFileId(String patientVisitId) {
        return "patient-visit/" + LocalDate.now().getYear() + "/" + patientVisitId + "/" + CommonUtils.idGenerator();
    }

    private FileMetaData uploadToS3(FileMetaData fileMetaData, MultipartFile uploadFile, List<FileMetaData> currentFileMetaData, String fileId,
                                    ObjectMetadata metadata) throws IOException {
        PutObjectResult result = awsConfig.getAmazonS3().putObject(awsConfig.getS3BucketName(), fileId, uploadFile.getInputStream(), metadata);
        logger.debug("Added to S3 bucket [{}]", result);
        String fileExtension = fineFileExtension(uploadFile);

        fileMetaData.setFileId(Base64.getEncoder().encodeToString(fileId.getBytes()));
        fileMetaData.setType(fileExtension);
        fileMetaData.setSize(uploadFile.getSize());
        currentFileMetaData.add(fileMetaData);
        return fileMetaData;
    }

    private ByteArrayOutputStream downloadFileFromS3(FileMetaData fileMetaData) throws IOException {
        S3Object s3Object = awsConfig.getAmazonS3().getObject(awsConfig.getS3BucketName(), fileMetaData.decodedFileIdValue());
        S3ObjectInputStream objectContent = s3Object.getObjectContent();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readBuf = new byte[1024];
        int readLen;
        try {
            while ((readLen = objectContent.read(readBuf)) > 0) {
                outputStream.write(readBuf, 0, readLen);
            }
        }catch (IOException e){
            logger.error(e.getMessage(), e);
            throw e;
        }finally {
            objectContent.close();
            outputStream.close();
        }
        return outputStream;
    }

    private String fineFileExtension(MultipartFile uploadFile) {
        String originalFilename = uploadFile.getOriginalFilename();
        if (originalFilename.contains(".")) {
            String[] split = originalFilename.split("\\.");
            originalFilename = split[split.length - 1];
        }
        return originalFilename;
    }
}
