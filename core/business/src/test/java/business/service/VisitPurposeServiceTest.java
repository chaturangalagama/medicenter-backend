package business.service;

import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.visit.VisitPurpose;
import com.ilt.cms.pm.business.service.patient.patientVisit.VisitPurposeService;
import com.ilt.cms.repository.patient.patientVisit.VisitPurposeRepository;
import com.lippo.cms.exception.CMSException;
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
public class VisitPurposeServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(VisitPurposeServiceTest.class);

    @Autowired
    private VisitPurposeService visitPurposeService;

    @Autowired
    private VisitPurposeRepository visitPurposeRepository;

    @Before
    public void setUp() throws Exception {

        when(visitPurposeRepository.findAll())
                .thenReturn(Arrays.asList(mockVisitPurpose(null,"Consultation"), mockVisitPurpose(null, "Refill"),
                        mockVisitPurpose(null, "Medical Checkup")));

        VisitPurpose visitPurpose = mockVisitPurpose();
        when(visitPurposeRepository.save(any(VisitPurpose.class))).thenAnswer(
                (Answer<VisitPurpose>) invocation->{
                    VisitPurpose visitPurpose1 = invocation.getArgument(0);
                    if(visitPurpose1.getId() == null) {
                        Field fieldId = PersistedObject.class.getDeclaredField("id");
                        fieldId.setAccessible(true);
                        fieldId.set(visitPurpose1, "VP00001");
                    }
                    return visitPurpose1;
                }
        );
        when(visitPurposeRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation->{
                    String idStr  = invocation.getArgument(0);
                    VisitPurpose visitPurpose1 = mockVisitPurpose();
                    Field fieldId = PersistedObject.class.getDeclaredField("id");
                    fieldId.setAccessible(true);
                    fieldId.set(visitPurpose1, idStr);

                   return Optional.of(visitPurpose1);
                }
        );
    }

    private VisitPurpose mockVisitPurpose() throws NoSuchFieldException, IllegalAccessException {
        return mockVisitPurpose("0001", "dummy name");
    }

    private VisitPurpose mockVisitPurpose(String id, String name) throws NoSuchFieldException, IllegalAccessException {
        VisitPurpose visitPurpose = new VisitPurpose(name);
        Field fieldId = PersistedObject.class.getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(visitPurpose, id);
        return visitPurpose;
    }

    @Test
    public void listAll() {
        List<VisitPurpose> visitPurposes = visitPurposeService.listAll();
        assertEquals(3, visitPurposes.size());
    }
    @Test
    public void addNew() throws CMSException, NoSuchFieldException, IllegalAccessException {
        VisitPurpose visitPurpose = visitPurposeService.addNew(mockVisitPurpose(null, "test visit purpose"));
        assertEquals("VP00001", visitPurpose.getId());
    }
    @Test
    public void modify() throws CMSException, NoSuchFieldException, IllegalAccessException {
        VisitPurpose purpose = visitPurposeService.modify("VP0002", mockVisitPurpose());
        assertEquals("VP0002", purpose.getId());
    }
    @Test
    public void remove() {
        boolean isSuccess = visitPurposeService.remove("0001");
        assertTrue(isSuccess);

    }
}
