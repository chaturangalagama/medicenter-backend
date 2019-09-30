package business.service;

import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class ClinicServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ClinicServiceTest.class);


    @Autowired
    private ClinicService clinicService;

    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Before
    public void setUp() throws Exception {


        when(doctorRepository.existsById(anyString())).thenReturn(true);
        when(clinicRepository.findAll())
                .thenReturn(Arrays.asList(
                        mockClinic("Bukit Batok", "West Avenue 5", "12688",
                                "8989022", "8899221", "CL01", Arrays.asList("DO1", "DO2"), "HMC", "clinic@email.com"),
                        mockClinic("Clementi MRT", "Mall", "12688",
                                "8989022", "8899222", "CL02", Arrays.asList("DO5", "DO3"), "CM", "clinic@email.com")));
        when(clinicRepository.checkClinicCodeExists(anyString())).thenReturn(false);
        Clinic clinic = mockClinic("Bukit Batok", "West Avenue 5", "12688",
                "8989022", "8899221", "CL01", Arrays.asList("DO1", "DO2"), "Group", "clinic@email.com");
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(clinic, "0001");
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(clinic));
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);
    }

    public static Clinic mockClinic(String my_clinic, String in_singapore, String postalCode,
                                    String faxNumber, String contactNumber, String cl91, List<String> attendingDoctorId, String groupName, String emailAddress) {
        return new Clinic(my_clinic, groupName, emailAddress, new CorporateAddress("attention", in_singapore, postalCode), faxNumber,
                contactNumber, Status.ACTIVE, cl91, attendingDoctorId, Arrays.asList("marry001", "scarlet", "wonderwoman"));
    }


    @Test
    public void listAll() {
        List<Clinic> clinics = clinicService.listAll();
        assertEquals(2, clinics.size());


    }

    @Test
    public void addNewClinic() throws CMSException {
        Clinic clinic = clinicService.addNewClinic(mockClinic("My Clinic", "Full Address",
                "08872", "8883000", "97922222", "CL91",
                Arrays.asList("DO5", "DO3"), "HMC", "clinic@email.com"));
        assertEquals( "0001", clinic.getId());

    }

    @Test
    public void validateClinicAccess() throws CMSException {
        boolean isValid = clinicService.validateClinicAccess("DO1", Arrays.asList("DO5", "DO3"),
                mockClinic("My Clinic", "Full Address",
                        "08872", "8883000", "97922222", "CL91",
                        Arrays.asList("DO5", "DO3"), "HMC", "clinic@email.com"),
                Arrays.asList("DO5", "DO8"));
        assertTrue(isValid);
    }


    @Test
    public void searchById() throws CMSException {
        Clinic clinic = clinicService.searchById("0001");
        assertEquals( "0001", clinic.getId());
    }

    @Test
    public void modify() throws CMSException {
        Clinic clinic = clinicService.modify("0001",
                mockClinic("My Clinic", "Full Address",
                        "08872", "8883000", "97922222", "CL91",
                        Arrays.asList("DO5", "DO3"), "HMC", "clinic@email.com"));

        assertEquals("0001", clinic.getId());
    }

    @Test
    public void remove() {
        boolean remove = clinicService.remove("0001");
        assertTrue(remove);
    }
}
