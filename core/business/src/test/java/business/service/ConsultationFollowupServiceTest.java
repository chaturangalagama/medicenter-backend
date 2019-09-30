package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockClinic;
import business.mock.MockPatientVisitRegistry;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationFollowupService;
import com.ilt.cms.repository.clinic.ClinicRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationFollowupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class ConsultationFollowupServiceTest {

    @Autowired
    private ConsultationFollowupService consultationFollowupService;

    @Autowired
    private ConsultationFollowupRepository consultationFollowupRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @MockBean
    private Principal principal;

    @Before
    public void setUp() throws Exception {
        when(principal.getName()).thenReturn("wonderwoman");

        when(consultationFollowupRepository.findByClinicIdAndFollowupDateBetween(anyString(), any(LocalDate.class),
                any(LocalDate.class), any(Sort.class)))
                .thenReturn(Arrays.asList(MockPatientVisitRegistry.mockConsultationFollowup()));
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(MockClinic.mockClinic()));
    }

    @Test
    public void listFollowups() throws Exception{
        List<ConsultationFollowup> consultationFollowups = consultationFollowupService.listFollowups(principal, "C900232nk2341",
                LocalDate.of(2018, 1, 1), LocalDate.of(2019, 12, 31));
        assertEquals( 1, consultationFollowups.size());
    }
}
