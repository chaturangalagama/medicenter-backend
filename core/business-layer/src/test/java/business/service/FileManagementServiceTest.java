package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockClinic;
import business.mock.MockPatient;
import business.mock.MockPatientVisitRegistry;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ilt.cms.core.entity.file.FileMetaData;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.pm.business.service.clinic.FileManagementService;
import com.ilt.cms.repository.clinic.ClinicRepository;
import com.ilt.cms.repository.patient.PatientRepository;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.AWSConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class FileManagementServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FileManagementServiceTest.class);

    @Autowired
    private FileManagementService fileManagementService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    @Autowired
    private AWSConfig awsConfig;

    @MockBean
    Principal principal;

    @MockBean
    AmazonS3 amazonS3;

    @MockBean
    S3Object s3Object;

    @MockBean
    S3ObjectInputStream s3ObjectInputStream;

    @Before
    public void setUp() throws Exception {
        when(principal.getName()).thenReturn("wonderwoman");
        when(awsConfig.getMaxFileSize()).thenReturn(10485760l);
        when(awsConfig.getS3BucketName()).thenReturn("cms-test");
        when(awsConfig.getAmazonS3()).thenReturn(amazonS3);
        //when(amazonS3.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class))).thenReturn();
        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
        when(s3ObjectInputStream.read(any())).thenReturn(11).thenReturn(12).thenReturn(-1);

        when(patientRepository.findById(anyString())).thenReturn(Optional.of(MockPatient.mockPatient()));
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(MockClinic.mockClinic()));
        when(patientVisitRepository.existsById(anyString())).thenReturn(true);
        when(clinicRepository.existsById(anyString())).thenReturn(true);
        when(patientVisitRepository.findById(anyString())).thenReturn(Optional.of(MockPatientVisitRegistry.mockVisitRegistry()));
        when(patientVisitRepository.save(any(PatientVisitRegistry.class))).thenAnswer(
                invocationOnMock -> {
                    PatientVisitRegistry patientVisitRegistry = invocationOnMock.getArgument(0);
                    return patientVisitRegistry;
                }
        );

    }

    @Test
    public void uploadToVisit() throws IOException, CMSException {
        File temp = File.createTempFile("temp-file-name", ".tmp");
        FileInputStream fileInputStream = new FileInputStream(temp);
        MockMultipartFile fstmp = new MockMultipartFile("file", temp.getName(), "multipart/form-data", fileInputStream);

        FileMetaData mockFileMetaData = new FileMetaData("visit/2019/5c36a936819737b028fc4d26/91006379399707189251548313296650100000",
                "test", "test_file", "admin", "5ab99ffadbea1b2384db9af8", "xlsx", 45415, "\"for test\"");
        FileMetaData fileMetaData = fileManagementService.uploadToVisit(mockFileMetaData, "abc", "82j2i1282j2h", "29928982nn2kkn2",
                fstmp);
        Assert.assertEquals("test_file",fileMetaData.getFileName());
    }

    @Test
    public void listFilesByVisit() throws CMSException {
        List<FileMetaData> fileMetaData = fileManagementService.listFilesByVisit("383874ui3289342");
        Assert.assertEquals(0,fileMetaData.size());
    }

    @Test(expected = CMSException.class)
    public void downloadFileFromVisit() throws CMSException, IOException {
        FileManagementService.FileDownloadResponse fileDownloadResponse = fileManagementService.downloadFileFromVisit("3909803242e32d",
                "cGF0aWVudC8yMDE5LzVjMzZhOTM2ODE5NzM3YjAyOGZjNGQyNi85MTAwNjM3OTM5OTcwNzE4OTI1MTU0ODMxMzI5NjY1MDEwMDAwMA==");
        Assert.assertEquals("test_file",fileDownloadResponse.getFileName());
    }


    @Test
    public void uploadToPatient() throws IOException, CMSException {
        File temp = File.createTempFile("temp-file-name", ".tmp");
        FileInputStream fileInputStream = new FileInputStream(temp);
        MockMultipartFile fstmp = new MockMultipartFile("file", temp.getName(), "multipart/form-data", fileInputStream);
        FileMetaData fileMetaData = fileManagementService.uploadToPatient(principal, "abc", "82j2i1282j2h", "29928982nn2kkn2",
                "temp_test", "description", fstmp);
        Assert.assertEquals("temp_test",fileMetaData.getFileName());
    }

    @Test
    public void listFilesByPatient() throws CMSException {
        List<FileMetaData> fileMetaData = fileManagementService.listFilesByPatient("383874ui3289342",
                LocalDateTime.of(2018, 01, 01, 0, 0),
                LocalDateTime.of(2019, 12, 31, 23, 59));
        Assert.assertEquals(1,fileMetaData.size());
    }

    @Test
    public void downloadFileFromPatient() throws CMSException, IOException {
        FileManagementService.FileDownloadResponse fileDownloadResponse = fileManagementService.downloadFileFromPatient("3909803242e32d",
                "cGF0aWVudC8yMDE5LzVjMzZhOTM2ODE5NzM3YjAyOGZjNGQyNi85MTAwNjM3OTM5OTcwNzE4OTI1MTU0ODMxMzI5NjY1MDEwMDAwMA==");
        Assert.assertEquals("test_file",fileDownloadResponse.getFileName());

    }
}
