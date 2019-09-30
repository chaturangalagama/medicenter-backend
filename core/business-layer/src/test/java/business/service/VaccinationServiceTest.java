package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockPatient;
import business.mock.MockVaccination;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientVaccination;
import com.ilt.cms.core.entity.vaccination.Dose;
import com.ilt.cms.core.entity.vaccination.Vaccination;
import com.ilt.cms.pm.business.service.patient.VaccinationService;
import com.ilt.cms.repository.patient.PatientRepository;
import com.ilt.cms.repository.patient.VaccinationRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class VaccinationServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(VaccinationServiceTest.class);

    @Autowired
    private VaccinationService vaccinationService;
    @Autowired
    private VaccinationRepository vaccinationRepository;
    @Autowired
    private PatientRepository patientRepository;


    @Before
    public void setUp() throws Exception {

        Patient patient = MockPatient.mockPatient();
        when(patientRepository.findById(anyString())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(MockPatient.mockPatient());
        when(vaccinationRepository.checkIfAllVaccinationDoseIdExists(anyString())).thenReturn(true);
        when(vaccinationRepository.isIdValid(anyString())).thenReturn(true);
        Vaccination vac1 = MockVaccination.mockVaccination();

        Vaccination vac2 = new Vaccination();
        vac2.setName("Vaccination 2");
        vac2.setAgeInMonths(36);
        List<Vaccination> vaccinations = Arrays.asList(vac1, vac2);

        Page<Vaccination> mock = mock(Page.class);
        when(mock.getTotalElements()).thenReturn(1000L);
        when(mock.getContent()).thenReturn(vaccinations);
        when(mock.getTotalPages()).thenReturn(10);
        when(mock.getNumber()).thenReturn(2);
        when(vaccinationRepository.findAll(any(Pageable.class))).thenReturn(mock);
        when(vaccinationRepository.findAll()).thenReturn(Arrays.asList(vac1, vac2));
        when(vaccinationRepository.findFirstByDosesIn(anyString())).thenAnswer(
                (Answer<Vaccination>) invocation->{
                    String doseId = invocation.getArgument(0);
                    Vaccination vaccination = MockVaccination.mockVaccination();
                    Optional<Dose> doseOpt = vaccination.getDoses().stream().findFirst();
                    if(doseOpt.isPresent()){
                        Dose dose= doseOpt.get();
                        Field id = Dose.class.getDeclaredField("doseId");
                        id.setAccessible(true);
                        id.set(dose, doseId);
                        vaccination.setDoses(Arrays.asList(dose));
                    }
                    return vaccination;
                }

        );
        when(vaccinationRepository.save(any(Vaccination.class))).thenAnswer(
                (Answer<Vaccination>) invocation->{
                    Vaccination vaccination = invocation.getArgument(0);
                    if(vaccination.getId() == null) {
                        Field id = PersistedObject.class.getDeclaredField("id");
                        id.setAccessible(true);
                        id.set(vaccination, "V000001");
                    }

                    return vaccination;
                }

        );
    }

    @Test
    public void listVaccines() {
        HashMap<String, Object> stringObjectHashMap = vaccinationService.listVaccines(2, 3);
        assertNotNull(stringObjectHashMap.get("content"));
    }

    @Test
    public void addVaccinationToPatient() throws CMSException, NoSuchFieldException, IllegalAccessException {
        PatientVaccination patientVaccination = vaccinationService.addVaccinationToPatient("P00001", MockVaccination.mockPatientVaccination());
        assertNotNull(patientVaccination.getId());
    }

    @Test
    public void removeVaccinationFromPatient() throws CMSException {
        boolean isSuccess = vaccinationService.removeVaccinationFromPatient("P00001", "00001");
        assertTrue(isSuccess);

    }

    @Test
    public void addVaccination() throws CMSException, NoSuchFieldException, IllegalAccessException {
        Vaccination mock = MockVaccination.mockVaccination();
        Vaccination vaccination = vaccinationService.addVaccination(mock);
        assertNotNull(vaccination.getId());
    }

    @Test
    public void addVaccinationWithDoseNameNull() throws NoSuchFieldException, IllegalAccessException {
        try{
            Vaccination mock = MockVaccination.mockVaccination();
            mock.getDoses().stream().findFirst().get().setName(null);
            Vaccination vaccination = vaccinationService.addVaccination(mock);
        } catch (CMSException e){
            assertThat(e.getCode(), is(StatusCode.E1002));
        }

    }

    @Test
    public void addVaccinationWithVaccinationNameEmpty() throws NoSuchFieldException, IllegalAccessException {
        try{
            Vaccination mock = MockVaccination.mockVaccination();
            mock.setName("");
            Vaccination vaccination = vaccinationService.addVaccination(mock);
        } catch (CMSException e){
            assertThat(e.getCode(), is(StatusCode.E1002));
        }

    }

    @Test
    public void listAllVaccines() {
        List<Vaccination> vaccinations = vaccinationService.listAllVaccines();
        assertEquals(2, vaccinations.size());
    }

    @Test
    public void findFirstByDosesIn(){
        Vaccination vaccination = vaccinationService.findFirstByDosesIn("0021");
        assertEquals("0021", vaccination.getDoses().get(0).getDoseId());

    }
}
