package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockDoctor;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.patient.PatientNote;
import com.ilt.cms.pm.business.service.clinic.PatientNoteService;
import com.ilt.cms.repository.clinic.DoctorRepository;
import com.ilt.cms.repository.clinic.PatientNoteRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class PatientNoteServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PatientNoteServiceTest.class);

    @Autowired
    private PatientNoteService patientNoteService;

    @Autowired
    private PatientNoteRepository patientNoteRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception {

        PatientNote patientNote = mockPatientNote();
        when(patientNoteRepository.findByPatientId(anyString())).thenAnswer(
                (Answer<PatientNote>) invocation -> {
                    String patientIdStr = invocation.getArgument(0);
                    PatientNote patientNote1 = mockPatientNote();
                    Field fieldpatientId = PatientNote.class.getDeclaredField("patientId");
                    fieldpatientId.setAccessible(true);
                    fieldpatientId.set(patientNote1, patientIdStr);
                    return patientNote1;
                }
        );

        when(mongoTemplate.findAndModify(any(Query.class),any(Update.class), any(FindAndModifyOptions.class), any())).
                thenReturn(mockPatientNote());
        //when(mongoTemplate.exists(any(Query.class), (Class<?>) any())).thenReturn(false);
        //when(mongoTemplate.exists(any(Query.class), anyString())).thenReturn(true);


        when(doctorRepository.findAll()).thenReturn(Arrays.asList(MockDoctor.mockDoctor()));
    }

    private PatientNote mockPatientNote(){
        PatientNote patientNote = new PatientNote("P000001");
        patientNote.getNoteDetails().add(new PatientNote.PatientNoteDetails("This is my first note", "0001",
                LocalDateTime.now(), Status.ACTIVE, PatientNote.TypeOfProblem.LONG_TERM, LocalDateTime.now()));
        return patientNote;
    }

    private PatientNote.PatientNoteDetails mockPatientNoteDetails(){
        return new PatientNote.PatientNoteDetails("This is my first note", "0001", LocalDateTime.now(),
                Status.ACTIVE, PatientNote.TypeOfProblem.SHORT_TERM, LocalDateTime.now());
    }



    @Test
    public void patientNote() {
        PatientNote patientNote = patientNoteService.patientNote("P00001");
        assertEquals("P00001", patientNote.getPatientId());
    }

    @Test
    public void addNewNote() throws CMSException {
        PatientNote patientNote = mockPatientNote();
        PatientNote patientNote1 = patientNoteService.addNewNote("P00001", mockPatientNoteDetails());
        assertNotNull(patientNote);
    }

    @Test
    public void changeNoteDetailState() {
        PatientNote patientNote = patientNoteService.changeNoteDetailState("1000", "1000", Status.INACTIVE);
        assertNotNull(patientNote);
    }
}
