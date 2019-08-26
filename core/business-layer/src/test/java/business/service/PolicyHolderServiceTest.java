package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockMedicalCoverage;
import business.mock.MockPolicyHolder;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.pm.business.service.PolicyHolderService;
import com.ilt.cms.repository.PolicyHolderRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderChasRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderCorporateRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderInsuranceRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderMediSaveRepository;
import com.lippo.cms.exception.PolicyHolderException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class PolicyHolderServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PolicyHolderServiceTest.class);

    @Autowired
    private PolicyHolderService policyHolderService;

    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    @Autowired
    private PolicyHolderChasRepository policyHolderChasRepository;

    @Autowired
    private PolicyHolderCorporateRepository policyHolderCorporateRepository;

    @Autowired
    private PolicyHolderInsuranceRepository policyHolderInsuranceRepository;

    @Autowired
    private PolicyHolderMediSaveRepository policyHolderMediSaveRepository;

    @Autowired
    private MedicalCoverageRepository coverageRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    //private PolicyHolder.PolicyHolderChas policyHolder;

    @Before
    public void setUp() throws Exception {
        PolicyHolder.PolicyHolderChas policyHolderChas = MockPolicyHolder.mockPolicyHolderChas();
        PolicyHolder.PolicyHolderInsurance policyHolderInsurance = MockPolicyHolder.mockPolicyHolderInsurance();
        PolicyHolder.PolicyHolderCorporate policyHolderCorporate = MockPolicyHolder.mockPolicyHolderCorporate();
        PolicyHolder.PolicyHolderMediSave policyHolderMediSave = MockPolicyHolder.mockPolicyHolderMediSave();

        CoveragePlan plan1 = MockMedicalCoverage.mockCoveragePlan();

        MedicalCoverage medicalCoverage = MockMedicalCoverage.mockMedicalCoverage();

        when(policyHolderChasRepository.findByIdentificationNumber(any(UserId.class))).thenReturn(null);
        when(policyHolderChasRepository.findAllByIdentificationNumberAndStatus(any(UserId.class), any(Status.class)))
                .thenReturn(Arrays.asList(MockPolicyHolder.mockPolicyHolderChas()));
        when(policyHolderChasRepository.save(any(PolicyHolder.PolicyHolderChas.class))).thenReturn(policyHolderChas);

        when(policyHolderInsuranceRepository.findAllByIdentificationNumberAndStatus(any(UserId.class), any(Status.class)))
                .thenReturn(Arrays.asList(MockPolicyHolder.mockPolicyHolderInsurance()));
        when(policyHolderInsuranceRepository.save(any(PolicyHolder.PolicyHolderInsurance.class))).thenReturn(policyHolderInsurance);

        when(policyHolderCorporateRepository.findByIdentificationNumberAndRelationshipIsNull(any(UserId.class)))
                .thenReturn(null);
        when(policyHolderCorporateRepository.save(any(PolicyHolder.PolicyHolderCorporate.class))).thenReturn(policyHolderCorporate);

        when(policyHolderMediSaveRepository.findByIdentificationNumber(any(UserId.class)))
                .thenReturn(Arrays.asList());
        when(policyHolderMediSaveRepository.save(any(PolicyHolder.PolicyHolderMediSave.class))).thenReturn(policyHolderMediSave);


        when(policyHolderChasRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) inocation->{
                    String idStr = inocation.getArgument(0);
                    PolicyHolder chas = MockPolicyHolder.mockPolicyHolderChas();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(chas, idStr);
                    return Optional.of(chas);
                }
        );

        when(policyHolderChasRepository.countAllByMedicalCoverageIdAndStatus(anyString(), any(Status.class))).thenReturn(2);
        when(policyHolderInsuranceRepository.countAllByMedicalCoverageIdAndStatus(anyString(), any(Status.class))).thenReturn(2);
        when(policyHolderCorporateRepository.countAllByMedicalCoverageIdAndStatus(anyString(), any(Status.class))).thenReturn(2);
        when(policyHolderMediSaveRepository.countAllByMedicalCoverageIdAndStatus(anyString(), any(Status.class))).thenReturn(2);

        when(mongoTemplate.findOne(any(Query.class), any())).thenReturn(MockMedicalCoverage.mockCoveragePlan());
        when(mongoTemplate.exists(any(Query.class), any(Class.class))).thenReturn(true);

    }

    @Test
    @Ignore
    //TODO cannot set the return count in mock repository
    public void populatePolicyHolderCounts() {
        List<MedicalCoverage> medicalCoverages = policyHolderService.populatePolicyHolderCounts(Arrays.asList(MockMedicalCoverage.mockMedicalCoverage()));
        assertEquals(2,medicalCoverages.get(0).getPolicyHolderCount());
    }

    @Test
    public void addPolicyHolderToCoverageChas() throws PolicyHolderException{
        PolicyHolder policyHolder = policyHolderService.addPolicyHolderToCoverage(MedicalCoverage.CoverageType.CHAS.name(), MockPolicyHolder.mockPolicyHolderChas());
        assertEquals("Policy holders name", policyHolder.getName());

    }
    @Test
    public void addPolicyHolderToCoverageInsurance() throws PolicyHolderException{
        PolicyHolder policyHolder = policyHolderService.addPolicyHolderToCoverage(MedicalCoverage.CoverageType.INSURANCE.name(), MockPolicyHolder.mockPolicyHolderInsurance());
        assertEquals("Policy holders name", policyHolder.getName());

    }
    @Test
    public void addPolicyHolderToCoverageCorporate() throws PolicyHolderException{
        PolicyHolder policyHolder = policyHolderService.addPolicyHolderToCoverage(MedicalCoverage.CoverageType.CORPORATE.name(), MockPolicyHolder.mockPolicyHolderCorporate());
        assertEquals("Policy holders name", policyHolder.getName());

    }
    @Test
    public void addPolicyHolderToCoverageMediSave() throws PolicyHolderException{
        PolicyHolder policyHolder = policyHolderService.addPolicyHolderToCoverage(MedicalCoverage.CoverageType.MEDISAVE.name(), MockPolicyHolder.mockPolicyHolderMediSave());
        assertEquals("Policy holders name", policyHolder.getName());

    }

    @Test
    public void removePolicyHolderToCoverage() {
        boolean isSuccess = policyHolderService.removePolicyHolderToCoverage("H0001", MedicalCoverage.CoverageType.CHAS.name(), "MC0001", "P0001");
        assertTrue(isSuccess);
    }

    @Test
    public void searchPolicyHolder() throws PolicyHolderException {
        PolicyHolder policyHolder = policyHolderService.searchPolicyHolder("H0001", MedicalCoverage.CoverageType.CHAS.name());
        assertEquals("H0001", policyHolder.getId());

    }
    @Test
    public void searchPolicyHolderByUserId() throws PolicyHolderException {
        Map<MedicalCoverage.CoverageType, List<? extends PolicyHolder>> coverageTypeListMap =
                policyHolderService.searchPolicyHolder(new UserId(UserId.IdType.NRIC_BLUE, "901-2343242"));
        assertEquals(1, coverageTypeListMap.get(MedicalCoverage.CoverageType.CHAS).size());

    }

    @Test
    public void searchPolicyHolderById() {
        Optional<PolicyHolder> policyHolder = policyHolderService.searchPolicyHolderById("H00001", MedicalCoverage.CoverageType.CHAS.name());
        assertEquals("H00001", policyHolder.get().getId() );

    }

    @Test
    public void findCoveragePlan() {
        when(mongoTemplate.findOne(any(Query.class), any())).thenReturn(MockMedicalCoverage.mockMedicalCoverage());
        MedicalCoverage medicalCoverage = policyHolderService.findCoveragePlan(MockPolicyHolder.mockPolicyHolderMediSave());
        assertNotNull(medicalCoverage);

    }
}
