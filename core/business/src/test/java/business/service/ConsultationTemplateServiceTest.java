package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockDoctor;
import business.mock.MockPatient;

import com.ilt.cms.core.entity.PersistedObject;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;

import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationTemplateService;

import com.ilt.cms.repository.clinic.DoctorRepository;
import com.ilt.cms.repository.patient.PatientRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationTemplateRepository;

import com.lippo.cms.exception.CMSException;

import com.mitchellbosecke.pebble.error.PebbleException;

import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;

import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import java.lang.reflect.Field;

import java.util.*;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class ConsultationTemplateServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationTemplateServiceTest.class);

    @Autowired
    private ConsultationTemplateService consultationTemplateService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsultationTemplateRepository templateRepository;

    @Before
    public void setUp() throws Exception {

        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(MockDoctor.mockDoctor()));
        when(patientRepository.findById(anyString())).thenReturn(Optional.of(MockPatient.mockPatient()));


        ConsultationTemplate consultationTemplate1 = new ConsultationTemplate("test-template",
                "<p>Doctor Name is : {{ doctor_name }}</p>");
        ConsultationTemplate consultationTemplate2 = new ConsultationTemplate("test-template",
                "<p>Doctor Name is : {{ doctor_name }}</p>");

        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(consultationTemplate1, "00001");
        id.set(consultationTemplate2, "00002");

        when(templateRepository.findById(anyString())).thenReturn(Optional.of(consultationTemplate1));
        when(templateRepository.findAll()).thenReturn(Arrays.asList(consultationTemplate2));

    }




    @Test
    public void loadTemplate() throws IOException, PebbleException, CMSException {
        String template = consultationTemplateService.loadTemplate("GLOBAL", "00001", "0001", "00001");
        logger.info(template);
        assertEquals("<p>Doctor Name is : Doctor Who</p>", template);
    }

    @Test
    public void listTemplate() throws CMSException {
        List<ConsultationTemplate> consultationTemplates = consultationTemplateService.listTemplate("0001");
        assertEquals(2, consultationTemplates.size());

    }
}
