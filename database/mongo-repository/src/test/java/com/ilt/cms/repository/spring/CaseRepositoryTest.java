package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesOrder;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.collection.ListRandomizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ilt.cms.core.entity.casem.Claim.ClaimStatus.APPROVED;
import static org.junit.Assert.*;


/**
 * <p>
 * <code>{@link CaseRepositoryTest}</code> -
 * Contains unit tests for <code>{@link CaseRepository}</code>
 * </p>
 */
@RunWith(SpringRunner.class)
@DataMongoTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.ilt.cms.repository.spring.*"}))
public class CaseRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CaseRepository caseRepository;

    private EnhancedRandom caseRandomEnhancer;

    private Randomizer<Invoice> invoiceRandomizer;

    private Randomizer<SalesOrder> salesOrderRandomizer;

    private AtomicInteger invoiceNumberCounter;

    private static Claim createTestClaim() {
        Claim claim = new Claim();
        claim.setClaimStatus(APPROVED);
        claim.setClaimId("claim-id-1");
        claim.setClaimDoctorId("claimDoctor-1");
        claim.setAttendingDoctorId("attending-doctor-1");
        claim.setClaimExpectedAmt(10);
        claim.setConsultationAmt(12);
        claim.setGstAmount(1001);
        claim.setMedicalTestAmt(50);
        claim.setMedicationAmt(105);
        claim.setOtherAmt(120);
        claim.setRemark("Remark");
        claim.setPayersName("payer-1");
        claim.setPayersNric("nric-1");
        claim.setSubmissionDateTime(LocalDateTime.now().minusDays(5));
        claim.setClaimResult(
                new Claim.ClaimResult("claimResult-ref-1", LocalDateTime.now(), 20, "S0000", "Claim Partially Passed"));
        claim.setPaidResult(new Claim.ClaimResult("paid-result-ref-1", LocalDateTime.now(), 80, "S0000", "paid result - 1"));
        claim.setSubmissionResult(new Claim.SubmissionResult("1010", "S0000", "Success"));
        claim.setDiagnosisCodes(Arrays.asList("11", "22", "33", "44"));
        List<Claim.AppealRejection> appealRejections =
                Arrays.asList(new Claim.AppealRejection() {{
                    this.setReason("reject-1");
                }}, new Claim.AppealRejection() {{
                    this.setReason("reject-2");
                }});
        claim.setAppealRejections(appealRejections);
        return claim;
    }

    @Before
    public void setUp() throws Exception {
        invoiceNumberCounter = new AtomicInteger(0);

        invoiceRandomizer = new Randomizer() {
            private EnhancedRandom invoiceRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

            @Override
            public Invoice getRandomValue() {
                Invoice invoice = invoiceRandom.nextObject(Invoice.class);
                invoice.setInvoiceType(Invoice.InvoiceType.CREDIT);
                invoice.setClaim(createTestClaim());
                invoice.setInvoiceNumber("INVOICE-00" + invoiceNumberCounter.incrementAndGet());
                invoice.setInvoiceTime(LocalDateTime.now());
                return invoice;
            }
        };

        salesOrderRandomizer = new Randomizer() {
            EnhancedRandom salesOrderRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                    .maxRandomizationDepth(2)
                    .randomizationDepth(2)
                    .exclude(FieldDefinitionBuilder.field().named("purchaseItem").get())
                    .randomize(FieldDefinitionBuilder.field()
                            .named("invoices")
                            .ofType(List.class).inClass(SalesOrder.class).get(), new ListRandomizer(invoiceRandomizer, 4))
                    .build();

            @Override
            public SalesOrder getRandomValue() {
                return salesOrderRandom.nextObject(SalesOrder.class);
            }
        };

        caseRandomEnhancer = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .seed(10L)
                .randomizationDepth(2)
                .randomize(FieldDefinitionBuilder.field()
                        .named("salesOrder")
                        .ofType(SalesOrder.class).inClass(Case.class).get(), salesOrderRandomizer)
                .scanClasspathForConcreteTypes(true).build();

        mongoTemplate.dropCollection(Case.class);

    }

    @Test
    public void existsByIdAndStatus() {
        mongoTemplate.dropCollection(Case.class);
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setStatus(Case.CaseStatus.OPEN);
        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setStatus(Case.CaseStatus.CLOSED);

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2));

        boolean exists = caseRepository.existsByIdAndStatus("MOCK-CASE-001", Case.CaseStatus.OPEN);
        assertTrue("The case does not exits ", exists);

        exists = caseRepository.existsByIdAndStatus("MOCK-CASE-002", Case.CaseStatus.CLOSED);
        assertTrue("Case does not exists", exists);

        exists = caseRepository.existsByIdAndStatus("MOCK-CASE-001", Case.CaseStatus.CLOSED);
        assertFalse("Case should not be present", exists);

        exists = caseRepository.existsByIdAndStatus("MOCK-CASE-002", Case.CaseStatus.OPEN);
        assertFalse("Case should not be present", exists);

    }

    @Test
    public void findByClinicId() {
        mongoTemplate.dropCollection(Case.class);
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setClinicId("CLINIC-001");
        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setClinicId("CLINIC-001");
        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setId("MOCK-CASE-003");
        mockCase3.setClinicId("CLINIC-002");

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2, mockCase3));

        List<Case> casesForClinic = caseRepository.findByClinicId("CLINIC-001");
        assertEquals("There should be 2 cases for clinic [CLINIC-001]", 2, casesForClinic.size());
        casesForClinic.forEach(aCase -> assertEquals("The clinic should be [CLINIC-001]", "CLINIC-001", aCase.getClinicId()));

        casesForClinic = caseRepository.findByClinicId("CLINIC-002");
        assertEquals("There should be 2 cases for clinic [CLINIC-002]", 1, casesForClinic.size());
        casesForClinic.forEach(aCase -> assertEquals("The clinic should be [CLINIC-002]", "CLINIC-002", aCase.getClinicId()));

    }

    @Test
    public void findByClinicIdWithPaging() {
        mongoTemplate.dropCollection(Case.class);
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setClinicId("CLINIC-001");
        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setClinicId("CLINIC-001");
        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setId("MOCK-CASE-003");
        mockCase3.setClinicId("CLINIC-002");

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2, mockCase3));

        List<Case> casesForClinic = caseRepository.findByClinicId("CLINIC-001", PageRequest.of(0, 1));
        assertEquals("There should be 1 case for clinic [CLINIC-001] in page 1", 1, casesForClinic.size());
        Case page1Case = casesForClinic.get(0);

        casesForClinic = caseRepository.findByClinicId("CLINIC-001", PageRequest.of(1, 1));
        assertEquals("There should be 1 case for clinic [CLINIC-001] in page 2", 1, casesForClinic.size());
        Case page2Case = casesForClinic.get(0);

        assertNotEquals("Two cases should be different", page1Case.getId(), page2Case.getId());

        casesForClinic = caseRepository.findByClinicId("CLINIC-001", PageRequest.of(0, 10));
        assertEquals("There should be 2 cases for clinic [CLINIC-001] in page 2", 2, casesForClinic.size());

    }

    @Test
    public void findCasesByPatientId() {
        mongoTemplate.dropCollection(Case.class);
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setClinicId("CLINIC-001");
        mockCase.setPatientId("PATIENT-001");
        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setClinicId("CLINIC-001");
        mockCase2.setPatientId("PATIENT-002");
        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setId("MOCK-CASE-003");
        mockCase3.setClinicId("CLINIC-002");
        mockCase3.setPatientId("PATIENT-003");

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2, mockCase3));

        List<Case> casesByPatientId = caseRepository.findCasesByPatientId("PATIENT-001");

        assertEquals("There should be only one case", 1, casesByPatientId.size());
        assertEquals("Wrong case was retrieved", "MOCK-CASE-001", casesByPatientId.get(0).getId());
        assertEquals("Wrong case was retrieved", "CLINIC-001", casesByPatientId.get(0).getClinicId());
    }

    @Test
    public void findCasesByClinicIdAndMedicalCoveragePlanIds() {
        mongoTemplate.dropCollection(SalesOrder.class);
        mongoTemplate.dropCollection(Case.class);

        SalesOrder mockSalesOrder = salesOrderRandomizer.getRandomValue();
        mockSalesOrder.setId("SALES-ORDER-001");

        mongoTemplate.insert(mockSalesOrder);

        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setClinicId("CLINIC-001");
        mockCase.setPatientId("PATIENT-001");
        mockCase.setSalesOrder(mockSalesOrder);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setClinicId("CLINIC-002");
        mockCase2.setPatientId("PATIENT-002");
        mockCase2.setSalesOrder(mockSalesOrder);
        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setId("MOCK-CASE-003");
        mockCase3.setClinicId("CLINIC-002");
        mockCase3.setPatientId("PATIENT-003");
        mockCase3.setSalesOrder(mockSalesOrder);

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2, mockCase3));

        LocalDateTime startDate = LocalDate.now().atStartOfDay().minusDays(2);
        LocalDateTime endDate = LocalDate.now().atStartOfDay().plusDays(1);

        List<Case> casesList = caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds("CLINIC-001",
                Arrays.asList("COVERAGE-PLAN-001", "COVERAGE-PLAN-003"),startDate,endDate);

        assertEquals("There should be only one case for this", 1, casesList.size());
        assertEquals("Wrong case was retrieved", "MOCK-CASE-001", casesList.get(0).getId());

        casesList = caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds("CLINIC-002",
                Arrays.asList("COVERAGE-PLAN-001", "COVERAGE-PLAN-003"),startDate,endDate);
        assertEquals("There should be 2 cases for this", 2, casesList.size());
    }

    @Test
    public void findCasesByMedicalCoveragePlanIdsIn_withDateRange() {
        mongoTemplate.dropCollection(SalesOrder.class);
        mongoTemplate.dropCollection(Case.class);

        SalesOrder mockSalesOrder = salesOrderRandomizer.getRandomValue();
        mockSalesOrder.setId("SALES-ORDER-001");

        mongoTemplate.insert(mockSalesOrder);


        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setClinicId("CLINIC-001");
        mockCase.setPatientId("PATIENT-001");
        mockCase.setSalesOrder(mockSalesOrder);
        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setClinicId("CLINIC-002");
        mockCase2.setPatientId("PATIENT-002");
        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setId("MOCK-CASE-003");
        mockCase3.setClinicId("CLINIC-002");
        mockCase3.setPatientId("PATIENT-003");

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2, mockCase3));

        List<Case> casesList = caseRepository.findCasesByMedicalCoveragePlanIdsIn(Arrays.asList("COVERAGE-PLAN-001", "COVERAGE-PLAN-005"),
                LocalDate.now().atStartOfDay().minusDays(1),LocalDate.now().atStartOfDay().plusDays(1));
        assertEquals("There should be only 1 case for this", 1, casesList.size());
        // check the case
        Case resultCase = casesList.get(0);
        assertEquals("Wrong case was fetched","MOCK-CASE-001",resultCase.getId());
    }

    @Test
    public void findByVisitIdsContains() {
        mongoTemplate.dropCollection(Case.class);

        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setId("MOCK-CASE-001");
        mockCase.setVisitIds(Arrays.asList("VISIT-001", "VISIT-002"));

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setId("MOCK-CASE-002");
        mockCase2.setVisitIds(Arrays.asList("VISIT-003", "VISIT-004"));
        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setId("MOCK-CASE-003");
        mockCase3.setVisitIds(Arrays.asList("VISIT-005", "VISIT-006"));

        mongoTemplate.insertAll(Arrays.asList(mockCase, mockCase2, mockCase3));

        Case caseForVisit = caseRepository.findByVisitIdsContains("VISIT-001");
        assertNotNull("Case was not retrieved", caseForVisit);
        assertEquals("Wrong case was retrieved", "MOCK-CASE-001", caseForVisit.getId());
        assertTrue("Wrong case was retrieved", caseForVisit.getVisitIds().contains("VISIT-001"));
    }
}

