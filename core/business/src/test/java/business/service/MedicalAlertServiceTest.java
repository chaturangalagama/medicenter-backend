package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockMedicalAlert;
import com.ilt.cms.core.entity.patient.MedicalAlert;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientAllergy;
import com.ilt.cms.pm.business.service.patient.MedicalAlertService;
import com.ilt.cms.repository.patient.MedicalAlertRepository;
import com.ilt.cms.repository.patient.PatientRepository;
import com.lippo.cms.exception.MedicalAlertException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class MedicalAlertServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MedicalAlertServiceTest.class);

    @Autowired
    private MedicalAlertService medicalAlertService;

    @Autowired
    private MedicalAlertRepository medicalAlertRepository;

    @Autowired
    private PatientRepository patientRepository;


    @Before
    public void setUp() throws Exception {
        MedicalAlert medicalAlert = MockMedicalAlert.mockMedicalAlert();

        when(patientRepository.existsById(anyString())).thenReturn(true);
        Patient patient = mock(Patient.class);
        when(patient.getAllergies()).thenReturn(Arrays.asList(new PatientAllergy(PatientAllergy.AllergyType.ALLERGY_GROUP,
                "Food", "remark", new Date())));
        when(patientRepository.findById(anyString())).thenReturn(Optional.of(patient));

        when(medicalAlertRepository.save(any(MedicalAlert.class))).thenReturn(medicalAlert);
        when(medicalAlertRepository.findByPatientId(anyString())).thenAnswer(
                (Answer<MedicalAlert>) invocation -> {
                    String idStr = invocation.getArgument(0);
                    MedicalAlert medicalAlert1 = MockMedicalAlert.mockMedicalAlert();
                    Field id = MedicalAlert.class.getDeclaredField("patientId");
                    id.setAccessible(true);
                    id.set(medicalAlert1, idStr);
                    return medicalAlert1;
                }
        );
        when(medicalAlertRepository.findByMedicalAlertId(anyString())).thenReturn(medicalAlert);
    }

    @Test
    public void medicalAlertList() throws MedicalAlertException {
        MedicalAlert medicalAlert = medicalAlertService.medicalAlertList("P0001");
        assertEquals("P0001", medicalAlert.getPatientId());
    }

    @Test
    public void add() throws MedicalAlertException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        Date addDate1 = calendar.getTime();
        calendar.add(Calendar.YEAR, 8);
        Date expiryDate1 = calendar.getTime();
        MedicalAlert.MedicalAlertDetails medicalAlertDetails1 = MockMedicalAlert.mockMedicalAlertDetails(MedicalAlert.AlertType.MEDICATION, "alert detail 1", "remark 1", MedicalAlert.MedicalAlertDetails.Priority.HIGH, addDate1, expiryDate1);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -3);
        Date addDate2 = calendar.getTime();
        calendar.add(Calendar.YEAR, 6);
        Date expiryDate2 = calendar.getTime();
        MedicalAlert.MedicalAlertDetails medicalAlertDetails2 = MockMedicalAlert. mockMedicalAlertDetails(MedicalAlert.AlertType.MEDICAL_CONDITION, "alert detail 2", "remark 2", MedicalAlert.MedicalAlertDetails.Priority.LOW, addDate2, expiryDate2);

        MedicalAlert medicalAlert = medicalAlertService.add("P0001", Arrays.asList(medicalAlertDetails1, medicalAlertDetails2));
        assertNotNull(medicalAlert);

    }

    @Test
    public void delete() throws MedicalAlertException {
        boolean isSuccess = medicalAlertService.delete(Arrays.asList("M0001", "M0002"));
        assertTrue(isSuccess);
    }
}
