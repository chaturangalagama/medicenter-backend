package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockClinic;
import business.mock.MockMedicalCoverage;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.pm.business.service.MedicalCoverageService;
import com.ilt.cms.repository.spring.ClinicRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderChasRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderCorporateRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderInsuranceRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderMediSaveRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class MedicalCoverageServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MedicalCoverageServiceTest.class);

    @Autowired
    private MedicalCoverageService medicalCoverageService;

    @Autowired
    private MedicalCoverageRepository medicalCoverageRepository;

    @Autowired
    private PolicyHolderChasRepository policyHolderChasRepository;

    @Autowired
    private PolicyHolderCorporateRepository policyHolderCorporateRepository;

    @Autowired
    private PolicyHolderInsuranceRepository policyHolderInsuranceRepository;

    @Autowired
    private PolicyHolderMediSaveRepository policyHolderMediSaveRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private MongoTemplate mongoTemplate;



    private int defaultSearchSize = 20;

    @Before
    public void init(){


        when(mongoTemplate.exists(any(Query.class), any(Class.class))).thenReturn(false);
        when(medicalCoverageRepository.findIfMedicalCoverageCodeExists(anyString())).thenReturn(false);
        when(medicalCoverageRepository.save(any(MedicalCoverage.class))).thenAnswer(
                (Answer<MedicalCoverage>) invocation -> {
                    MedicalCoverage medicalCoverage1 = invocation.getArgument(0);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(medicalCoverage1, "M000001");
                    CoveragePlan coveragePlan = MockMedicalCoverage.mockCoveragePlan();
                    Field planId = CoveragePlan.class.getDeclaredField("id");
                    planId.setAccessible(true);
                    planId.set(coveragePlan, "P332222");
                    medicalCoverage1.getCoveragePlans().add(coveragePlan);
                    return medicalCoverage1;
                }
        );

        when(policyHolderChasRepository.exists(any(Example.class))).thenReturn(false);
        when(policyHolderCorporateRepository.exists(any(Example.class))).thenReturn(false);
        when(policyHolderInsuranceRepository.exists(any(Example.class))).thenReturn(false);
        when(policyHolderMediSaveRepository.exists(any(Example.class))).thenReturn(false);
        doNothing().when(medicalCoverageRepository).deleteById(anyString());

        when(medicalCoverageRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String idStr = invocation.getArgument(0);
                    MedicalCoverage medicalCoverage = MockMedicalCoverage.mockMedicalCoverage();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(medicalCoverage, idStr);
                    return Optional.of(medicalCoverage);
                }
        );

        when(clinicRepository.findAll()).thenReturn(Arrays.asList(MockClinic.mockClinic()));
    }



    @Test
    public void addANewCoverage() throws CMSException {
        MedicalCoverage medicalCoverage = medicalCoverageService.addANewCoverage(MockMedicalCoverage.mockMedicalCoverage());

        assertEquals("M000001",medicalCoverage.getId());
    }



    /*private void validateScheme() throws CMSException {

    }

    private void validateScheme() {

    }*/

    @Test
    public void removeCoverage() throws CMSException {
        boolean isSuccess = medicalCoverageService.removeCoverage("M000001");
        assertTrue(isSuccess);
    }

    @Test
    @Ignore
    public void addNewPlan() throws CMSException {
        MedicalCoverage medicalCoverage = medicalCoverageService.addNewPlan("M000001", MockMedicalCoverage.mockCoveragePlan());
        assertEquals("M000001", medicalCoverage.getId());



    }

    @Test
    @Ignore
    public void removePlan() throws CMSException {

    }

    @Test
    @Ignore
    public void modifyMedicalCoverage() throws CMSException {

    }

    @Test
    @Ignore
    public void updateMedicalPlan() throws CMSException {

    }

    @Test
    @Ignore
    public void removeSchemeFromPlan() throws CMSException {

    }

    @Test
    @Ignore
    public void addNewSchemeToPlan() throws CMSException {


    }

    @Test
    @Ignore
    public void replaceSchemesOfPlan() throws CMSException {

    }


    @Test
    @Ignore
    public void  searchCoverage() {


    }

    @Test
    @Ignore
    public void list() {

    }

    @Test
    @Ignore
    public void findCoveragePlan(){

    }

    @Test
    @Ignore
    public void findAssociatedCoverageDrug(){

    }

    @Test
    @Ignore
    public void findAssociatedCoverageMedicalTest(){

    }

    @Test
    @Ignore
    public void findAssociatedCoverageVaccination(){

    }

    @Test
    @Ignore
    public void findMedicalService(){

    }

    @Test
    @Ignore
    public void listAll() {

    }

    @Test
    @Ignore
    public void listByClinic() {

    }


    @Test
    @Ignore
    public void findPlanByMedicalCoverageId(){

    }

    @Test
    @Ignore
    public void doesPlanExists(){

    }
}
