package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockDoctor;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.doctor.Speciality;
import com.ilt.cms.pm.business.service.clinic.DoctorService;
import com.ilt.cms.repository.clinic.ClinicRepository;
import com.ilt.cms.repository.clinic.DoctorRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class DoctorServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceTest.class);

    @Autowired
    public DoctorService doctorService;

    @Autowired
    public DoctorRepository doctorRepository;
    @Autowired
    public ClinicRepository clinicRepository;

    @Before
    public void setUp() throws Exception {


        Field idField = PersistedObject.class.getDeclaredField("id");
        idField.setAccessible(true);
        when(doctorRepository.existsById(anyString())).thenReturn(true);
        ConsultationTemplate consultationTemplate = new ConsultationTemplate("test-template",
                "<p>Doctor Name is : {{ doctor_name }}</p>");
        idField.set(consultationTemplate, "00001");
        List<ConsultationTemplate> consultationTemplates = Arrays.asList(consultationTemplate);
        Doctor doctor1 = MockDoctor.mockDoctor(idField, "Doctor Who", Speciality.Practice.ANAESTHESIOLOGY, Doctor.DoctorGroup.LOCUM, consultationTemplates, "admin", "0001");
        Doctor doctor2 = MockDoctor.mockDoctor(idField, "Doctor Who Seasson 2", Speciality.Practice.ENDOCRINOLOGY, Doctor.DoctorGroup.FLOATING, consultationTemplates, "admin1", "0002");

        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor1, doctor2));

        Doctor doctor = MockDoctor.mockDoctor(idField, "Who Seasson 5", Speciality.Practice.GP, Doctor.DoctorGroup.ANCHOR, consultationTemplates, "admin1", "0002");
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(doctor, "0001");
        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(doctor));
        when(doctorRepository.findByStatus(any())).thenReturn(Arrays.asList(doctor, doctor1, doctor2));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Clinic clinic = mock(Clinic.class);
        when(clinic.getAttendingDoctorId()).thenReturn(Arrays.asList("0001", "0002"));
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(clinic));
    }

    @Test
    public void listAll() {
        List<Doctor> doctors = doctorService.listAll();
        assertEquals(2, doctors.size());
    }

    @Test
    public void searchById() throws CMSException {
        Doctor doctor = doctorService.searchById("0001");
        assertEquals("0001", doctor.getId());
    }

    @Test
    public void addNewDoctor() throws NoSuchFieldException, IllegalAccessException, CMSException {
        Doctor doctor = doctorService.addNewDoctor(MockDoctor.mockDoctor());
        assertEquals("0001", doctor.getId());
    }

    @Test
    public void modify() throws NoSuchFieldException, IllegalAccessException, CMSException {
        Doctor doctor = doctorService.modify("0001", MockDoctor.mockDoctor());
        assertEquals("0001", doctor.getId());
    }

    @Test
    public void findAll() {
        List<Doctor> doctors = doctorService.findAll();

        assertEquals(2, doctors.size());
    }
}
