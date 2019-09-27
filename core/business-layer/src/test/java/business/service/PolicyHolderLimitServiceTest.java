package business.service;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.pm.business.service.coverage.PolicyHolderLimitService;
import com.ilt.cms.repository.spring.CaseRepository;
import com.ilt.cms.repository.spring.CaseRepositoryImpl;
import com.ilt.cms.repository.spring.PatientVisitRegistryRepository;
import com.ilt.cms.repository.spring.SalesOrderRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PolicyHolderLimitServiceTest {

    private PolicyHolderLimitService policyHolderLimitService;

    @Before
    public void setUp() throws Exception {
        CaseRepositoryImpl caseRepositoryCustomImpl = Mockito.mock(CaseRepositoryImpl.class);
        CaseRepository caseRepository = Mockito.mock(CaseRepository.class);
        MedicalCoverageRepository medicalCoverageRepository = Mockito.mock(MedicalCoverageRepository.class);
        PatientVisitRegistryRepository patientVisitRegistryRepository = Mockito.mock(PatientVisitRegistryRepository.class);
        SalesOrderRepository salesOrderRepository = Mockito.mock(SalesOrderRepository.class);

        policyHolderLimitService = new PolicyHolderLimitService(caseRepositoryCustomImpl
                , caseRepository, medicalCoverageRepository, patientVisitRegistryRepository, salesOrderRepository);
        policyHolderLimitService.initCache(); // executing init manually as not using spring runner

        // setup mock cases
        when(caseRepository.findCasesByPatientId(anyString()))
                .thenReturn(Arrays.asList(
                        new Case() {{
                            this.setAttachedMedicalCoverages(Arrays.asList(
                                    new AttachedMedicalCoverage("coverage-plan-1")
                                    , new AttachedMedicalCoverage("coverage-plan-2")
                                    , new AttachedMedicalCoverage("coverage-plan-3")
                                    , new AttachedMedicalCoverage("coverage-plan-4")
                                    , new AttachedMedicalCoverage("coverage-plan-5")
                            ));
                        }}
                ));
        when(medicalCoverageRepository.findMedicalCoverageByPlanId(anyString(), any()))
                .thenReturn(mockMedicalCoverages());
        when(medicalCoverageRepository.findMedicalCoverageByPlanId(anyString()))
                .thenReturn(mockMedicalCoverages().get(0));

        when(caseRepositoryCustomImpl.findFirstInvoiceOfPlanForPatient(anyString(), anyString()))
                .thenReturn(Optional.of(
                        new Invoice() {{
                            this.setInvoiceTime(LocalDate.now().atStartOfDay().minusMonths(1));
                        }}
                ));

        when(caseRepositoryCustomImpl.findAllInvoicesForGivenTimePeriodByPatientId(anyString(), any(), any()))
                .thenReturn(mockInvoiceList());

        when(salesOrderRepository.findSalesOrdersWithInvoicesCreatedInSearchPeriodWithVisitIds(anyList(), any(), any()))
                .thenReturn(Arrays.asList(
                        new SalesOrder() {{
                            this.setInvoices(mockInvoiceList());
                        }}
                ));


    }

    private List<Invoice> mockInvoiceList() {
        final LocalDateTime now = LocalDateTime.now();//LocalDateTime.of(LocalDate.of(2019, Month.FEBRUARY, 5), LocalTime.now()); //This appears to be problematic. Hence, the dates are set relatively to date
        return Arrays.asList(
                new Invoice() {{
                    this.setInvoiceTime(now.minusYears(1));
                    this.setClaim(new Claim() {{
                        this.setClaimStatus(ClaimStatus.PAID);
                    }});
                    this.setPaidAmount(1500);
                    this.setPlanId("coverage-plan-5");
                    this.setInvoiceType(InvoiceType.CREDIT);
                    this.setInvoiceState(InvoiceState.PAID);
                }},
                new Invoice() {{
                    this.setInvoiceTime(now.minusMonths(1));
                    this.setClaim(new Claim() {{
                        this.setClaimStatus(ClaimStatus.REJECTED);
                    }});
                    this.setPaidAmount(500);
                    this.setPlanId("coverage-plan-4");
                    this.setInvoiceType(InvoiceType.CREDIT);
                    this.setInvoiceState(InvoiceState.PAID);
                }},
                new Invoice() {{
                    this.setInvoiceTime(now.minusHours(2));
                    this.setClaim(new Claim() {{
                        this.setClaimStatus(ClaimStatus.SUBMITTED);
                    }});
                    this.setPayableAmount(200);
                    this.setPaidAmount(0);
                    this.setPlanId("coverage-plan-3");
                    this.setInvoiceType(InvoiceType.CREDIT);
                    this.setInvoiceState(InvoiceState.INITIAL);
                }},
                new Invoice() {{
                    this.setInvoiceTime(now.minusHours(2));
                    this.setClaim(new Claim() {{
                        this.setClaimStatus(ClaimStatus.PENDING);
                    }});
                    this.setPayableAmount(50);
                    this.setPlanId("coverage-plan-2");
                    this.setInvoiceType(InvoiceType.CREDIT);
                    this.setInvoiceState(InvoiceState.INITIAL);
                }},
                new Invoice() {{
                    this.setInvoiceTime(now.minusHours(1));
                    this.setClaim(new Claim() {{
                        this.setClaimStatus(ClaimStatus.PENDING);
                    }});
                    this.setPayableAmount(125);
                    this.setPlanId("coverage-plan-1");
                    this.setInvoiceType(InvoiceType.CREDIT);
                    this.setInvoiceState(InvoiceState.INITIAL);
                }}
        );
    }

    private List<MedicalCoverage> mockMedicalCoverages() {
        return Arrays.asList(
                new MedicalCoverage() {{
                    this.setCoveragePlans(Arrays.asList(
                            new CoveragePlan() {{
                                this.setId("coverage-plan-1");
                                this.setCapPerVisit(new CapLimiter(1, 100));
                                this.setCapPerDay(new CapLimiter(2, 150));
                                this.setCapPerWeek(new CapLimiter(4, 500));
                                this.setCapPerMonth(new CapLimiter(8, 1000));
                                this.setCapPerYear(new CapLimiter(16, 1500));
                            }},
                            new CoveragePlan() {{
                                this.setId("coverage-plan-2");
                                this.setCapPerVisit(new CapLimiter(1, 100));
                                this.setCapPerDay(new CapLimiter(2, 150));
                                this.setCapPerWeek(new CapLimiter(4, 500));
                                this.setCapPerMonth(new CapLimiter(8, 1000));
                                this.setCapPerYear(new CapLimiter(16, 1500));
                            }},
                            new CoveragePlan() {{
                                this.setId("coverage-plan-3");
                                this.setCapPerVisit(new CapLimiter(1, 100));
                                this.setCapPerDay(new CapLimiter(2, 150));
                                this.setCapPerWeek(new CapLimiter(4, 500));
                                this.setCapPerMonth(new CapLimiter(8, 1000));
                                this.setCapPerYear(new CapLimiter(16, 1500));
                            }},
                            new CoveragePlan() {{
                                this.setId("coverage-plan-4");
                                this.setCapPerVisit(new CapLimiter(1, 100));
                                this.setCapPerDay(new CapLimiter(2, 150));
                                this.setCapPerWeek(new CapLimiter(4, 500));
                                this.setCapPerMonth(new CapLimiter(8, 1000));
                                this.setCapPerYear(new CapLimiter(16, 1500));
                            }},
                            new CoveragePlan() {{
                                this.setId("coverage-plan-6");
                                this.setCapPerVisit(new CapLimiter(1, 100));
                                this.setCapPerDay(new CapLimiter(2, 150));
                                this.setCapPerWeek(new CapLimiter(4, 500));
                                this.setCapPerMonth(new CapLimiter(8, 1000));
                                this.setCapPerYear(new CapLimiter(16, 1500));
                            }}
                    ));
                }}
        );
    }

    @Test
    public void calculateCurrentAvailableDailyLimit() {
        int limit = policyHolderLimitService.calculateCurrentAvailableDailyLimit("P001", "coverage-plan-1");
        int limit2 = policyHolderLimitService.calculateCurrentAvailableDailyLimit("P001", "coverage-plan-2");
        int limit3 = policyHolderLimitService.calculateCurrentAvailableDailyLimit("P001", "coverage-plan-3");
        int limit4 = policyHolderLimitService.calculateCurrentAvailableDailyLimit("P001", "coverage-plan-4");
        int limit5 = policyHolderLimitService.calculateCurrentAvailableDailyLimit("P001", "coverage-plan-5");
        System.out.println("the limit for plan-1 : " + limit);
        System.out.println("the limit for plan-2 : " + limit2);
        System.out.println("the limit for plan-3 : " + limit3);
        System.out.println("the limit for plan-4 : " + limit4);
        System.out.println("the limit for plan-5 : " + limit5);
        assertEquals("limit error for plan 1 ", 25, limit);
        assertEquals("limit error for plan 2 ", 100, limit2);
        assertEquals("limit error for plan 3 ", -1, limit3);
        assertEquals("limit error for plan 4 ", 150, limit4);
        assertEquals("limit error for plan 5 ", -1, limit5);

    }

    @Test
    public void calculateCurrentAvailableWeeklyLimit() {
        int limit = policyHolderLimitService.calculateCurrentAvailableWeeklyLimit("P001", "coverage-plan-1");
        int limit2 = policyHolderLimitService.calculateCurrentAvailableWeeklyLimit("P001", "coverage-plan-2");
        int limit3 = policyHolderLimitService.calculateCurrentAvailableWeeklyLimit("P001", "coverage-plan-3");
        int limit4 = policyHolderLimitService.calculateCurrentAvailableWeeklyLimit("P001", "coverage-plan-4");
        int limit5 = policyHolderLimitService.calculateCurrentAvailableWeeklyLimit("P001", "coverage-plan-5");
        System.out.println("the limit for plan-1 : " + limit);
        System.out.println("the limit for plan-2 : " + limit2);
        System.out.println("the limit for plan-3 : " + limit3);
        System.out.println("the limit for plan-4 : " + limit4);
        System.out.println("the limit for plan-5 : " + limit5);
        assertEquals("limit error for plan 1 ", 375, limit);
        assertEquals("limit error for plan 2 ", 450, limit2);
        assertEquals("limit error for plan 3 ", 300, limit3);
        assertEquals("limit error for plan 4 ", 500, limit4);
        assertEquals("limit error for plan 5 ", -1, limit5);
    }

    @Test
    public void calculateCurrentAvailableMonthlyLimit() {
        int limit = policyHolderLimitService.calculateCurrentAvailableMonthlyLimit("P001", "coverage-plan-1");
        int limit2 = policyHolderLimitService.calculateCurrentAvailableMonthlyLimit("P001", "coverage-plan-2");
        int limit3 = policyHolderLimitService.calculateCurrentAvailableMonthlyLimit("P001", "coverage-plan-3");
        int limit4 = policyHolderLimitService.calculateCurrentAvailableMonthlyLimit("P001", "coverage-plan-4");
        int limit5 = policyHolderLimitService.calculateCurrentAvailableMonthlyLimit("P001", "coverage-plan-5");
        System.out.println("the limit for plan-1 : " + limit);
        System.out.println("the limit for plan-2 : " + limit2);
        System.out.println("the limit for plan-3 : " + limit3);
        System.out.println("the limit for plan-4 : " + limit4);
        System.out.println("the limit for plan-5 : " + limit5);
        assertEquals("limit error for plan 1 ", 875, limit);
        assertEquals("limit error for plan 2 ", 950, limit2);
        assertEquals("limit error for plan 3 ", 800, limit3);
        assertEquals("limit error for plan 4 ", 1000, limit4);
        assertEquals("limit error for plan 5 ", -1, limit5);
    }

    @Test
    public void calculateCurrentAvailableYearlyLimit() {
        int limit = policyHolderLimitService.calculateCurrentAvailableYearlyLimit("P001", "coverage-plan-1");
        int limit2 = policyHolderLimitService.calculateCurrentAvailableYearlyLimit("P001", "coverage-plan-2");
        int limit3 = policyHolderLimitService.calculateCurrentAvailableYearlyLimit("P001", "coverage-plan-3");
        int limit4 = policyHolderLimitService.calculateCurrentAvailableYearlyLimit("P001", "coverage-plan-4");
        int limit5 = policyHolderLimitService.calculateCurrentAvailableYearlyLimit("P001", "coverage-plan-5");
        System.out.println("the limit for plan-1 : " + limit);
        System.out.println("the limit for plan-2 : " + limit2);
        System.out.println("the limit for plan-3 : " + limit3);
        System.out.println("the limit for plan-4 : " + limit4);
        System.out.println("the limit for plan-5 : " + limit5);
        assertEquals("limit error for plan 1 ", 1375, limit);
        assertEquals("limit error for plan 2 ", 1450, limit2);
        assertEquals("limit error for plan 3 ", 1300, limit3);
        assertEquals("limit error for plan 4 ", 1500, limit4);
        assertEquals("limit error for plan 5 ", -1, limit5);
    }


    @Test
    public void calculateCurrentAvailableVisitLimit() {
        int limit = policyHolderLimitService.calculateCurrentAvailableVisitLimit("P001", "coverage-plan-1");
        int limit2 = policyHolderLimitService.calculateCurrentAvailableVisitLimit("P001", "coverage-plan-2");
        int limit3 = policyHolderLimitService.calculateCurrentAvailableVisitLimit("P001", "coverage-plan-3");
        int limit4 = policyHolderLimitService.calculateCurrentAvailableVisitLimit("P001", "coverage-plan-4");
        int limit5 = policyHolderLimitService.calculateCurrentAvailableVisitLimit("P001", "coverage-plan-5");
        System.out.println("the limit for plan-1 : " + limit);
        System.out.println("the limit for plan-2 : " + limit2);
        System.out.println("the limit for plan-3 : " + limit3);
        System.out.println("the limit for plan-4 : " + limit4);
        System.out.println("the limit for plan-5 : " + limit5);
        assertEquals("limit error for plan 1 ", 100, limit);
        assertEquals("limit error for plan 2 ", 100, limit2);
        assertEquals("limit error for plan 3 ", 100, limit3);
        assertEquals("limit error for plan 4 ", 100, limit4);
        assertEquals("limit error for plan 5 ", -1, limit5);
    }

    @Test
    public void calculateCurrentAvailableUsage() {
        int limit = policyHolderLimitService.calculateCurrentAvailableUsage("P001", "coverage-plan-1");
        int limit2 = policyHolderLimitService.calculateCurrentAvailableUsage("P001", "coverage-plan-2");
        int limit3 = policyHolderLimitService.calculateCurrentAvailableUsage("P001", "coverage-plan-3");
        int limit4 = policyHolderLimitService.calculateCurrentAvailableUsage("P001", "coverage-plan-4");
        int limit5 = policyHolderLimitService.calculateCurrentAvailableUsage("P001", "coverage-plan-5");
        System.out.println("the limit for plan-1 : " + limit);
        System.out.println("the limit for plan-2 : " + limit2);
        System.out.println("the limit for plan-3 : " + limit3);
        System.out.println("the limit for plan-4 : " + limit4);
        System.out.println("the limit for plan-5 : " + limit5);
        assertEquals("limit error for plan 1 ", 25, limit);
        assertEquals("limit error for plan 2 ", 100, limit2);
        assertEquals("limit error for plan 3 ", -1, limit3);
        assertEquals("limit error for plan 4 ", 100, limit4);
        assertEquals("limit error for plan 5 ", -1, limit5);
    }

    @Test
    public void findAvailableLimits() {
        Map<String, Integer> availableLimits = policyHolderLimitService.findAvailableLimits(
                Arrays.asList(new AttachedMedicalCoverage("coverage-plan-1"),
                        new AttachedMedicalCoverage("coverage-plan-6"),
                        new AttachedMedicalCoverage("coverage-plan-not-in-db"))
                , "PATIENT-001");
        System.out.println(availableLimits);
        assertEquals("Coverage plan number error", 3, availableLimits.size());
        availableLimits.forEach((key, value) -> {
            switch (key) {
                case "coverage-plan-1":
                    assertEquals("coverage-plan-1 available limit error", 25, value.intValue());
                    break;
                case "coverage-plan-6":
                    assertEquals("coverage-plan-2 available limit error", 100, value.intValue());
                    break;
                case "coverage-plan-not-in-db":
                    assertEquals("coverage-plan-not-in-db available limit error", -1, value.intValue());
                    break;
                default:
                    System.out.println("This can't happen");
                    Assert.fail();
            }
        });

    }
}