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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.ilt.cms.repository.spring.*"}))
public class CaseRepositoryImplTest {

    private static AtomicInteger claimNumberCounter = new AtomicInteger(0);
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CaseRepositoryImpl caseRepositoryCustom;

    private EnhancedRandom caseRandomEnhancer;
    private Randomizer<Invoice> invoiceRandomizer;
    private Randomizer<Invoice> normalInvoiceRandomizer;

    private static Claim createMockClaim(LinkedList<Claim.ClaimStatus> statusList) {
        Claim.ClaimStatus claimStatus = statusList.pop();
        statusList.addLast(claimStatus);
        Claim claim = new Claim();
        claim.setClaimStatus(claimStatus);
        claim.setClaimId("CLAIM-0" + claimNumberCounter.incrementAndGet());
        claim.setClaimDoctorId("DOCTOR-001");
        claim.setAttendingDoctorId("DOCTOR-002");
        claim.setClaimExpectedAmt(10);
        claim.setConsultationAmt(12);
        claim.setGstAmount(1001);
        claim.setMedicalTestAmt(50);
        claim.setMedicationAmt(105);
        claim.setOtherAmt(120);
        claim.setRemark("Remark");
        claim.setPayersName("PAYER-NAME-001");
        claim.setPayersNric("NRIC-001");
        claim.setSubmissionDateTime(LocalDateTime.now().minusDays(5));
        claim.setClaimResult(
                new Claim.ClaimResult("claimResult-ref-1", LocalDateTime.now(), 20,
                        "S0000", "Claim Partially Passed"));

        claim.setPaidResult(new Claim.ClaimResult("paid-result-ref-1", LocalDateTime.now(),
                80, "S0000", "paid result - 1"));

        claim.setSubmissionResult(new Claim.SubmissionResult("1010", "S0000", "Success"));

        claim.setDiagnosisCodes(Arrays.asList("11", "22", "33", "44"));

        claim.setAppealRejections(Arrays.asList(new Claim.AppealRejection() {{
            this.setReason("reject-1");
        }}, new Claim.AppealRejection() {{
            this.setReason("reject-2");
        }}));
        return claim;
    }

    @Before
    public void setUp() throws Exception {

        LinkedList<Claim.ClaimStatus> claimStatuses = new LinkedList() {{
            this.add(Claim.ClaimStatus.APPROVED);
            this.add(Claim.ClaimStatus.PAID);
            this.add(Claim.ClaimStatus.REJECTED);
            this.add(Claim.ClaimStatus.PENDING);
        }};

        invoiceRandomizer = new Randomizer() {
            private EnhancedRandom invoiceRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

            @Override
            public Invoice getRandomValue() {
                Invoice invoice = invoiceRandom.nextObject(Invoice.class);
                invoice.setInvoiceType(Invoice.InvoiceType.CREDIT);
                invoice.setClaim(createMockClaim(claimStatuses));
                invoice.setInvoiceTime(LocalDate.now().atStartOfDay().minusDays(5));
                return invoice;
            }
        };

        normalInvoiceRandomizer = new Randomizer() {
            private EnhancedRandom invoiceRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

            @Override
            public Invoice getRandomValue() {
                return invoiceRandom.nextObject(Invoice.class);
            }
        };

        Randomizer<SalesOrder> salesOrderRandomizer = new Randomizer() {
            EnhancedRandom salesOrderRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                    .randomizationDepth(2)
                    .exclude(FieldDefinitionBuilder.field().named("purchaseItem").get())
                    .exclude(FieldDefinitionBuilder.field().named("id").get())
                    .randomize(FieldDefinitionBuilder.field()
                            .named("invoices")
                            .ofType(List.class)
                            .inClass(SalesOrder.class).get(), new ListRandomizer(invoiceRandomizer, 4))
                    .build();

            @Override
            public SalesOrder getRandomValue() {
                return salesOrderRandom.nextObject(SalesOrder.class);
            }
        };

        caseRandomEnhancer = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .seed(10L)
                .randomizationDepth(2)
                .exclude(FieldDefinitionBuilder.field().named("id").get())
                .randomize(FieldDefinitionBuilder.field()
                        .named("salesOrder")
                        .ofType(SalesOrder.class).inClass(Case.class).get(), salesOrderRandomizer)
                .scanClasspathForConcreteTypes(true).build();

        dropCollections();
    }

    private void dropCollections() {
        mongoTemplate.dropCollection(Case.class);
        mongoTemplate.dropCollection(SalesOrder.class);
    }

    @Test
    public void updateClaimByInvoiceIdAndClaimId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");
        SalesOrder salesOrder = mockCase.getSalesOrder();

        Invoice oldInvoice1 = salesOrder.getInvoices().get(0);
        Invoice oldInvoice2 = salesOrder.getInvoices().get(1);
        oldInvoice1.setPlanId("OLD_COVERAGE_PLAN-001");
        oldInvoice2.setPlanId("OLD_COVERAGE_PLAN-002");

        String oldInvoicePlanId = oldInvoice1.getPlanId();

        salesOrder.setInvoices(Arrays.asList(oldInvoice1, oldInvoice2));

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        oldInvoice1.setPlanId("COV-PLAN-001"); //this should not be updated
        Claim claim = oldInvoice1.getClaim();
        claim.setAttendingDoctorId("DOCTOR-003");

        caseRepositoryCustom.updateClaimByInvoiceIdAndClaimId(oldInvoice1.getInvoiceNumber(), claim);
        Case caseByClaimId = caseRepositoryCustom.findCaseByClaimId(claim.getClaimId());
        assertNotNull("Case was not found", caseByClaimId);
        Invoice invoiceForUpdatedClaim = caseByClaimId.getSalesOrder().getInvoices().get(0);
        assertEquals("Doctor was different ", "DOCTOR-003", invoiceForUpdatedClaim.getClaim().getAttendingDoctorId());
        assertEquals("Invoice was changed ", oldInvoicePlanId, invoiceForUpdatedClaim.getPlanId());
    }

    @Test
    public void updateInvoice() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");
        SalesOrder salesOrder = mockCase.getSalesOrder();

        Invoice oldInvoice1 = salesOrder.getInvoices().get(0);
        Invoice oldInvoice2 = salesOrder.getInvoices().get(1);
        oldInvoice1.setInvoiceNumber("OLD-INVOICE-NUMBER-001");
        oldInvoice1.setPlanId("OLD_COVERAGE_PLAN-001");
        oldInvoice2.setInvoiceNumber("OLD-INVOICE-NUMBER-002");
        oldInvoice2.setPlanId("OLD_COVERAGE_PLAN-002");

        oldInvoice1.getClaim().setClaimId("OLD-INVOICE-CLAIM-001");
        oldInvoice1.getClaim().setAttendingDoctorId("OLD-INVOICE-CLAIM-ATTENDING-DOCTOR-001");

        salesOrder.setInvoices(Arrays.asList(oldInvoice1, oldInvoice2));

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Invoice newInvoice = salesOrder.getInvoices()
                .stream()
                .filter(invoice -> "OLD-INVOICE-NUMBER-001".equals(invoice.getInvoiceNumber()))
                .findFirst().get();
        newInvoice.setInvoiceNumber("NEW-INVOICE-001");
        newInvoice.setPlanId("NEW-INVOICE-PLAN-001");
        Claim claim = oldInvoice1.getClaim();
        claim.setClaimId("NEW_INVOICE-CLAIM-001");
        claim.setAttendingDoctorId("DOCTOR-003");

        caseRepositoryCustom.updateInvoiceBySalesOrderIdAndInvoiceNumber(salesOrder.getId(), "OLD-INVOICE-NUMBER-001", newInvoice);

        Query findSalesOrderQuery = new Query();
        findSalesOrderQuery.addCriteria(Criteria.where("_id").is(salesOrder.getId()));
        SalesOrder updatedSalesOrder = mongoTemplate.findOne(findSalesOrderQuery, SalesOrder.class);
        assertNotNull("Sales order was not fetched", updatedSalesOrder);
        // check for sales order fields
        assertEquals("Sales order was modified", salesOrder.getSalesNumber(), updatedSalesOrder.getSalesNumber());
        assertEquals("Sales order was modified", salesOrder.getStatus(), updatedSalesOrder.getStatus());
        assertEquals("Sales order was modified", salesOrder.getTaxValue(), updatedSalesOrder.getTaxValue());
        assertEquals("Sales order was modified", salesOrder.getDispenses().size(), updatedSalesOrder.getDispenses().size());

        // check for invoices
        assertEquals("Invoice number was changed", 2, updatedSalesOrder.getInvoices().size());
        updatedSalesOrder.getInvoices().forEach(invoice -> {
            if ("NEW-INVOICE-001".equals(invoice.getInvoiceNumber())) {
                assertEquals("Invoice plan id was not changed", "NEW-INVOICE-PLAN-001", invoice.getPlanId());
                assertEquals("Claim's id was not changed", "NEW_INVOICE-CLAIM-001", invoice.getClaim().getClaimId());
                assertEquals("Claim's attending doctor was not changed", "DOCTOR-003", invoice.getClaim().getAttendingDoctorId());
                System.out.println("=====> Invoice's fields was updated properly");
            } else if ("OLD-INVOICE-NUMBER-001".equals(invoice.getInvoiceNumber())) {
                Assert.fail("The invoice was not updated");
            } else if ("OLD-INVOICE-NUMBER-002".equals(invoice.getInvoiceNumber())) {
                assertEquals("Invoice plan id was changed", "OLD_COVERAGE_PLAN-002", invoice.getPlanId());
            }
        });

        // check the other invoice
        Optional<Invoice> untouchedInvoiceOpt = updatedSalesOrder.getInvoices().stream()
                .filter(invoice -> "OLD-INVOICE-NUMBER-002".equals(invoice.getInvoiceNumber()))
                .findFirst();
        assertTrue("Other invoice was removed", untouchedInvoiceOpt.isPresent());
        assertEquals("Invoice plan id was changed", "OLD_COVERAGE_PLAN-002", untouchedInvoiceOpt.get().getPlanId());
        System.out.println("=====> Other invoices was not updated");

    }

    @Test
    public void addNewInvoiceToSalesOrderWithId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");
        SalesOrder salesOrder = mockCase.getSalesOrder();

        Invoice oldInvoice1 = salesOrder.getInvoices().get(0);
        Invoice oldInvoice2 = salesOrder.getInvoices().get(1);
        oldInvoice1.setInvoiceNumber("OLD-INVOICE-NUMBER-001");
        oldInvoice1.setPlanId("OLD_COVERAGE_PLAN-001");

        oldInvoice2.setInvoiceNumber("OLD-INVOICE-NUMBER-002");
        oldInvoice2.setPlanId("OLD_COVERAGE_PLAN-002");

        oldInvoice1.getClaim().setClaimId("OLD-INVOICE-CLAIM-001");
        oldInvoice1.getClaim().setAttendingDoctorId("OLD-INVOICE-CLAIM-ATTENDING-DOCTOR-001");

        salesOrder.setInvoices(Arrays.asList(oldInvoice1, oldInvoice2));

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Invoice newInvoice = new Invoice("CLAIM-DUE-INVOICE-001", Invoice.InvoiceType.DIRECT, "OLD_COVERAGE_PLAN-001");
        newInvoice.setPayableAmount(1000);
        newInvoice.setVisitId("CURRENT-VISIT-ID");

        caseRepositoryCustom.addNewInvoiceToSalesOrderWithId(salesOrder.getId(), newInvoice);

        Query findSalesOrderQuery = new Query();
        findSalesOrderQuery.addCriteria(Criteria.where("_id").is(salesOrder.getId()));
        SalesOrder updatedSalesOrder = mongoTemplate.findOne(findSalesOrderQuery, SalesOrder.class);
        assertNotNull("Sales order was not fetched", updatedSalesOrder);
        // check for sales order fields
        assertEquals("Sales order was modified", salesOrder.getSalesNumber(), updatedSalesOrder.getSalesNumber());
        assertEquals("Sales order was modified", salesOrder.getStatus(), updatedSalesOrder.getStatus());
        assertEquals("Sales order was modified", salesOrder.getTaxValue(), updatedSalesOrder.getTaxValue());
        assertEquals("Sales order was modified", salesOrder.getDispenses().size(), updatedSalesOrder.getDispenses().size());

        // check for invoices
        assertEquals("Invoice was not added properly", 3, updatedSalesOrder.getInvoices().size());
        updatedSalesOrder.getInvoices().forEach(invoice -> {
            if ("CLAIM-DUE-INVOICE-001".equals(invoice.getInvoiceNumber())) {
                assertEquals("Invoice plan id was changed", "OLD_COVERAGE_PLAN-001", invoice.getPlanId());
                assertEquals("Invoice visit id was changed", "CURRENT-VISIT-ID", invoice.getVisitId());
                assertEquals("Invoice type was changed", Invoice.InvoiceType.DIRECT, invoice.getInvoiceType());
                assertEquals("Invoice payable amount id was changed", 1000, invoice.getPayableAmount());
                System.out.println("=====> New Invoice was added properly");
            } else if ("OLD-INVOICE-NUMBER-001".equals(invoice.getInvoiceNumber())) {
                assertEquals("Invoice plan id was changed", "OLD_COVERAGE_PLAN-001", invoice.getPlanId());
                System.out.println("=====> OLD-INVOICE-NUMBER-001 was not changed");
            } else if ("OLD-INVOICE-NUMBER-002".equals(invoice.getInvoiceNumber())) {
                assertEquals("Invoice plan id was changed", "OLD_COVERAGE_PLAN-002", invoice.getPlanId());
                System.out.println("=====> OLD-INVOICE-NUMBER-002 was not changed");
            } else {
                Assert.fail("This shouldn't happen !!!");
            }
        });

    }

    @Test
    public void findClaimByInvoiceId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        Invoice invoice = salesOrder.getInvoices().get(0);
        invoice.setInvoiceNumber("INVOICE-NO-001");
        Claim claim = invoice.getClaim();

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Optional<Claim> claimByInvoiceIdOpt = caseRepositoryCustom.findClaimByInvoiceId("INVOICE-NO-001");
        assertTrue("Claim was not retrieved", claimByInvoiceIdOpt.isPresent());
        Claim claimByInvoice = claimByInvoiceIdOpt.get();
        assertEquals(claim.getClaimId(), claimByInvoice.getClaimId());
        assertEquals(claim.getClaimRefNo(), claimByInvoice.getClaimRefNo());

    }

    @Test
    public void findInvoiceById() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        Invoice invoice = salesOrder.getInvoices().get(0);
        invoice.setInvoiceNumber("INVOICE-NO-001");

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Optional<Invoice> invoiceByIdOpt = caseRepositoryCustom.findInvoiceById("INVOICE-NO-001");
        assertTrue("Invoice was not retrieved", invoiceByIdOpt.isPresent());
        Invoice invoiceById = invoiceByIdOpt.get();
        assertEquals("Invoice received is different ", "INVOICE-NO-001", invoiceById.getInvoiceNumber());
        assertEquals("Invoice's plan id was different ", invoice.getPlanId(), invoiceById.getPlanId());

    }

    @Test
    public void findAllInvoicesForGivenTimePeriodByPatientId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");
        mockCase.setPatientId("TEST-PATIENT-ID-001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        final AtomicInteger invCount = new AtomicInteger(0);
        salesOrder.getInvoices().forEach(invoice -> {
            invoice.setInvoiceTime(LocalDate.now().atStartOfDay().plusHours(12));
            invoice.setInvoiceNumber("INVOICE-TEST-PATIENT-ID-001-" + invCount.incrementAndGet());
        });

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setPatientId("PATIENT-123");

        mongoTemplate.save(mockCase2.getSalesOrder());
        mongoTemplate.save(mockCase2);

        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setPatientId("PATIENT-456");

        mongoTemplate.save(mockCase3.getSalesOrder());
        mongoTemplate.save(mockCase3);

        List<Invoice> invoicesForPatient = caseRepositoryCustom.findAllInvoicesForGivenTimePeriodByPatientId("TEST-PATIENT-ID-001"
                , LocalDate.now().atStartOfDay().minusDays(1), LocalDate.now().atStartOfDay().plusDays(1));

        assertTrue("Invoice was not retrieved", Objects.nonNull(invoicesForPatient));
        assertEquals("Number of invoices not match", 4, invoicesForPatient.size());
        invoicesForPatient.forEach(invoice -> {
            assertTrue("Invoice number error", invoice.getInvoiceNumber()
                    .contains("INVOICE-TEST-PATIENT-ID-001-"));
            System.out.println(invoice);

        });
    }

    @Test
    public void findFirstInvoiceOfPlanForPatient() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setClinicId("CLINIC001");
        mockCase.setPatientId("TEST-PATIENT-ID-001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        final AtomicInteger invCount = new AtomicInteger(0);
        salesOrder.getInvoices().forEach(invoice -> {
            invoice.setInvoiceTime(LocalDate.now().atStartOfDay().minusDays(5 - invCount.incrementAndGet()));
            invoice.setInvoiceNumber("INVOICE-TEST-PATIENT-ID-001-" + invCount.get());
            invoice.setPlanId("TEST-PLAN-00" + invCount.get());
        });

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mockCase2.setPatientId("TEST-PATIENT-ID-001");
        mockCase2.getSalesOrder();
        salesOrder.getInvoices().forEach(invoice -> {
            invoice.setInvoiceTime(LocalDate.now().atStartOfDay().minusDays(5 - invCount.incrementAndGet()));
            invoice.setInvoiceNumber("INVOICE-TEST-PATIENT-ID-001-" + invCount.get());
            invoice.setPlanId("TEST-PLAN-00" + invCount.get());
        });

        mongoTemplate.save(mockCase2.getSalesOrder());
        mongoTemplate.save(mockCase2);

        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mockCase3.setPatientId("PATIENT-456");

        mongoTemplate.save(mockCase3.getSalesOrder());
        mongoTemplate.save(mockCase3);

        Optional<Invoice> firstInvoiceOpt = caseRepositoryCustom.findFirstInvoiceOfPlanForPatient("TEST-PATIENT-ID-001"
                , "TEST-PLAN-001");

        assertTrue("Invoice was not retrieved", firstInvoiceOpt.isPresent());
        Invoice firstInvoice = firstInvoiceOpt.get();
        assertEquals("Invoice date error", LocalDate.now().atStartOfDay().minusDays(4), firstInvoice.getInvoiceTime());
        System.out.println(firstInvoice);
    }

    @Test
    public void findCaseByClaimNo() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setCaseNumber("CASE-NO-TEST-001");
        mockCase.setClinicId("CLINIC001");
        mockCase.setPatientId("TEST-PATIENT-ID-001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        final AtomicInteger invCount = new AtomicInteger(0);
        salesOrder.getInvoices().forEach(invoice -> {
            invoice.setInvoiceTime(LocalDate.now().atStartOfDay().minusDays(5 - invCount.incrementAndGet()));
            invoice.setInvoiceNumber("INVOICE-TEST-PATIENT-ID-001-" + invCount.get());
            invoice.setPlanId("TEST-PLAN-00" + invCount.get());
            invoice.getClaim().getSubmissionResult().setClaimNo("TEST-CLAIM-SUB-RES-00" + invCount.get());
        });

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase2.getSalesOrder());
        mongoTemplate.save(mockCase2);

        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase3.getSalesOrder());
        mongoTemplate.save(mockCase3);

        Case caseByClaimNo = caseRepositoryCustom.findCaseByClaimNo("TEST-CLAIM-SUB-RES-001");

        assertTrue("Case was not retrieved", Objects.nonNull(caseByClaimNo));
        assertEquals("Case is different", "CASE-NO-TEST-001", caseByClaimNo.getCaseNumber());
        assertEquals("Case is different", "CLINIC001", caseByClaimNo.getClinicId());
        assertEquals("Case is different", "TEST-PATIENT-ID-001", caseByClaimNo.getPatientId());
        Optional<Claim> claimOptional = caseByClaimNo.getSalesOrder().getInvoices().stream().map(Invoice::getClaim)
                .filter(claim -> "TEST-CLAIM-SUB-RES-001".equals(claim.getSubmissionResult().getClaimNo())).findFirst();
        assertTrue("Claim was not in the case", claimOptional.isPresent());
    }

    @Test
    public void findCaseByClaimId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setCaseNumber("CASE-NO-TEST-001");
        mockCase.setClinicId("CLINIC001");
        mockCase.setPatientId("TEST-PATIENT-ID-001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        final AtomicInteger invCount = new AtomicInteger(0);
        salesOrder.getInvoices().forEach(invoice -> {
            invoice.setInvoiceTime(LocalDate.now().atStartOfDay().minusDays(5 - invCount.incrementAndGet()));
            invoice.setInvoiceNumber("INVOICE-TEST-PATIENT-ID-001-" + invCount.get());
            invoice.setPlanId("TEST-PLAN-00" + invCount.get());
            invoice.getClaim().setClaimId("TEST-CLAIM-00" + invCount.get());
        });

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase2.getSalesOrder());
        mongoTemplate.save(mockCase2);

        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase3.getSalesOrder());
        mongoTemplate.save(mockCase3);

        Case caseByClaimNo = caseRepositoryCustom.findCaseByClaimId("TEST-CLAIM-001");

        assertTrue("Case was not retrieved", Objects.nonNull(caseByClaimNo));
        assertEquals("Case is different", "CASE-NO-TEST-001", caseByClaimNo.getCaseNumber());
        assertEquals("Case is different", "CLINIC001", caseByClaimNo.getClinicId());
        assertEquals("Case is different", "TEST-PATIENT-ID-001", caseByClaimNo.getPatientId());
        Optional<Claim> claimOptional = caseByClaimNo.getSalesOrder().getInvoices().stream().map(Invoice::getClaim)
                .filter(claim -> "TEST-CLAIM-001".equals(claim.getClaimId())).findFirst();
        assertTrue("Claim was not in the case", claimOptional.isPresent());
    }

    @Test
    public void findClinicIdByClaimId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);
        mockCase.setCaseNumber("CASE-NO-TEST-001");
        mockCase.setClinicId("CLINIC001");
        mockCase.setPatientId("TEST-PATIENT-ID-001");

        SalesOrder salesOrder = mockCase.getSalesOrder();
        final AtomicInteger invCount = new AtomicInteger(0);
        salesOrder.getInvoices().forEach(invoice -> {
            invoice.getClaim().setClaimId("TEST-CLAIM-00" + invCount.incrementAndGet());
        });

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase2.getSalesOrder());
        mongoTemplate.save(mockCase2);

        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase3.getSalesOrder());
        mongoTemplate.save(mockCase3);

        String clinicId = caseRepositoryCustom.findClinicIdByClaimId("TEST-CLAIM-001");

        assertTrue("Clinic Id was not retrieved", Objects.nonNull(clinicId));
        assertEquals("Clinic Id was different", "CLINIC001", clinicId);
    }

    @Test
    public void findClaimByClaimId() {
        dropCollections();
        Case mockCase = caseRandomEnhancer.nextObject(Case.class);

        SalesOrder salesOrder = mockCase.getSalesOrder();
        final AtomicInteger invCount = new AtomicInteger(0);
        AtomicReference<Claim> testClaim = new AtomicReference<>();
        salesOrder.getInvoices().forEach(invoice -> {
            int counterValue = invCount.incrementAndGet();
            Claim claim = invoice.getClaim();
            claim.setClaimId("TEST-CLAIM-00" + counterValue);
            if (counterValue == 1) {
                // TEST-CLAIM-001's values
                claim.setAttendingDoctorId("TEST-ATTENDING_DOCTOR-001");
                claim.setClaimedAmount(1000);
                claim.setClaimExpectedAmt(1000);
                claim.setSubmissionResult(null);
                claim.setClaimResult(null);
                claim.setPaidResult(null);
                claim.setSubmissionDateTime(null);
                claim.setClaimStatus(Claim.ClaimStatus.PAID);
                claim.setAppealRejections(Collections.emptyList());
                claim.setDiagnosisCodes(Collections.emptyList());
                testClaim.set(claim);
            }
        });

        mongoTemplate.save(salesOrder);
        mongoTemplate.save(mockCase);

        Case mockCase2 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase2.getSalesOrder());
        mongoTemplate.save(mockCase2);

        Case mockCase3 = caseRandomEnhancer.nextObject(Case.class);
        mongoTemplate.save(mockCase3.getSalesOrder());
        mongoTemplate.save(mockCase3);

        Optional<Claim> claimFromIdOpt = caseRepositoryCustom.findClaimByClaimId("TEST-CLAIM-001");

        assertTrue("Claim was not retrieved", claimFromIdOpt.isPresent());
        assertEquals("Clinic Id was different", testClaim.get().toString().trim(), claimFromIdOpt.get().toString().trim());
    }
}