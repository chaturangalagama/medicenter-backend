package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockMedicalCoverage;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import com.ilt.cms.pm.business.service.doctor.DiagnosisService;
import com.ilt.cms.repository.spring.DiagnosisRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class DiagnosisServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(DiagnosisServiceTest.class);

    @Autowired
    public DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisRepository diagnosisRepository;

    @Autowired
    public MongoTemplate mongoTemplate;


    @Before
    public void setUp() throws Exception {

        Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setIcd10Id("ICD ID");
        diagnosis1.setSnomedId("SNOMED ID");
        diagnosis1.setIcd10Code("B35.6");
        diagnosis1.setSnomedCode("SN892");
        diagnosis1.setIcd10Term("Headache");
        diagnosis1.setSnomedTerm("Brain Dead");
        diagnosis1.setStatus(Status.ACTIVE);
        diagnosis1.setFilterablePlanIds(Arrays.asList("PL01", "PL02"));

        Diagnosis diagnosis2 = new Diagnosis();
        diagnosis2.setIcd10Id("ICD ID");
        diagnosis2.setSnomedId("SNOMED ID");
        diagnosis2.setIcd10Code("B35.6");
        diagnosis2.setSnomedCode("SN892");
        diagnosis2.setIcd10Term("Headache");
        diagnosis2.setSnomedTerm("Brain Dead");
        diagnosis2.setStatus(Status.ACTIVE);
        diagnosis2.setFilterablePlanIds(Arrays.asList("PL01", "PL02"));

        MedicalCoverage coverage = MockMedicalCoverage.mockMedicalCoverage();

        when(diagnosisRepository.search(anyString())).thenReturn(Arrays.asList(diagnosis1, diagnosis2));
        when(diagnosisRepository.findAllById(anyList())).thenReturn(Arrays.asList(diagnosis1, diagnosis2));
        when(diagnosisRepository.searchFilerByPlan(anyString(), anyList())).thenReturn(Arrays.asList(diagnosis1, diagnosis2));
        when(mongoTemplate.findOne(any(Query.class), any(Class.class))).thenReturn(coverage);

    }
    @Test
    public void search() {
        List<Diagnosis> search = diagnosisService.search( Arrays.asList("11111", "PL02"), "temp");
        assertEquals(2, search.size());

    }

    @Test
    public void searchById() {
        List<Diagnosis> diagnoses = diagnosisService.searchById(Arrays.asList("D00001", "D00002"));
        assertEquals(2, diagnoses.size());
    }
}
