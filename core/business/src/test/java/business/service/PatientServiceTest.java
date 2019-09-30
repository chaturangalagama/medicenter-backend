package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockPatient;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.RunningNumber;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.pm.business.service.patient.PatientService;
import com.ilt.cms.repository.patient.PatientRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.exception.PatientException;
import com.lippo.commons.util.exception.RestValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class PatientServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PatientServiceTest.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception{
        Patient patient = MockPatient.mockPatient();

        when(patientRepository.save(any(Patient.class))).thenAnswer(
                (Answer<Patient>) invocation -> {
                    Patient patient1 = invocation.getArgument(0);

                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(patient1, "I0001");

                    return patient1;
                }
        );
        when(patientRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String idStr = invocation.getArgument(0);
                    Patient patient1 = MockPatient.mockPatient();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(patient1, idStr);
                    return Optional.of(patient1);
                }
        );
        when(patientRepository.findByUserId(new UserId(patient.getUserId().getIdType(),
                patient.getUserId().getNumber()))).thenReturn(Optional.of(patient));
        when(patientRepository.existsByUserId(any(UserId.class))).thenReturn(true);
        when(patientRepository.findAllByNameLike(anyString())).thenAnswer(
                (Answer<List>) invocation -> {
                    String searchStr = invocation.getArgument(0);
                    Patient patient1 = MockPatient.mockPatient();
                    Field name = Patient.class.getDeclaredField("name");
                    name.setAccessible(true);
                    name.set(patient1, searchStr);
                    return Arrays.asList(patient1);
                }
        );
        when(patientRepository.patientLikeSearch(anyString(), any(Pageable.class))).thenAnswer(
            (Answer<List<Patient>>) invocation -> {
                String searchStr = invocation.getArgument(0);
                Patient patient1 = MockPatient.mockPatient();
                Field name = Patient.class.getDeclaredField("name");
                name.setAccessible(true);
                name.set(patient1, searchStr);
                return Arrays.asList(patient1);
            }
            );

        when(patientRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String idStr = invocation.getArgument(0);
                    Patient patient1 = MockPatient.mockPatient();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(patient1, idStr);
                    return Optional.of(patient1);
                }
        );


        when(mongoTemplate.findAndModify(any(Query.class),any(Update.class), any(FindAndModifyOptions.class), any())).thenReturn(new RunningNumber());
        when(mongoTemplate.exists(any(Query.class), (Class<?>) any())).thenReturn(false);
        when(mongoTemplate.exists(any(Query.class), anyString())).thenReturn(true);
    }

    @Test
    public void save() throws NoSuchFieldException, IllegalAccessException {
        Patient patient = MockPatient.mockPatient();
        Patient newPatient = patientService.save(patient);
        assertEquals("I0001", newPatient.getId());
    }

    @Test
    public void patientRegistration() throws RestValidationException, PatientException, NoSuchFieldException, IllegalAccessException {
        Patient patient = MockPatient.mockPatient();
        Patient patient1 = patientService.patientRegistration(patient);
        assertEquals("I0001", patient1.getId());
    }

    @Test
    public void findPatientById() throws CMSException {
        Patient patient = patientService.findPatientById("I0001");
        assertEquals("I0001", patient.getId());
    }

    @Test
    public void findPatient() throws PatientException {
        Patient patient = patientService.findPatient("name", "Chan Tai Man");
        assertEquals( "Chan Tai Man", patient.getName());

    }

    @Test
    public void validateIdNumberUse() throws PatientException {
        boolean valid = patientService.validateIdNumberUse("NRIC:3322918(0)");
        assertTrue(valid);

    }

    @Test
    public void updatePatient() throws PatientException, NoSuchFieldException, IllegalAccessException {
        Patient patient = MockPatient.mockPatient();
        Patient updatePatient = patientService.updatePatient("I0001", patient);
        assertEquals( "I0001", updatePatient.getId());
    }

    @Test
    public void likeSearchPatient() {
        List<Patient> patients = patientService.likeSearchPatient("Chan Tai Man");
        assertEquals( "Chan Tai Man", patients.get(0).getName());

    }
}
