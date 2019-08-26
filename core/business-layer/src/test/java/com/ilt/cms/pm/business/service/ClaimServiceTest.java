package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.claim.ClaimViewCore;
import com.ilt.cms.core.entity.claim.ClaimsBalance;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.SellingPrice;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientPayable;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.pm.business.service.claim.DefaultClaimService;
import com.ilt.cms.repository.CustomMedicalCoverageRepository;
import com.ilt.cms.repository.spring.*;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.ilt.cms.repository.spring.system.SystemStoreRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
//import com.lippo.connector.mhcp.MHCPManager;
//import com.lippo.connector.mhcp.MHCPResult;
//import com.lippo.connector.mhcp.entity.response.chas.CHASAnnualBalanceResponseEntity;
//import com.lippo.connector.mhcp.entity.response.chas.CHASClaimStatusQueryResponseEntity;
//import com.lippo.connector.mhcp.entity.response.chas.CHASClaimSubmissionResponseEntity;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClaimServiceTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

//    private DiagnosisRepository diagnosisRepository;
//    private CaseRepository caseRepository;
//    private CustomMedicalCoverageRepository customMedicalCoverageRepository;
//    private ClinicRepository clinicRepository;
//    private PatientRepository patientRepository;
//    private ItemRepository itemRepository;
//    private SystemStoreRepository systemStoreRepository;
//    private ClinicDatabaseService clinicDatabaseService;
//    private MHCPManager mhcpManager;
//    private DoctorRepository doctorRepository;
//    private RunningNumberService runningNumberService;
//    private PatientVisitRegistryRepository visitRegistryRepository;
//    private CaseDatabaseService caseDatabaseService;
//    private MedicalCoverageRepository medicalCoverageRepository;
//    private DefaultClaimService claimService;
//    private MongoOperations mongoOperations;
//
//    @Before
//    public void setUp() throws Exception {
//        diagnosisRepository = Mockito.mock(DiagnosisRepository.class);
//        caseRepository = Mockito.mock(CaseRepository.class);
//        customMedicalCoverageRepository = mock(CustomMedicalCoverageRepository.class);
//        clinicRepository = mock(ClinicRepository.class);
//        patientRepository = mock(PatientRepository.class);
//        itemRepository = mock(ItemRepository.class);
//        systemStoreRepository = mock(SystemStoreRepository.class);
//        clinicDatabaseService = mock(ClinicDatabaseService.class);
//        mhcpManager = mock(MHCPManager.class);
//        doctorRepository = mock(DoctorRepository.class);
//        runningNumberService = mock(RunningNumberService.class);
//        visitRegistryRepository = mock(PatientVisitRegistryRepository.class);
//        caseDatabaseService = mock(CaseDatabaseService.class);
//        medicalCoverageRepository = mock(MedicalCoverageRepository.class);
//        mongoOperations = mock(MongoOperations.class);
//
//        claimService = new DefaultClaimService(diagnosisRepository, caseRepository, customMedicalCoverageRepository,
//                clinicRepository, patientRepository, itemRepository, systemStoreRepository, clinicDatabaseService,
//                mhcpManager, doctorRepository, runningNumberService, visitRegistryRepository, caseDatabaseService,
//                medicalCoverageRepository);
//    }
//
//    @Test
//    public void testPopulateClaimsPriceBreakDown() throws CMSException {
//
//        SalesOrder salesOrder = prepareClaimPopulationContext();
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        SalesOrder populatedSalesOrder = claimService.populateClaims(salesOrder);
//        System.out.println(populatedSalesOrder);
//        // check for the claims price breakdown
//        populatedSalesOrder.getInvoices().stream()
//                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
//                .forEach(invoice -> {
//                    if (invoice.getPlanId().equals("cov-pln-001")) {
//                        // assertions for plan 001
//                        Claim claim = invoice.getClaim();
//                        assertEquals("Laboratory amount was not properly added", 2000, claim.getMedicalTestAmt());
//                        assertEquals("Consultation amount was not properly added", 1000, claim.getConsultationAmt());
//                        System.out.println("Invoice for cov-pln-001 : passed");
//                    } else if (invoice.getPlanId().equals("cov-pln-002")) {
//                        // assertions for plan 002
//                        Claim claim = invoice.getClaim();
//                        assertEquals("Consultation amount was not properly added", 1500, claim.getConsultationAmt());
//                        assertEquals("Implant amount was not properly added", 1500, claim.getOtherAmt());
//                        System.out.println("Invoice for cov-pln-002 : passed");
//                    } else if (invoice.getPlanId().equals("cov-pln-003")) {
//                        // assertions for plan 003
//                        Claim claim = invoice.getClaim();
//                        assertEquals("Drug amount was not properly added", 3000, claim.getMedicationAmt());
//                        System.out.println("Invoice for cov-pln-003 : passed");
//                    }
//                });
//
//        // check for the original sales order's values
//        populatedSalesOrder.getPurchaseItems()
//                .forEach(salesItem -> {
//                    if (salesItem.getItemRefId().equals("ITEM-REF-001")) {
//                        SalesItem mockSalesItem = createMockSalesItem("ITEM-REF-001", Set.of("cov-pln-001", "cov-pln-002"), 3000, Item.ItemType.DRUG);
//                        assertEquals(mockSalesItem.toString().trim(), salesItem.toString().trim());
//                        System.out.println("Sales item [" + salesItem.getItemRefId() + "] was unchanged during the process");
//                    } else if (salesItem.getItemRefId().equals("ITEM-REF-002")) {
//                        SalesItem mockSalesItem = createMockSalesItem("ITEM-REF-002", Set.of("cov-pln-002"), 2000, Item.ItemType.LABORATORY);
//                        assertEquals(mockSalesItem.toString().trim(), salesItem.toString().trim());
//                        System.out.println("Sales item [" + salesItem.getItemRefId() + "] was unchanged during the process");
//                    } else if (salesItem.getItemRefId().equals("ITEM-REF-003")) {
//                        SalesItem mockSalesItem = createMockSalesItem("ITEM-REF-003", Set.of("cov-pln-003"), 2500, Item.ItemType.CONSULTATION);
//                        assertEquals(mockSalesItem.toString().trim(), salesItem.toString().trim());
//                        System.out.println("Sales item [" + salesItem.getItemRefId() + "] was unchanged during the process");
//                    } else {
//                        SalesItem mockSalesItem = createMockSalesItem("ITEM-REF-004", Set.of(), 2500, Item.ItemType.IMPLANTS);
//                        assertEquals(mockSalesItem.toString().trim(), salesItem.toString().trim());
//                        System.out.println("Sales item [" + salesItem.getItemRefId() + "] was unchanged during the process");
//                    }
//                });
//
//    }
//
//    @Test
//    public void testPopulateClaimsClaimObjectValues() throws CMSException {
//
//        SalesOrder salesOrder = prepareClaimPopulationContext();
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        SalesOrder populatedSalesOrder = claimService.populateClaims(salesOrder);
//        System.out.println(populatedSalesOrder);
//        // check for the claims price breakdown
//        populatedSalesOrder.getInvoices().stream()
//                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
//                .forEach(invoice -> {
//                    if (invoice.getPlanId().equals("cov-pln-001")) {
//                        // assertions for plan 001
//                        assertClaimFields(invoice);
//                        System.out.println("Invoice for cov-pln-001 : passed");
//                    } else if (invoice.getPlanId().equals("cov-pln-002")) {
//                        // assertions for plan 002
//                        assertClaimFields(invoice);
//                        System.out.println("Invoice for cov-pln-002 is passed");
//                    } else if (invoice.getPlanId().equals("cov-pln-003")) {
//                        // assertions for plan 003
//                        assertClaimFields(invoice);
//                        System.out.println("Invoice for cov-pln-003 is passed");
//                    }
//                });
//
//    }
//
//    @Test
//    public void submitClaim_success() throws Exception {
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.PENDING); // only PENDING,REJECTED,FAILED can be submitted
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-NRIC-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-NRIC-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setNric("DOC-NRIC-001");
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimSubmissionResponseEntity() {{
//                            this.setClaimNo("CLAIM-001");
//                            this.setClaimStatus(new ClaimStatus() {{
//                                this.setStatusCode("OK");
//                                this.setStatusDescription("status description");
//                            }});
//                        }})
//                );
//
//        ClaimViewCore claimViewCore = claimService.submitClaim("CLAIM-001", null);
//        Claim claim = claimViewCore.getClaim();
//        assertNotNull("Claim object was not set", claim);
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertEquals("Wrong claim status", Claim.ClaimStatus.SUBMITTED, claim.getClaimStatus());
//        System.out.println("Claim status was properly updated to SUBMITTED");
//
//        // check claim submission results
//        Claim.SubmissionResult submissionResult = claim.getSubmissionResult();
//        assertEquals("Submission result's claim no not properly set", "CLAIM-001", submissionResult.getClaimNo());
//        assertEquals("Submission result's status code not properly set", "OK", submissionResult.getStatusCode());
//        assertEquals("Submission result's description not properly set", "status description", submissionResult.getStatusDescription());
//        System.out.println("Claim submission result was properly updated [" + submissionResult + "]");
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void submitClaim_successWithUpdate() throws Exception {
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.PENDING); // only PENDING,REJECTED,FAILED can be submitted
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-NRIC-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-NRIC-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setNric("DOC-NRIC-001");
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-002"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimSubmissionResponseEntity() {{
//                            this.setClaimNo("CLAIM-001");
//                            this.setClaimStatus(new ClaimStatus() {{
//                                this.setStatusCode("OK");
//                                this.setStatusDescription("status description");
//                            }});
//                        }})
//                );
//
//        ClaimViewCore claimRequest = new ClaimViewCore() {{
//            this.setClaimDoctorId("CLAIM-DOC-002");
//            this.setPayersName("PAYER-NAME-UPDATE-1");
//            this.setPayersNric("PAYER-NRIC-UPDATE-1");
//            this.setDiagnosisCodes(Arrays.asList("DIAG-UPDATE-00.1", "DIAG-UPDATE-00.2"));
//            this.setClaimRemarks("CLAIM-REMARK");
//            // following should not get updated
//            this.setExpectedClaimAmount(1500);
//            this.setClaimStatus(Claim.ClaimStatus.PAID);
//            this.setClaimedAmount(2000);
//            this.setClaimRefNo("FALSE REF NO");
//
//        }};
//        ClaimViewCore claimViewCore = claimService.submitClaim("CLAIM-001", claimRequest);
//        Claim claim = claimViewCore.getClaim();
//        assertNotNull("Claim object was not set", claim);
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertEquals("Wrong claim status", Claim.ClaimStatus.SUBMITTED, claim.getClaimStatus());
//        System.out.println("Claim status was properly updated to SUBMITTED");
//
//        // check claim submission results
//        Claim.SubmissionResult submissionResult = claim.getSubmissionResult();
//        assertEquals("Submission result's claim no not properly set", "CLAIM-001", submissionResult.getClaimNo());
//        assertEquals("Submission result's status code not properly set", "OK", submissionResult.getStatusCode());
//        assertEquals("Submission result's description not properly set", "status description", submissionResult.getStatusDescription());
//        System.out.println("Claim submission result was properly updated [" + submissionResult + "]");
//
//        // check for updated values
//        assertNotEquals("Claim status was updated to PAID", Claim.ClaimStatus.PAID, claim.getClaimStatus());
//        assertNotEquals("Claimed amount was updated to 2000", 2000, claim.getClaimedAmount());
//        assertNotEquals("Claim expected amount was updated to 1500", 1500, claim.getClaimExpectedAmt());
//        assertEquals("Claim ref was not updated", "FALSE REF NO", claim.getClaimRefNo());
//        assertEquals("Claim remark was not updated", "CLAIM-REMARK", claim.getRemark());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void submitClaim_reject() throws Exception {
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.PENDING); // only PENDING,REJECTED,FAILED can be submitted
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-NRIC-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-NRIC-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setNric("DOC-NRIC-001");
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimSubmissionResponseEntity() {{
//                            this.setClaimNo("CLAIM-001");
//                            this.setClaimStatus(new ClaimStatus() {{
//                                this.setStatusCode("REJECT"); // anything other than OK
//                                this.setStatusDescription("status description");
//                            }});
//                        }})
//                );
//
//        ClaimViewCore claimViewCore = claimService.submitClaim("CLAIM-001", null);
//        Claim claim = claimViewCore.getClaim();
//        assertNotNull("Claim object was not set", claim);
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertEquals("Wrong claim status", Claim.ClaimStatus.REJECTED, claim.getClaimStatus());
//        System.out.println("Claim status was properly updated to REJECTED");
//
//        // check claim submission results
//        Claim.SubmissionResult submissionResult = claim.getSubmissionResult();
//        assertEquals("Submission result's claim no not properly set", "CLAIM-001", submissionResult.getClaimNo());
//        assertNotEquals("Submission result's status code not properly set", "OK", submissionResult.getStatusCode());
//        assertEquals("Submission result's description not properly set", "status description", submissionResult.getStatusDescription());
//        System.out.println("Claim submission result was properly updated [" + submissionResult + "]");
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void submitClaim_mhcpInvalidRequest() throws Exception {
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.PENDING); // only PENDING,REJECTED,FAILED can be submitted
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-NRIC-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-NRIC-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setNric("DOC-NRIC-001");
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.E6004,
//                        new CHASClaimSubmissionResponseEntity() {{
//                            this.setClaimNo("CLAIM-001");
//                            this.setClaimStatus(new ClaimStatus() {{
//                                this.setStatusCode("INVALID REQUEST"); // anything other than OK
//                                this.setStatusDescription("status description");
//                            }});
//                        }})
//                );
//
//        ClaimViewCore claimViewCore = claimService.submitClaim("CLAIM-001", null);
//        Claim claim = claimViewCore.getClaim();
//        assertNotNull("Claim object was not set", claim);
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertEquals("Wrong claim status", Claim.ClaimStatus.FAILED, claim.getClaimStatus());
//        System.out.println("Claim status was properly updated to FAILED");
//
//        // check claim submission results
//        Claim.SubmissionResult submissionResult = claim.getSubmissionResult();
//        assertEquals("Submission result's claim no not properly set", "CLAIM-001", submissionResult.getClaimNo());
//        assertEquals("Submission result's status code not properly set", "INVALID REQUEST", submissionResult.getStatusCode());
//        assertEquals("Submission result's description not properly set", "status description", submissionResult.getStatusDescription());
//        System.out.println("Claim submission result was properly updated [" + submissionResult + "]");
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void submitClaim_soapError() throws Exception {
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.PENDING); // only PENDING,REJECTED,FAILED can be submitted
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-NRIC-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-NRIC-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setNric("DOC-NRIC-001");
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.E6001,
//                        null)
//                );
//
//        ClaimViewCore claimViewCore = claimService.submitClaim("CLAIM-001", null);
//        Claim claim = claimViewCore.getClaim();
//        assertNotNull("Claim object was not set", claim);
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertEquals("Wrong claim status", Claim.ClaimStatus.FAILED, claim.getClaimStatus());
//        System.out.println("Claim status was properly updated to FAILED");
//
//        // check claim submission results
//        Claim.SubmissionResult submissionResult = claim.getSubmissionResult();
//        assertEquals("Submission result's claim no not properly set", "-1", submissionResult.getClaimNo());
//        assertEquals("Submission result's status code not properly set", "E6001", submissionResult.getStatusCode());
//        assertEquals("Submission result's description not properly set", "SOAP Connection Failure", submissionResult.getStatusDescription());
//        System.out.println("Claim submission result was properly updated [" + submissionResult + "]");
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void submitClaim_InvalidCase() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("No Case found with the given claim id");
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(null);
//
//        claimService.submitClaim("CLAIM-001", null);
//    }
//
//    @Test
//    public void submitClaim_InvalidClinic() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("No clinic found with given clinicId");
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.empty());
//
//        claimService.submitClaim("CLAIM-001", null);
//    }
//
//
//    @Test
//    public void saveClaim_successMHCP() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimSaveTest(Claim.ClaimStatus.PENDING, false);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        ClaimViewCore claimRequest = new ClaimViewCore() {{
//            this.setClaimDoctorId("CLAIM-DOC-002");
//            this.setPayersName("PAYER-NAME-UPDATE-1");
//            this.setPayersNric("PAYER-NRIC-UPDATE-1");
//            this.setDiagnosisCodes(Arrays.asList("DIAG-UPDATE-00.1", "DIAG-UPDATE-00.2"));
//            this.setExpectedClaimAmount(1500);
//
//            this.setClaimStatus(Claim.ClaimStatus.PAID);
//            this.setClaimedAmount(2000);
//            this.setClaimRefNo("REF NO");
//            this.setClaimRemarks("CLAIM-REMARK");
//
//        }};
//
//        ClaimViewCore claimViewCore = claimService.saveClaim("CLAIM-001", claimRequest);
//        Claim claim = claimViewCore.getClaim();
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertNotNull("Claim object was not set", claim);
//
//        // check for updated values
//        assertEquals("Claim expected amount was not updated", 1500, claim.getClaimExpectedAmt());
//        assertEquals("Claim doctor id was not updated", "CLAIM-DOC-002", claim.getClaimDoctorId());
//        assertEquals("Claim's payer name was not updated", "PAYER-NAME-UPDATE-1", claim.getPayersName());
//        assertEquals("Claim's payer nric was not updated", "PAYER-NRIC-UPDATE-1", claim.getPayersNric());
//        assertEquals("Claim's diagnosis list was not updated", Arrays.asList("DIAG-UPDATE-001", "DIAG-UPDATE-002"), claim.getDiagnosisCodes());
//
//        assertEquals("Claim status was updated to PAID", Claim.ClaimStatus.PAID, claim.getClaimStatus());
//        assertEquals("Claimed amount was not updated to 2000", 2000, claim.getClaimedAmount());
//        assertEquals("Claim ref was not updated", "REF NO", claim.getClaimRefNo());
//        assertEquals("Claim remark was not updated", "CLAIM-REMARK", claim.getRemark());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void submitClaim_claimStatusNotAllowedForUpdateMHCP_REJECTED_PERMANENT() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("Only [PENDING,REJECTED,FAILED] states can be claimed");
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.REJECTED_PERMANENT); // only PENDING,REJECTED,FAILED can be submitted
//            this.setManuallyUpdated(false);
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        claimService.submitClaim("CLAIM-001", null);
//    }
//
//    @Test
//    public void submitClaim_claimStatusNotAllowedForUpdateMHCP_SUBMITTED() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("Only [PENDING,REJECTED,FAILED] states can be claimed");
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.SUBMITTED); // only PENDING,REJECTED,FAILED can be submitted
//            this.setManuallyUpdated(false);
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        claimService.submitClaim("CLAIM-001", null);
//    }
//
//    @Test
//    public void submitClaim_claimStatusNotAllowedForUpdateMHCP_PAID() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("Only [PENDING,REJECTED,FAILED] states can be claimed");
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.PAID); // only PENDING,REJECTED,FAILED can be submitted
//            this.setManuallyUpdated(false);
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        claimService.submitClaim("CLAIM-001", null);
//    }
//
//    @Test
//    public void saveClaim_claimStatusAllowedForUpdateManual_SUBMITTED() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimSaveTest(Claim.ClaimStatus.SUBMITTED, true);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        ClaimViewCore claimRequest = new ClaimViewCore() {{
//            this.setClaimDoctorId("CLAIM-DOC-002");
//            this.setPayersName("PAYER-NAME-UPDATE-1");
//            this.setPayersNric("PAYER-NRIC-UPDATE-1");
//            this.setDiagnosisCodes(Arrays.asList("DIAG-UPDATE-00.1", "DIAG-UPDATE-00.2"));
//            this.setExpectedClaimAmount(1500);
//
//            this.setClaimStatus(Claim.ClaimStatus.PAID);
//            this.setClaimedAmount(2000);
//            this.setClaimRefNo("REF NO");
//            this.setClaimRemarks("CLAIM-REMARK");
//
//        }};
//
//        ClaimViewCore claimViewCore = claimService.saveClaim("CLAIM-001", claimRequest);
//        Claim claim = claimViewCore.getClaim();
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertNotNull("Claim object was not set", claim);
//
//        // check for updated values
//        assertEquals("Claim expected amount was not updated", 1500, claim.getClaimExpectedAmt());
//        assertEquals("Claim doctor id was not updated", "CLAIM-DOC-002", claim.getClaimDoctorId());
//        assertEquals("Claim's payer name was not updated", "PAYER-NAME-UPDATE-1", claim.getPayersName());
//        assertEquals("Claim's payer nric was not updated", "PAYER-NRIC-UPDATE-1", claim.getPayersNric());
//        assertEquals("Claim's diagnosis list was not updated", Arrays.asList("DIAG-UPDATE-001", "DIAG-UPDATE-002"), claim.getDiagnosisCodes());
//
//        assertEquals("Claim status was updated to PAID", Claim.ClaimStatus.PAID, claim.getClaimStatus());
//        assertEquals("Claimed amount was not updated to 2000", 2000, claim.getClaimedAmount());
//        assertEquals("Claim ref was not updated", "REF NO", claim.getClaimRefNo());
//        assertEquals("Claim remark was not updated", "CLAIM-REMARK", claim.getRemark());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void saveClaim_claimStatusNotAllowedForUpdateManual_REJECTED_PERMANENT() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("Only [PENDING,REJECTED,FAILED] states can be claimed");
//
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.REJECTED_PERMANENT);
//            this.setManuallyUpdated(true);
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//        }};
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(mockCase);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        claimService.submitClaim("CLAIM-001", null);
//    }
//
//    @Test
//    public void saveClaim_successStatusUpdate() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimSaveTest(Claim.ClaimStatus.PENDING, true);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        ClaimViewCore claimRequest = new ClaimViewCore() {{
//            this.setClaimDoctorId("CLAIM-DOC-002");
//            this.setPayersName("PAYER-NAME-UPDATE-1");
//            this.setPayersNric("PAYER-NRIC-UPDATE-1");
//            this.setDiagnosisCodes(Arrays.asList("DIAG-UPDATE-00.1", "DIAG-UPDATE-00.2"));
//            this.setClaimRemarks("CLAIM-REMARK");
//            this.setExpectedClaimAmount(1500);
//            this.setClaimStatus(Claim.ClaimStatus.PAID);
//            this.setClaimedAmount(2000);
//            this.setClaimRefNo("FALSE REF NO");
//
//        }};
//
//        ClaimViewCore claimViewCore = claimService.saveClaim("CLAIM-001", claimRequest);
//        Claim claim = claimViewCore.getClaim();
//
//        assertNotNull("Claim was not submitted properly", claimViewCore);
//        assertNotNull("Claim object was not set", claim);
//
//        // check for updated values
//        assertEquals("Claim expected amount was not updated", 1500, claim.getClaimExpectedAmt());
//        assertEquals("Claim doctor id was not updated", "CLAIM-DOC-002", claim.getClaimDoctorId());
//        assertEquals("Claim's payer name was not updated", "PAYER-NAME-UPDATE-1", claim.getPayersName());
//        assertEquals("Claim's payer nric was not updated", "PAYER-NRIC-UPDATE-1", claim.getPayersNric());
//        assertEquals("Claim's diagnosis list was not updated", Arrays.asList("DIAG-UPDATE-001", "DIAG-UPDATE-002"), claim.getDiagnosisCodes());
//        assertEquals("Claim remark was not updated", "CLAIM-REMARK", claim.getRemark());
//        assertEquals("Claim status was not updated to PAID", Claim.ClaimStatus.PAID, claim.getClaimStatus());
//        assertEquals("Claimed amount was not updated to 2000", 2000, claim.getClaimedAmount());
//        assertEquals("Claim ref was not updated", "FALSE REF NO", claim.getClaimRefNo());
//
//        // check invoice paid amount
//        assertEquals("Invoice value was not updated", 2000, mockInvoice.getPaidAmount());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void saveClaim_claimStatusCantBeUpdated() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("Only [PENDING,REJECTED,FAILED] states can be updated");
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(new Case() {{
//                    this.setSalesOrder(new SalesOrder() {{
//                        this.setInvoices(Arrays.asList(new Invoice() {{
//                            this.setInvoiceType(InvoiceType.CREDIT);
//                            this.setClaim(new Claim() {{
//                                this.setClaimId("CLAIM-001");
//                                this.setClaimStatus(ClaimStatus.REJECTED_PERMANENT);
//                            }});
//                        }}));
//                    }});
//                }});
//
//        claimService.saveClaim("CLAIM-001", new ClaimViewCore());
//    }
//
//
//    @Test
//    public void saveClaim_noCaseForClaim() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("No Case found with the given claim id");
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenReturn(null);
//
//        ClaimViewCore claimRequest = new ClaimViewCore();
//
//        claimService.saveClaim("CLAIM-001", claimRequest);
//    }
//
//    private Invoice prepareCaseContextForClaimSaveTest(Claim.ClaimStatus claimStatus, boolean isManuallyUpdated) {
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(claimStatus);
//            this.setManuallyUpdated(isManuallyUpdated);
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId("CLAIM-001"))
//                .thenReturn(mockCase);
//        return mockInvoice;
//    }
//
//    @Test
//    public void rejectClaim_success() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimSaveTest(Claim.ClaimStatus.PENDING, false);
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setHeCode("CINIC-HE-001");
//            this.setDomain("clinic001.domain");
//            this.setUen("UEN-001");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        ClaimViewCore claimViewCore = claimService.rejectClaim("CLAIM-001", Claim.ClaimStatus.REJECTED_PERMANENT);
//        Claim claim = claimViewCore.getClaim();
//
//        validateClaimRejectSuccessResult(mockInvoice, mockPatient, claimViewCore, claim, new Claim() {{
//            this.setClaimResult(new Claim.ClaimResult() {{
//                this.setRemark("Not submitted");
//            }});
//        }});
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//
//        // when the claim was submitted once
//        Claim submittedClaim = mockInvoice.getClaim();
//        submittedClaim.setSubmissionDateTime(LocalDateTime.now());
//        submittedClaim.setClaimResult(new Claim.ClaimResult() {{
//            this.setRemark("Rejected due to late submission");
//        }});
//        submittedClaim.setClaimStatus(Claim.ClaimStatus.REJECTED);
//
//        mockPatient.setPatientPayables(null);
//
//        claimViewCore = claimService.rejectClaim("CLAIM-001", Claim.ClaimStatus.REJECTED_PERMANENT);
//        claim = claimViewCore.getClaim();
//
//        validateClaimRejectSuccessResult(mockInvoice, mockPatient, claimViewCore, claim, submittedClaim);
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//
//        // when the claim was submitted once
//        submittedClaim = mockInvoice.getClaim();
//        submittedClaim.setSubmissionDateTime(LocalDateTime.now());
//        submittedClaim.setClaimResult(new Claim.ClaimResult() {{
//            this.setRemark("Failed to submit due to soap error");
//        }});
//        submittedClaim.setClaimStatus(Claim.ClaimStatus.FAILED);
//
//        mockPatient.setPatientPayables(null);
//
//        claimViewCore = claimService.rejectClaim("CLAIM-001", Claim.ClaimStatus.REJECTED_PERMANENT);
//        claim = claimViewCore.getClaim();
//
//        validateClaimRejectSuccessResult(mockInvoice, mockPatient, claimViewCore, claim, submittedClaim);
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    private void validateClaimRejectSuccessResult(Invoice mockInvoice, Patient mockPatient, ClaimViewCore claimViewCore, Claim claim, Claim submittedClaim) {
//        PatientPayable patientPayable;
//        assertNotNull("Claim was not rejected properly", claimViewCore);
//        assertNotNull("Claim object was not set", claim);
//
//        // check for updated values
//        assertEquals("Claim status was updated", Claim.ClaimStatus.REJECTED_PERMANENT, claim.getClaimStatus());
//        assertNotNull("Patient payable was not set properly", mockPatient.getPatientPayables());
//        assertEquals("Patient payable should have only 1 item", 1, mockPatient.getPatientPayables().size());
//        patientPayable = mockPatient.getPatientPayables().get(0);
//        assertEquals("Patient payable invoice Id wrong", mockInvoice.getInvoiceNumber(), patientPayable.getInvoiceId());
//        assertEquals("Patient payable invoice number wrong", mockInvoice.getInvoiceNumber(), patientPayable.getBillNumber());
//        assertEquals("Patient payable amount wrong", mockInvoice.getPayableAmount(), patientPayable.getAmount());
//        assertEquals("Patient payable bill date wrong", mockInvoice.getInvoiceTime(), patientPayable.getBillDateTime());
//        assertEquals("Patient payable rejection reason wrong", submittedClaim.getClaimResult().getRemark(), patientPayable.getRejectionReason());
//        assertFalse("Patient payable paid by user value wrong", patientPayable.isPaidByUser());
//    }
//
//
//    @Test
//    public void rejectClaim_invalidClinic() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("No clinic found with given clinicId");
//
//        prepareCaseContextForClaimSaveTest(Claim.ClaimStatus.PENDING, false);
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.empty());
//
//        claimService.rejectClaim("CLAIM-001", Claim.ClaimStatus.REJECTED_PERMANENT);
//    }
//
//
//    @Test
//    public void rejectClaim_invalidClaimStatus() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("Only [REJECTED,PENDING,FAILED] states can be rejected permanently");
//
//        prepareCaseContextForClaimSaveTest(Claim.ClaimStatus.PAID, false);
//
//        Clinic mockClinic = new Clinic();
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        claimService.rejectClaim("CLAIM-001", Claim.ClaimStatus.REJECTED_PERMANENT);
//    }
//
//
//    @Test
//    public void checkClaimStatus_successAPPROVED() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimCheckStatusTest();
//
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setUen("UEN-001");
//            this.setHeCode("CLINIC-HE-001");
//            this.setDomain("clinic001.domain");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001");
//                                            this.setStatus("A");
//                                            this.setPaidDate(null);
//                                            this.setPaidAmount(null);
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                );
//
//        List<ClaimViewCore> claimStatusList = claimService.checkClaimStatus("CLINIC-001", Arrays.asList("CLAIM-001"));
//
//        assertEquals("Status list's size should be 1", 1, claimStatusList.size());
//        ClaimViewCore claimViewCore = claimStatusList.get(0);
//        Claim claim = claimViewCore.getClaim();
//        assertEquals("Status was not updated properly", Claim.ClaimStatus.APPROVED, claim.getClaimStatus());
//
//        // check claim result
//        Claim.ClaimResult claimResult = claim.getClaimResult();
//        assertNotNull("Claim result can't be null", claimResult);
//        assertEquals("Claim no is wrong", "CLAIM-SUBMISSION-NO-001", claimResult.getReferenceNumber());
//        assertNull("Paid date should be null", claimResult.getResultDateTime());
//        assertEquals("Paid amount should be 0", 0, claimResult.getAmount());
//        assertEquals("Status code was not properly set", "ERROR-CODE", claimResult.getStatusCode());
//        assertEquals("Remark was not properly set", "STATUS-DESCRIPTION", claimResult.getRemark());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void checkClaimStatus_successREJECTED() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimCheckStatusTest();
//
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setUen("UEN-001");
//            this.setHeCode("CLINIC-HE-001");
//            this.setDomain("clinic001.domain");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001");
//                                            this.setStatus("R");
//                                            this.setPaidDate(null);
//                                            this.setPaidAmount(null);
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                );
//
//        List<ClaimViewCore> claimStatusList = claimService.checkClaimStatus("CLINIC-001", Arrays.asList("CLAIM-001"));
//
//        assertEquals("Status list's size should be 1", 1, claimStatusList.size());
//        ClaimViewCore claimViewCore = claimStatusList.get(0);
//        Claim claim = claimViewCore.getClaim();
//        assertEquals("Status was not updated properly", Claim.ClaimStatus.REJECTED, claim.getClaimStatus());
//
//        // check claim result
//        Claim.ClaimResult claimResult = claim.getClaimResult();
//        assertNotNull("Claim result can't be null", claimResult);
//        assertEquals("Claim no is wrong", "CLAIM-SUBMISSION-NO-001", claimResult.getReferenceNumber());
//        assertNull("Paid date should be null", claimResult.getResultDateTime());
//        assertEquals("Paid amount should be 0", 0, claimResult.getAmount());
//        assertEquals("Status code was not properly set", "ERROR-CODE", claimResult.getStatusCode());
//        assertEquals("Remark was not properly set", "STATUS-DESCRIPTION", claimResult.getRemark());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void checkClaimStatus_successAPPEALED() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimCheckStatusTest();
//
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setUen("UEN-001");
//            this.setHeCode("CLINIC-HE-001");
//            this.setDomain("clinic001.domain");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001");
//                                            this.setStatus("AE");
//                                            this.setPaidDate(null);
//                                            this.setPaidAmount(null);
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                );
//
//        List<ClaimViewCore> claimStatusList = claimService.checkClaimStatus("CLINIC-001", Arrays.asList("CLAIM-001"));
//
//        assertEquals("Status list's size should be 1", 1, claimStatusList.size());
//        ClaimViewCore claimViewCore = claimStatusList.get(0);
//        Claim claim = claimViewCore.getClaim();
//        assertEquals("Status was not updated properly", Claim.ClaimStatus.APPEALED, claim.getClaimStatus());
//
//        // check claim result
//        Claim.ClaimResult claimResult = claim.getClaimResult();
//        assertNotNull("Claim result can't be null", claimResult);
//        assertEquals("Claim no is wrong", "CLAIM-SUBMISSION-NO-001", claimResult.getReferenceNumber());
//        assertNull("Paid date should be null", claimResult.getResultDateTime());
//        assertEquals("Paid amount should be 0", 0, claimResult.getAmount());
//        assertEquals("Status code was not properly set", "ERROR-CODE", claimResult.getStatusCode());
//        assertEquals("Remark was not properly set", "STATUS-DESCRIPTION", claimResult.getRemark());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//    @Test
//    public void checkClaimStatus_successOTHER() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimCheckStatusTest();
//
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setUen("UEN-001");
//            this.setHeCode("CLINIC-HE-001");
//            this.setDomain("clinic001.domain");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001");
//                                            this.setStatus("OTHER");
//                                            this.setPaidDate(null);
//                                            this.setPaidAmount(null);
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                );
//
//        List<ClaimViewCore> claimStatusList = claimService.checkClaimStatus("CLINIC-001", Arrays.asList("CLAIM-001"));
//
//        assertEquals("Status list's size should be 1", 1, claimStatusList.size());
//        ClaimViewCore claimViewCore = claimStatusList.get(0);
//        Claim claim = claimViewCore.getClaim();
//        assertEquals("Status was updated ", Claim.ClaimStatus.SUBMITTED, claim.getClaimStatus());
//
//        // check claim result
//        Claim.ClaimResult claimResult = claim.getClaimResult();
//        assertNull("Claim result should be null", claimResult);
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//
//    @Test
//    public void checkClaimStatus_successPAID() throws Exception {
//
//        Invoice mockInvoice = prepareCaseContextForClaimCheckStatusTest();
//
//
//        Clinic mockClinic = new Clinic() {{
//            this.setId("CLINIC-001");
//            this.setUen("UEN-001");
//            this.setHeCode("CLINIC-HE-001");
//            this.setDomain("clinic001.domain");
//        }};
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.of(mockClinic));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        Instant mockDateInstance = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{ // record for paid with paid amount
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setStatus("P");
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001");
//                                            this.setPaidAmount(BigDecimal.valueOf(10.0));
//                                            this.setPaidDate(
//                                                    DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar() {{
//                                                        this.setTime(Date.from(mockDateInstance));
//                                                    }}));
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                );
//
//        List<ClaimViewCore> claimStatusList = claimService.checkClaimStatus("CLINIC-001", Arrays.asList("CLAIM-001"));
//
//        assertEquals("Status list's size should be 1", 1, claimStatusList.size());
//        ClaimViewCore claimViewCore = claimStatusList.get(0);
//        Claim claim = claimViewCore.getClaim();
//        assertEquals("Status was not updated properly", Claim.ClaimStatus.PAID, claim.getClaimStatus());
//
//        // check claim result
//        Claim.ClaimResult claimResult = claim.getClaimResult();
//        assertNotNull("Claim result can't be null", claimResult);
//        assertEquals("Claim no is wrong", "CLAIM-SUBMISSION-NO-001", claimResult.getReferenceNumber());
//        assertEquals("Paid date is wrong", LocalDateTime.ofInstant(mockDateInstance, ZoneId.systemDefault()), claimResult.getResultDateTime());
//        assertEquals("Paid amount should be 1000", 1000, claimResult.getAmount());
//        assertEquals("Status code was not properly set", "ERROR-CODE", claimResult.getStatusCode());
//        assertEquals("Remark was not properly set", "STATUS-DESCRIPTION", claimResult.getRemark());
//
//        // check invoice
//        assertEquals("Invoice paid amount is wrong", 1000, mockInvoice.getPaidAmount());
//        assertEquals("Invoice state is wrong", Invoice.InvoiceState.PAID, mockInvoice.getInvoiceState());
//
//        // check claim view fields
//        checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//    }
//
//
//    @Test
//    public void checkClaimStatus_invalidClinic() throws Exception {
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("No clinic found with given clinicId");
//
//
//        when(clinicRepository.findById("CLINIC-001"))
//                .thenReturn(Optional.empty());
//
//        claimService.checkClaimStatus("CLINIC-001", Arrays.asList("CLAIM-001"));
//    }
//
//    private Invoice prepareCaseContextForClaimCheckStatusTest() {
//        Claim mockClaim = new Claim() {{
//            this.setClaimId("CLAIM-001");
//            this.setClaimStatus(ClaimStatus.SUBMITTED);
//            this.setManuallyUpdated(false);
//            this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//            this.setClaimDoctorId("CLAIM-DOC-001");
//            this.setAttendingDoctorId("ATTENDING-DOC-001");
//            this.setClaimExpectedAmt(1000);
//            this.setConsultationAmt(450);
//            this.setMedicationAmt(400);
//            this.setMedicalTestAmt(50);
//            this.setOtherAmt(100);
//            this.setGstAmount(70);
//            this.setPayersName("PAYER-NAME");
//            this.setPayersNric("PAYER-NRIC-001");
//            this.setClaimRefNo("CLAIM-REF-001");
//            this.setRemark("CLAIM-REMARK");
//            //set submission details
//            this.setSubmissionResult(new SubmissionResult() {{
//                this.setClaimNo("CLAIM-SUBMISSION-NO-001");
//            }});
//        }};
//
//        Invoice mockInvoice = new Invoice() {{
//            this.setInvoiceNumber("INVOICE-001");
//            this.setPlanId("coverage-plan-01");
//            this.setInvoiceType(InvoiceType.CREDIT);
//            this.setClaim(mockClaim);
//            this.setInvoiceTime(LocalDateTime.now());
//            this.setPayableAmount(1000);
//        }};
//
//        SalesOrder mockSalesOrder = new SalesOrder() {{
//            this.setInvoices(Arrays.asList(mockInvoice));
//        }};
//
//        Case mockCase = new Case() {{
//            this.setClinicId("CLINIC-001");
//            this.setSalesOrder(mockSalesOrder);
//            this.setPatientId("PATIENT-001");
//        }};
//
//        when(caseRepository.findCaseByClaimId("CLAIM-001"))
//                .thenReturn(mockCase);
//
//        when(caseRepository.findCaseByClaimNo("CLAIM-SUBMISSION-NO-001"))
//                .thenReturn(mockCase);
//
//        return mockInvoice;
//    }
//
//    @Test
//    public void checkClaimStatusForClinics_success() throws Exception {
//
//        List<String> clinicIdList = Arrays.asList("CLINIC-001", "CLINIC-002");
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                    }});
//                });
//
//        Map<String, Case> caseMap = prepareCaseContextForClaimCheckStatusForClinicTest(clinicIdList);
//
//        Doctor mockDoctor = new Doctor() {{
//            this.setMcr("DOC-MCR-001");
//        }};
//
//        when(doctorRepository.findById("CLAIM-DOC-001"))
//                .thenReturn(Optional.of(mockDoctor));
//
//        Instant mockDateInstance = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{ // record for paid with paid amount
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setStatus("P");
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001-CLINIC-001");
//                                            this.setPaidAmount(BigDecimal.valueOf(10.0));
//                                            this.setPaidDate(
//                                                    DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar() {{
//                                                        this.setTime(Date.from(mockDateInstance));
//                                                    }}));
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                )
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASClaimStatusQueryResponseEntity() {{
//                            this.record = Arrays.asList(
//                                    new Record() {{ // record for paid with paid amount
//                                        this.setRecordStatus(new RecordStatus() {{
//                                            this.setStatus("P");
//                                            this.setRecordID("CLAIM-SUBMISSION-NO-001-CLINIC-002");
//                                            this.setPaidAmount(BigDecimal.valueOf(10.0));
//                                            this.setPaidDate(
//                                                    DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar() {{
//                                                        this.setTime(Date.from(mockDateInstance));
//                                                    }}));
//                                            this.setErrorCode("ERROR-CODE");
//                                            this.setStatusDescription("STATUS-DESCRIPTION");
//                                        }});
//                                    }}
//                            );
//                        }})
//                );
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//
//        List<ClaimViewCore> claimStatusResults = claimService.checkClaimStatusForClinics(clinicIdList);
//
//        assertNotNull("Status results not fetched properly", claimStatusResults);
//        assertEquals("There should be 2 claim results", 2, claimStatusResults.size());
//        claimStatusResults.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of clinic [" + claimViewCore.getClinicId() + "]");
//
//            Case mockCase = caseMap.get(claimViewCore.getClinicId());
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//            assertEquals("Status was not updated properly", Claim.ClaimStatus.PAID, claim.getClaimStatus());
//
//            // check claim result
//            Claim.ClaimResult claimResult = claim.getClaimResult();
//            assertNotNull("Claim result can't be null", claimResult);
//            assertEquals("Claim no is wrong", "CLAIM-SUBMISSION-NO-001-" + claimViewCore.getClinicId(), claimResult.getReferenceNumber());
//            assertEquals("Paid date is wrong", LocalDateTime.ofInstant(mockDateInstance, ZoneId.systemDefault()), claimResult.getResultDateTime());
//            assertEquals("Paid amount should be 1000", 1000, claimResult.getAmount());
//            assertEquals("Status code was not properly set", "ERROR-CODE", claimResult.getStatusCode());
//            assertEquals("Remark was not properly set", "STATUS-DESCRIPTION", claimResult.getRemark());
//
//            // check invoice
//            assertEquals("Invoice paid amount is wrong", 1000, mockInvoice.getPaidAmount());
//            assertEquals("Invoice state is wrong", Invoice.InvoiceState.PAID, mockInvoice.getInvoiceState());
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of clinic [" + claimViewCore.getClinicId() + "] passed verification");
//        });
//
//
//    }
//
//    @Test
//    public void checkClaimStatusForClinics_invalidClinic() throws Exception {
//        exceptionRule.expect(RuntimeException.class);
//        exceptionRule.expectMessage("No clinic found with given clinicId");
//
//        List<String> clinicIdList = Arrays.asList("CLINIC-001", "CLINIC-002");
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> Optional.empty());
//
//        claimService.checkClaimStatusForClinics(clinicIdList);
//
//    }
//
//    private Map<String, Case> prepareCaseContextForClaimCheckStatusForClinicTest(List<String> clinicIdList) {
//
//        Map<String, Case> mockCasesMap = clinicIdList.stream()
//                .collect(Collectors.toMap(clinicId -> clinicId, clinicId -> new Case() {{
//                    this.setClinicId(clinicId);
//                    this.setSalesOrder(new SalesOrder() {{
//                        this.setInvoices(Arrays.asList(
//                                new Invoice() {{
//                                    this.setInvoiceNumber("INVOICE-001-" + clinicId);
//                                    this.setPlanId("coverage-plan-01");
//                                    this.setInvoiceType(InvoiceType.CREDIT);
//                                    this.setInvoiceTime(LocalDateTime.now());
//                                    this.setPayableAmount(1000);
//                                    this.setClaim(new Claim() {{
//                                        this.setClaimId("CLAIM-001-" + clinicId);
//                                        this.setClaimStatus(ClaimStatus.SUBMITTED);
//                                        this.setManuallyUpdated(false);
//                                        this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//                                        this.setClaimDoctorId("CLAIM-DOC-001");
//                                        this.setAttendingDoctorId("ATTENDING-DOC-001");
//                                        this.setClaimExpectedAmt(1000);
//                                        this.setConsultationAmt(450);
//                                        this.setMedicationAmt(400);
//                                        this.setMedicalTestAmt(50);
//                                        this.setOtherAmt(100);
//                                        this.setGstAmount(70);
//                                        this.setPayersName("PAYER-NAME");
//                                        this.setPayersNric("PAYER-NRIC-001");
//                                        this.setClaimRefNo("CLAIM-REF-001");
//                                        this.setRemark("CLAIM-REMARK");
//                                        //set submission details
//                                        this.setSubmissionResult(new SubmissionResult() {{
//                                            this.setClaimNo("CLAIM-SUBMISSION-NO-001-" + clinicId);
//                                        }});
//
//                                    }});
//                                }}
//                        ));
//                    }});
//                    this.setPatientId("PATIENT-001");
//                }}));
//
//        when(caseRepository.findByClinicId(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Arrays.asList(mockCasesMap.get(invocationId));
//                });
//
//        when(caseRepository.findCaseByClaimNo(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return mockCasesMap.values().stream()
//                            .filter(aCase -> invocationId.equals(aCase.getSalesOrder().getInvoices().get(0).getClaim().getSubmissionResult().getClaimNo()))
//                            .findFirst().get();
//                });
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return mockCasesMap.values().stream()
//                            .filter(aCase -> invocationId.equals(aCase.getSalesOrder().getInvoices().get(0).getClaim().getClaimId()))
//                            .findFirst().get();
//                });
//
//        return mockCasesMap;
//    }
//
//    @Test
//    public void listClaimsByTypeForClinicList_success() throws Exception {
//
//        List<String> clinicIdList = Arrays.asList("CLINIC-001@COVERAGE-PLAN-ID-001", "CLINIC-002@COVERAGE-PLAN-ID-002");
//
//        Map<String, Case> caseMap = prepareCaseContextForClaimListByCoverageTypeTest(Arrays.asList("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-ID-002"));
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                    }});
//                });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository())
//                .thenReturn(medicalCoverageRepository);
//
//        when(medicalCoverageRepository.findByType(any()))
//                .thenReturn(Arrays.asList(
//                        new MedicalCoverage() {{
//                            this.setId("MED-COV-ID-001");
//                            this.setCoveragePlans(Arrays.asList(
//                                    new CoveragePlan("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-001"),
//                                    new CoveragePlan("COVERAGE-PLAN-ID-002", "COVERAGE-PLAN-002")
//                            ));
//                        }}
//                ));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//
//        List<ClaimViewCore> claimList = claimService.listClaimsByTypeForClinicList(clinicIdList, null, MedicalCoverage.CoverageType.MEDISAVE
//                , null, "ALL", LocalDate.now().minusDays(2), LocalDate.now());
//
//        assertNotNull("Claim list should not be null", claimList);
//        assertEquals("There should be 2 claims", 2, claimList.size());
//        claimList.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of clinic [" + claimViewCore.getClinicId() + "]");
//
//            Case mockCase = caseMap.get(claimViewCore.getClinicId().split("@")[1]);
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of clinic [" + claimViewCore.getClinicId() + "] passed verification");
//        });
//
//    }
//
//    @Test
//    public void listClaimsByTypeForClinicList_successClaimStatus_SUBMITTED() throws Exception {
//
//        List<String> clinicIdList = Arrays.asList("CLINIC-001@COVERAGE-PLAN-ID-001", "CLINIC-002@COVERAGE-PLAN-ID-002");
//
//        Map<String, Case> caseMap = prepareCaseContextForClaimListByCoverageTypeTest(Arrays.asList("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-ID-002"));
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                    }});
//                });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository())
//                .thenReturn(medicalCoverageRepository);
//
//        when(medicalCoverageRepository.findByType(any()))
//                .thenReturn(Arrays.asList(
//                        new MedicalCoverage() {{
//                            this.setId("MED-COV-ID-001");
//                            this.setCoveragePlans(Arrays.asList(
//                                    new CoveragePlan("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-001"),
//                                    new CoveragePlan("COVERAGE-PLAN-ID-002", "COVERAGE-PLAN-002")
//                            ));
//                        }}
//                ));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        when(patientRepository.findByUserId(anyString()))
//                .thenReturn(mockPatient);
//
//
//        List<ClaimViewCore> claimList = claimService.listClaimsByTypeForClinicList(clinicIdList, null, MedicalCoverage.CoverageType.MEDISAVE
//                , "PATIENT-NRIC-001", "SUBMITTED", LocalDate.now().minusDays(2), LocalDate.now());
//
//        assertNotNull("Claim list should not be null", claimList);
//        assertEquals("There should be only 1 claim", 1, claimList.size());
//        claimList.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of clinic [" + claimViewCore.getClinicId() + "]");
//
//            Case mockCase = caseMap.get(claimViewCore.getClinicId().split("@")[1]);
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of clinic [" + claimViewCore.getClinicId() + "] passed verification");
//        });
//
//    }
//
//    @Test
//    public void listClaimsByClinic_success() throws Exception {
//
//        Map<String, Case> mockCaseMap = new HashMap<>();
//
//
//        when(caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds(anyString(), anyList(),any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenAnswer(invocation -> {
//                    String clinicId = invocation.getArgument(0);
//                    List<String> planIdList = invocation.getArgument(1);
//                    return planIdList.stream()
//                            .map(planId -> {
//                                Case mockCase = new Case() {{
//                                    this.setClinicId(clinicId);
//                                    this.setSalesOrder(new SalesOrder() {{
//                                        this.setInvoices(Arrays.asList(
//                                                new Invoice() {{
//                                                    this.setInvoiceNumber("INVOICE-001-" + planId);
//                                                    this.setPlanId(planId);
//                                                    this.setInvoiceType(InvoiceType.CREDIT);
//                                                    this.setInvoiceTime(LocalDateTime.now());
//                                                    this.setPayableAmount(1000);
//                                                    this.setClaim(new Claim() {{
//                                                        this.setClaimId("CLAIM-001-" + planId);
//                                                        this.setClaimStatus(ClaimStatus.SUBMITTED);
//                                                        this.setManuallyUpdated(false);
//                                                        this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//                                                        this.setClaimDoctorId("CLAIM-DOC-001");
//                                                        this.setAttendingDoctorId("ATTENDING-DOC-001");
//                                                        this.setClaimExpectedAmt(1000);
//                                                        this.setConsultationAmt(450);
//                                                        this.setMedicationAmt(400);
//                                                        this.setMedicalTestAmt(50);
//                                                        this.setOtherAmt(100);
//                                                        this.setGstAmount(70);
//                                                        this.setPayersName("PAYER-NAME");
//                                                        this.setPayersNric("PAYER-NRIC-001");
//                                                        this.setClaimRefNo("CLAIM-REF-001");
//                                                        this.setRemark("CLAIM-REMARK");
//                                                        //set submission details
//                                                        this.setSubmissionResult(new SubmissionResult() {{
//                                                            this.setClaimNo("CLAIM-SUBMISSION-NO-001-" + planId);
//                                                        }});
//
//                                                    }});
//                                                }}
//                                        ));
//                                    }});
//                                    this.setPatientId("PATIENT-001");
//                                }};
//                                mockCaseMap.put("CLAIM-001-" + planId, mockCase);
//                                return mockCase;
//                            })
//                            .collect(Collectors.toList());
//
//                });
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenAnswer(invocation -> {
//                    String claimId = invocation.getArgument(0);
//                    return mockCaseMap.values().stream()
//                            .filter(aCase -> aCase.getSalesOrder().getInvoices().get(0).getClaim().getClaimId().equals(claimId))
//                            .findFirst().get();
//                });
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                        this.setClinicStaffUsernames(Arrays.asList(
//                                "testUser"
//                        ));
//                    }});
//                });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository())
//                .thenReturn(medicalCoverageRepository);
//
//        when(customMedicalCoverageRepository.findPlanByMedicalCoverageId(any()))
//                .thenReturn(
//                        new MedicalCoverage() {{
//                            this.setId("MED-COV-ID-001");
//                            this.setCoveragePlans(Arrays.asList(
//                                    new CoveragePlan("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-001"),
//                                    new CoveragePlan("COVERAGE-PLAN-ID-002", "COVERAGE-PLAN-002")
//                            ));
//                        }}
//                );
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        when(patientRepository.findByUserId("PATIENT-NRIC-001"))
//                .thenReturn(mockPatient);
//
//
//        List<ClaimViewCore> claimList = claimService.listClaimsByClinic("testUser", "MED-COV-ID-001"
//                , "PATIENT-NRIC-001", "ALL", LocalDate.now().minusDays(1), LocalDate.now()
//                , "CLINIC-001");
//
//        assertNotNull("Claim list should not be null", claimList);
//        assertEquals("There should be 2 claims", 2, claimList.size());
//        claimList.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "]");
//
//            Case mockCase = mockCaseMap.get(claimViewCore.getClaim().getClaimId());
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "] passed verification");
//        });
//    }
//
//    @Test
//    public void listClaimsByClinic_successClaimStatus_SUBMITTED() throws Exception {
//
//        Map<String, Case> mockCaseMap = new HashMap<>();
//
//
//        when(caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds(anyString(), anyList(),any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenAnswer(invocation -> {
//                    String clinicId = invocation.getArgument(0);
//                    List<String> planIdList = invocation.getArgument(1);
//                    return planIdList.stream()
//                            .map(planId -> {
//                                Case mockCase = new Case() {{
//                                    this.setClinicId(clinicId);
//                                    this.setSalesOrder(new SalesOrder() {{
//                                        this.setInvoices(Arrays.asList(
//                                                new Invoice() {{
//                                                    this.setInvoiceNumber("INVOICE-001-" + planId);
//                                                    this.setPlanId(planId);
//                                                    this.setInvoiceType(InvoiceType.CREDIT);
//                                                    this.setInvoiceTime(LocalDateTime.now());
//                                                    this.setPayableAmount(1000);
//                                                    this.setClaim(new Claim() {{
//                                                        this.setClaimId("CLAIM-001-" + planId);
//                                                        if (mockCaseMap.size() != 1) {
//                                                            this.setClaimStatus(ClaimStatus.SUBMITTED);
//                                                        } else {
//                                                            this.setClaimStatus(ClaimStatus.PAID);
//                                                        }
//                                                        this.setManuallyUpdated(false);
//                                                        this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//                                                        this.setClaimDoctorId("CLAIM-DOC-001");
//                                                        this.setAttendingDoctorId("ATTENDING-DOC-001");
//                                                        this.setClaimExpectedAmt(1000);
//                                                        this.setConsultationAmt(450);
//                                                        this.setMedicationAmt(400);
//                                                        this.setMedicalTestAmt(50);
//                                                        this.setOtherAmt(100);
//                                                        this.setGstAmount(70);
//                                                        this.setPayersName("PAYER-NAME");
//                                                        this.setPayersNric("PAYER-NRIC-001");
//                                                        this.setClaimRefNo("CLAIM-REF-001");
//                                                        this.setRemark("CLAIM-REMARK");
//                                                        //set submission details
//                                                        this.setSubmissionResult(new SubmissionResult() {{
//                                                            this.setClaimNo("CLAIM-SUBMISSION-NO-001-" + planId);
//                                                        }});
//
//                                                    }});
//                                                }}
//                                        ));
//                                    }});
//                                    this.setPatientId("PATIENT-001");
//                                }};
//                                mockCaseMap.put("CLAIM-001-" + planId, mockCase);
//                                return mockCase;
//                            })
//                            .collect(Collectors.toList());
//
//                });
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenAnswer(invocation -> {
//                    String claimId = invocation.getArgument(0);
//                    return mockCaseMap.values().stream()
//                            .filter(aCase -> aCase.getSalesOrder().getInvoices().get(0).getClaim().getClaimId().equals(claimId))
//                            .findFirst().get();
//                });
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                        this.setClinicStaffUsernames(Arrays.asList(
//                                "testUser"
//                        ));
//                    }});
//                });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository())
//                .thenReturn(medicalCoverageRepository);
//
//        when(customMedicalCoverageRepository.findPlanByMedicalCoverageId(any()))
//                .thenReturn(
//                        new MedicalCoverage() {{
//                            this.setId("MED-COV-ID-001");
//                            this.setCoveragePlans(Arrays.asList(
//                                    new CoveragePlan("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-001"),
//                                    new CoveragePlan("COVERAGE-PLAN-ID-002", "COVERAGE-PLAN-002")
//                            ));
//                        }}
//                );
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        when(patientRepository.findByUserId("PATIENT-NRIC-001"))
//                .thenReturn(mockPatient);
//
//
//        List<ClaimViewCore> claimList = claimService.listClaimsByClinic("testUser", "MED-COV-ID-001"
//                , "PATIENT-NRIC-001", "SUBMITTED", LocalDate.now().minusDays(1), LocalDate.now()
//                , "CLINIC-001");
//
//        assertNotNull("Claim list should not be null", claimList);
//        assertEquals("There should be only 1 claim", 1, claimList.size());
//        claimList.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "]");
//
//            Case mockCase = mockCaseMap.get(claimViewCore.getClaim().getClaimId());
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "] passed verification");
//        });
//    }
//
//    @Test
//    public void listClaimsByClinic_successNoNrIC() throws Exception {
//
//        Map<String, Case> mockCaseMap = new HashMap<>();
//
//
//        when(caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds(anyString(), anyList(),any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenAnswer(invocation -> {
//                    String clinicId = invocation.getArgument(0);
//                    List<String> planIdList = invocation.getArgument(1);
//                    return planIdList.stream()
//                            .map(planId -> {
//                                Case mockCase = new Case() {{
//                                    this.setClinicId(clinicId);
//                                    this.setSalesOrder(new SalesOrder() {{
//                                        this.setInvoices(Arrays.asList(
//                                                new Invoice() {{
//                                                    this.setInvoiceNumber("INVOICE-001-" + planId);
//                                                    this.setPlanId(planId);
//                                                    this.setInvoiceType(InvoiceType.CREDIT);
//                                                    this.setInvoiceTime(LocalDateTime.now());
//                                                    this.setPayableAmount(1000);
//                                                    this.setClaim(new Claim() {{
//                                                        this.setClaimId("CLAIM-001-" + planId);
//                                                        this.setClaimStatus(ClaimStatus.SUBMITTED);
//                                                        this.setManuallyUpdated(false);
//                                                        this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//                                                        this.setClaimDoctorId("CLAIM-DOC-001");
//                                                        this.setAttendingDoctorId("ATTENDING-DOC-001");
//                                                        this.setClaimExpectedAmt(1000);
//                                                        this.setConsultationAmt(450);
//                                                        this.setMedicationAmt(400);
//                                                        this.setMedicalTestAmt(50);
//                                                        this.setOtherAmt(100);
//                                                        this.setGstAmount(70);
//                                                        this.setPayersName("PAYER-NAME");
//                                                        this.setPayersNric("PAYER-NRIC-001");
//                                                        this.setClaimRefNo("CLAIM-REF-001");
//                                                        this.setRemark("CLAIM-REMARK");
//                                                        //set submission details
//                                                        this.setSubmissionResult(new SubmissionResult() {{
//                                                            this.setClaimNo("CLAIM-SUBMISSION-NO-001-" + planId);
//                                                        }});
//
//                                                    }});
//                                                }}
//                                        ));
//                                    }});
//                                    this.setPatientId("PATIENT-ID-001");
//                                }};
//                                mockCaseMap.put("CLAIM-001-" + planId, mockCase);
//                                return mockCase;
//                            })
//                            .collect(Collectors.toList());
//
//                });
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenAnswer(invocation -> {
//                    String claimId = invocation.getArgument(0);
//                    return mockCaseMap.values().stream()
//                            .filter(aCase -> aCase.getSalesOrder().getInvoices().get(0).getClaim().getClaimId().equals(claimId))
//                            .findFirst().get();
//                });
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                        this.setClinicStaffUsernames(Arrays.asList(
//                                "testUser"
//                        ));
//                    }});
//                });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository())
//                .thenReturn(medicalCoverageRepository);
//
//        when(customMedicalCoverageRepository.findPlanByMedicalCoverageId(any()))
//                .thenReturn(
//                        new MedicalCoverage() {{
//                            this.setId("MED-COV-ID-001");
//                            this.setCoveragePlans(Arrays.asList(
//                                    new CoveragePlan("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-001"),
//                                    new CoveragePlan("COVERAGE-PLAN-ID-002", "COVERAGE-PLAN-002")
//                            ));
//                        }}
//                );
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-ID-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//
//        List<ClaimViewCore> claimList = claimService.listClaimsByClinic("testUser", "MED-COV-ID-001"
//                , null, "ALL", LocalDate.now().minusDays(1), LocalDate.now()
//                , "CLINIC-001");
//
//        assertNotNull("Claim list should not be null", claimList);
//        assertEquals("There should be 2 claims", 2, claimList.size());
//        claimList.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "]");
//
//            Case mockCase = mockCaseMap.get(claimViewCore.getClaim().getClaimId());
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "] passed verification");
//        });
//    }
//
//
//    @Test
//    public void listClaimsByClinicByType_success() throws Exception {
//
//        Map<String, Case> mockCaseMap = new HashMap<>();
//
//
//        when(caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds(anyString(), anyList(),any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenAnswer(invocation -> {
//                    String clinicId = invocation.getArgument(0);
//                    List<String> planIdList = invocation.getArgument(1);
//                    return planIdList.stream()
//                            .map(planId -> {
//                                Case mockCase = new Case() {{
//                                    this.setClinicId(clinicId);
//                                    this.setSalesOrder(new SalesOrder() {{
//                                        this.setInvoices(Arrays.asList(
//                                                new Invoice() {{
//                                                    this.setInvoiceNumber("INVOICE-001-" + planId);
//                                                    this.setPlanId(planId);
//                                                    this.setInvoiceType(InvoiceType.CREDIT);
//                                                    this.setInvoiceTime(LocalDateTime.now());
//                                                    this.setPayableAmount(1000);
//                                                    this.setClaim(new Claim() {{
//                                                        this.setClaimId("CLAIM-001-" + planId);
//                                                        this.setClaimStatus(ClaimStatus.SUBMITTED);
//                                                        this.setManuallyUpdated(false);
//                                                        this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//                                                        this.setClaimDoctorId("CLAIM-DOC-001");
//                                                        this.setAttendingDoctorId("ATTENDING-DOC-001");
//                                                        this.setClaimExpectedAmt(1000);
//                                                        this.setConsultationAmt(450);
//                                                        this.setMedicationAmt(400);
//                                                        this.setMedicalTestAmt(50);
//                                                        this.setOtherAmt(100);
//                                                        this.setGstAmount(70);
//                                                        this.setPayersName("PAYER-NAME");
//                                                        this.setPayersNric("PAYER-NRIC-001");
//                                                        this.setClaimRefNo("CLAIM-REF-001");
//                                                        this.setRemark("CLAIM-REMARK");
//                                                        //set submission details
//                                                        this.setSubmissionResult(new SubmissionResult() {{
//                                                            this.setClaimNo("CLAIM-SUBMISSION-NO-001-" + planId);
//                                                        }});
//
//                                                    }});
//                                                }}
//                                        ));
//                                    }});
//                                    this.setPatientId("PATIENT-001");
//                                }};
//                                mockCaseMap.put("CLAIM-001-" + planId, mockCase);
//                                return mockCase;
//                            })
//                            .collect(Collectors.toList());
//
//                });
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenAnswer(invocation -> {
//                    String claimId = invocation.getArgument(0);
//                    return mockCaseMap.values().stream()
//                            .filter(aCase -> aCase.getSalesOrder().getInvoices().get(0).getClaim().getClaimId().equals(claimId))
//                            .findFirst().get();
//                });
//
//        when(clinicRepository.findById(anyString()))
//                .thenAnswer(invocation -> {
//                    String invocationId = String.valueOf(invocation.getArguments()[0]);
//                    return Optional.of(new Clinic() {{
//                        this.setId(invocationId);
//                        this.setUen("UEN-001");
//                        this.setHeCode("CLINIC-HE-001");
//                        this.setDomain("clinic001.domain");
//                        this.setClinicStaffUsernames(Arrays.asList(
//                                "testUser"
//                        ));
//                    }});
//                });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository())
//                .thenReturn(medicalCoverageRepository);
//
//        when(medicalCoverageRepository.findByType(any()))
//                .thenReturn(Arrays.asList(
//                        new MedicalCoverage() {{
//                            this.setId("MED-COV-ID-001");
//                            this.setCoveragePlans(Arrays.asList(
//                                    new CoveragePlan("COVERAGE-PLAN-ID-001", "COVERAGE-PLAN-001"),
//                                    new CoveragePlan("COVERAGE-PLAN-ID-002", "COVERAGE-PLAN-002")
//                            ));
//                        }}
//                ));
//
//        Patient mockPatient = new Patient() {{
//            this.setId("PATIENT-001");
//            this.setName("PATIENT-NAME");
//            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//            this.setPatientPayables(null);
//        }};
//
//        when(patientRepository.findById("PATIENT-001"))
//                .thenReturn(Optional.of(mockPatient));
//
//        when(patientRepository.findByUserId("PATIENT-NRIC-001"))
//                .thenReturn(mockPatient);
//
//
//        List<ClaimViewCore> claimList = claimService.listClaimsByClinicByType("CLINIC-001", "testUser"
//                , MedicalCoverage.CoverageType.MEDISAVE, "PATIENT-NRIC-001", "ALL", LocalDate.now().minusDays(2), LocalDate.now());
//
//        assertNotNull("Claim list should not be null", claimList);
//        assertEquals("There should be 2 claims", 2, claimList.size());
//        claimList.forEach(claimViewCore -> {
//            System.out.println("---> Checking for claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "]");
//
//            Case mockCase = mockCaseMap.get(claimViewCore.getClaim().getClaimId());
//            Invoice mockInvoice = mockCase.getSalesOrder().getInvoices().get(0);
//
//            Claim claim = claimViewCore.getClaim();
//
//            // check claim view fields
//            Clinic mockClinic = new Clinic() {{
//                this.setId(claimViewCore.getClinicId());
//                this.setUen("UEN-001");
//                this.setHeCode("CLINIC-HE-001");
//                this.setDomain("clinic001.domain");
//            }};
//            checkClaimViewFields(mockInvoice, mockClinic, mockPatient, claimViewCore, claim);
//            System.out.println("---> Claim results of claim id [" + claimViewCore.getClaim().getClaimId() + "] passed verification");
//        });
//    }
//
//    private Map<String, Case> prepareCaseContextForClaimListByCoverageTypeTest(List<String> coveragePlanIdList) {
//
//        Map<String, Case> mockCasesMap = coveragePlanIdList.stream()
//                .collect(Collectors.toMap(planId -> planId, planId -> new Case() {{
//                    this.setClinicId("CLINIC-001@" + planId);
//                    this.setSalesOrder(new SalesOrder() {{
//                        this.setInvoices(Arrays.asList(
//                                new Invoice() {{
//                                    this.setInvoiceNumber("INVOICE-001-" + planId);
//                                    this.setPlanId(planId);
//                                    this.setInvoiceType(InvoiceType.CREDIT);
//                                    this.setInvoiceTime(LocalDateTime.now());
//                                    this.setPayableAmount(1000);
//                                    this.setClaim(new Claim() {{
//                                        this.setClaimId("CLAIM-001-" + planId);
//                                        this.setClaimStatus(ClaimStatus.SUBMITTED);
//                                        this.setManuallyUpdated(false);
//                                        this.setDiagnosisCodes(Arrays.asList("DIAG-001", "DIAG-002"));
//                                        this.setClaimDoctorId("CLAIM-DOC-001");
//                                        this.setAttendingDoctorId("ATTENDING-DOC-001");
//                                        this.setClaimExpectedAmt(1000);
//                                        this.setConsultationAmt(450);
//                                        this.setMedicationAmt(400);
//                                        this.setMedicalTestAmt(50);
//                                        this.setOtherAmt(100);
//                                        this.setGstAmount(70);
//                                        this.setPayersName("PAYER-NAME");
//                                        this.setPayersNric("PAYER-NRIC-001");
//                                        this.setClaimRefNo("CLAIM-REF-001");
//                                        this.setRemark("CLAIM-REMARK");
//                                        //set submission details
//                                        this.setSubmissionResult(new SubmissionResult() {{
//                                            this.setClaimNo("CLAIM-SUBMISSION-NO-001-" + planId);
//                                        }});
//
//                                    }});
//                                }}
//                        ));
//                    }});
//                    this.setPatientId("PATIENT-001");
//                }}));
//
//
//        when(caseRepository.findCasesByMedicalCoveragePlanIdsIn(anyList(),any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenAnswer(invocation -> {
//                    List<String> planIdList = invocation.getArgument(0);
//                    return planIdList.stream().map(mockCasesMap::get).collect(Collectors.toList());
//                });
//
//        when(caseRepository.findCaseByClaimId(anyString()))
//                .thenAnswer(invocation -> {
//                    String claimId = invocation.getArgument(0);
//                    return mockCasesMap.values().stream()
//                            .filter(aCase -> claimId.equals(aCase.getSalesOrder().getInvoices().get(0).getClaim().getClaimId()))
//                            .findFirst().get();
//                });
//
//
//        return mockCasesMap;
//    }
//
//    private void checkClaimViewFields(Invoice mockInvoice, Clinic mockClinic, Patient mockPatient,
//                                      ClaimViewCore claimViewCore, Claim claim) {
//        System.out.println("Starting claim view field validation.");
//
//        assertEquals("Bill date is not properly set", mockInvoice.getInvoiceTime(), claimViewCore.getBillDate());
//        assertEquals("Clinic id is wrong", mockClinic.getId(), claimViewCore.getClinicId());
//        assertEquals("Clinic HE code is wrong", mockClinic.getHeCode(), claimViewCore.getClinicHeCode());
//        assertEquals("Hospital code is wrong", "", claimViewCore.getHospitalCode());
//        assertEquals("Claim doctor id is wrong", claim.getClaimDoctorId(), claimViewCore.getClaimDoctorId());
//        assertEquals("Diagnostics list is wrong", claim.getDiagnosisCodes(), claimViewCore.getDiagnosisCodes());
//        assertEquals("Payers Name is wrong", claim.getPayersName(), claimViewCore.getPayersName());
//        assertEquals("Payers NRIC is wrong", claim.getPayersNric(), claimViewCore.getPayersNric());
//        assertEquals("Patients Name is wrong", mockPatient.getName(), claimViewCore.getPatientsName());
//        assertEquals("Patients Nric is wrong", mockPatient.getUserId().getNumber(), claimViewCore.getPatientsNric());
//        assertEquals("Expected Claim amount is wrong", claim.getClaimExpectedAmt(), claimViewCore.getExpectedClaimAmount());
//        assertEquals("Claimed amount is wrong", claim.getClaimedAmount(), claimViewCore.getClaimedAmount());
//        assertEquals("Claim ref no is wrong", claim.getClaimRefNo(), claimViewCore.getClaimRefNo());
//        assertEquals("Claim status is wrong", claim.getClaimStatus(), claimViewCore.getClaimStatus());
//        assertEquals("Claim remarks is wrong", claim.getRemark(), claimViewCore.getClaimRemarks());
//
//        System.out.println("ClaimView fields were properly set");
//    }
//
//    @Test
//    public void checkBalanceForNricNullPlanIdTest() throws CMSException {
//        exceptionRule.expect(RuntimeException.class);
//        exceptionRule.expectMessage("Plan Not available");
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan("null-id")).thenReturn(null);
//
//        claimService.checkBalanceForNric("clinic-01", "null-id", "nric");
//    }
//
//    @Test
//    public void checkBalanceForNricNullClinicIdTest() throws CMSException {
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString()))
//                .thenAnswer(invocation -> {
//                    String planId = String.valueOf(invocation.getArguments()[0]);
//                    return new MedicalCoverage() {{
//                        this.setId(planId);
//                        this.setStatus(Status.ACTIVE);
//                    }};
//                });
//
//        when(clinicRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        exceptionRule.expect(CMSException.class);
//        exceptionRule.expectMessage("No clinic found with given clinicId");
//
//        claimService.checkBalanceForNric("null-id", "coverage-plan-01", "nric");
//    }
//
//    @Test
//    public void checkBalanceForNricUnusedPlanIdTest() throws CMSException {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        ClaimsBalance actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "absent-coverage-plan-01", "nric");
//        assertEquals("Expected claim balance mismatch!", 1000, actualClaimBalance.getAvailableBalance());
//    }
//
//    @Test
//    public void checkBalanceForNricAcutePlansTest() throws CMSException {
//
//        when(patientRepository.findByUserId(anyString())).thenAnswer(invocation -> {
//            String userId = String.valueOf(invocation.getArguments()[0]);
//            return new Patient() {{
//                this.setId(userId);
//            }};
//        });
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        when(caseRepository.findCasesByPatientId(anyString())).thenAnswer(invocation -> {
//            String patientId = String.valueOf(invocation.getArguments()[0]);
//            return Arrays.asList(
//                    new Case() {{
//                        this.setPatientId(patientId);
//                        this.setSalesOrder(new SalesOrder() {{
//                            this.setInvoices(Arrays.asList(
//                                    new Invoice() {{
//                                        this.setPlanId("coverage-plan-01");
//                                    }},
//                                    new Invoice() {{
//                                        this.setPlanId("coverage-plan-02");
//                                    }}
//                            ));
//                        }});
//                    }},
//                    new Case() {{
//                        this.setPatientId(patientId);
//                        this.setSalesOrder(new SalesOrder() {{
//                            this.setInvoices(Arrays.asList(
//                                    new Invoice() {{
//                                        this.setPlanId("coverage-plan-02");
//                                    }},
//                                    new Invoice() {{
//                                        this.setPlanId("coverage-plan-03");
//                                    }}
//                            ));
//                        }});
//                    }}
//            );
//        });
//
//        ClaimsBalance actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-04", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualClaimBalance.getBalanceCheckStatusCode());
//        actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-01", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.E1010, actualClaimBalance.getBalanceCheckStatusCode());
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteChasMhcpErrTest() throws Exception {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.CHAS);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        when(mhcpManager.request(any())).thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.E6001));
//
//        ClaimsBalance actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-05", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.I5000, actualClaimBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 2500, actualClaimBalance.getAvailableBalance());
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteChasTier1Test() throws Exception {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.CHAS);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setTier1SubsidyBalance(new BigDecimal(15));
//                                this.setSubsidyBalance(new BigDecimal(10));
//                            }});
//                        }}))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setSubsidyBalance(new BigDecimal(10));
//                            }});
//                        }}));
//
//        ClaimsBalance actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-05", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualClaimBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 1500, actualClaimBalance.getAvailableBalance());
//
//
//        actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-05", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualClaimBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 1000, actualClaimBalance.getAvailableBalance());
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteChasTier2Test() throws Exception {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.CHAS);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setTier2SubsidyBalance(new BigDecimal(15));
//                                this.setSubsidyBalance(new BigDecimal(10));
//                            }});
//                        }}))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setSubsidyBalance(new BigDecimal(10));
//                            }});
//                        }}));
//
//        ClaimsBalance actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-06", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualClaimBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 1500, actualClaimBalance.getAvailableBalance());
//
//        actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-06", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualClaimBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 1000, actualClaimBalance.getAvailableBalance());
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteChasDefaultTest() throws Exception {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.CHAS);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        when(mhcpManager.request(any())).thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                new CHASAnnualBalanceResponseEntity() {{
//                    this.setResponse(new Response() {{
//                        this.setSubsidyBalance(new BigDecimal(10));
//                    }});
//                }}));
//
//        ClaimsBalance actualClaimBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-07", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualClaimBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 1000, actualClaimBalance.getAvailableBalance());
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteChasCapSubsidyTest() throws Exception {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setCoveragePlans(mockCoveragePlans());
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.CHAS);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        when(mhcpManager.request(any()))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setSubsidyBalance(new BigDecimal(10));
//                            }});
//                        }}))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setSubsidyBalance(new BigDecimal(30));
//                            }});
//                        }}))
//                .thenAnswer(invocation -> new MHCPResult(com.lippo.connector.mhcp.util.StatusCode.S0000,
//                        new CHASAnnualBalanceResponseEntity() {{
//                            this.setResponse(new Response() {{
//                                this.setSubsidyBalance(new BigDecimal(0));
//                            }});
//                        }}));
//
//        // when the balance received from mhcp is less than the cap per visit
//        ClaimsBalance actualBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-07", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 1000, actualBalance.getAvailableBalance());
//
//        // when the balance received from mhcp is greater than cap per visit
//        actualBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-07", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", 2500, actualBalance.getAvailableBalance());
//
//        // when the balance received from mhcp is 0
//        actualBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-07", "nric");
//        assertEquals("Expected status code mismatch!", StatusCode.S0000, actualBalance.getBalanceCheckStatusCode());
//        assertEquals("Expected balance mismatch", -1, actualBalance.getAvailableBalance());
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteMedisaveTest() throws Exception {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.MEDISAVE);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        ClaimsBalance claimsBalance = claimService.checkBalanceForNric("clinic-01", "coverage-plan-07", "nric");
//        assertNull("Claim balance should be null for this", claimsBalance);
//    }
//
//    @Test
//    public void checkBalanceForNricNonAcuteDefaultTest() throws CMSException {
//
//        when(customMedicalCoverageRepository.findMedicalCoverageByPlan(anyString())).thenAnswer(invocation -> {
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                this.setStatus(Status.ACTIVE);
//                this.setType(CoverageType.CORPORATE);
//            }};
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//            }});
//        });
//
//        exceptionRule.expect(RuntimeException.class);
//        exceptionRule.expectMessage("Only CHAS and MEDISAVE is supported");
//
//        claimService.checkBalanceForNric("clinic-01", "coverage-plan-07", "nric");
//    }
//
//    @Test
//    @Ignore
//    public void listClaimsByClinicTest() throws Exception {
//        Map<String, Case> caseMap = new HashMap<>();
//        final String SHOW_ALL_CLAIMS = "ALL";
//        final String name = null;
//        String status = SHOW_ALL_CLAIMS;
//        LocalDate startDate = LocalDate.now().minusYears(1), endDate = LocalDate.now();
//        String clinicId = "clinic-01";
//        String payerNric = "nric";
//
//        when(customMedicalCoverageRepository.findPlanByMedicalCoverageId(anyString())).thenAnswer(invocation -> {
//            List<CoveragePlan> plans = mockCoveragePlans();
//            String planId = String.valueOf(invocation.getArguments()[0]);
//            return new MedicalCoverage() {{
//                this.setId(planId);
//                int size = plans.size();
//                int skip = 2;
//                int limit = size / skip + Math.min(size % skip, 1);
//                this.setCoveragePlans(Stream.iterate(0, i -> i + skip)
//                        .limit(limit)
//                        .map(plans::get)
//                        .collect(Collectors.toList()));
//            }};
//        });
//
//        when(caseRepository.findCaseByClaimId(anyString())).thenAnswer(invocation -> {
//            String claimId = String.valueOf(invocation.getArguments()[0]);
//            return caseMap.entrySet().stream()
//                    .filter(c -> c.getValue()
//                            .getSalesOrder()
//                            .getInvoices()
//                            .stream().filter(i -> i.getClaim().getClaimId().equals(claimId)).count() >= 1)
//                    .map(Map.Entry::getValue)
//                    .collect(Collectors.toList()).get(0);
//        });
//
//        when(patientRepository.findById(anyString())).thenAnswer(invocation -> {
//            String patientId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Patient() {{
//                this.setId(patientId);
//            }});
//        });
//
//        when(caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds(anyString(), anyList(),any(LocalDateTime.class), any(LocalDateTime.class))).thenAnswer(invocation -> {
//            String ci = String.valueOf(invocation.getArguments()[0]);
//            List<String> planIdList = (ArrayList<String>) invocation.getArguments()[1];
//            List<Case> cases = new ArrayList<>();
//            Random randomizePlanIds = new Random();
//
//            cases.add(
//                    new Case() {{
//                        this.setId("case-1");
//                        this.setPatientId("patient-1");
//                        this.setClinicId(ci);
//                        this.setSalesOrder(new SalesOrder() {{
//                            this.setStatus(SalesStatus.OPEN);
//                            this.setInvoices(Arrays.asList(
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(randomizePlanIds.nextInt(planIdList.size())));
//                                        this.setVisitId("visit-01");
//                                        this.setTaxAmount(20);
//                                        this.setPayableAmount(200);
//                                        this.setInvoiceTime(LocalDateTime.now().minusMonths(2));
//                                        this.setClaim(createMockClaim("claim-1", payerNric, 220, Claim.ClaimStatus.SUBMITTED));
//                                    }},
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(randomizePlanIds.nextInt(planIdList.size())));
//                                        this.setVisitId("visit-02");
//                                        this.setTaxAmount(10);
//                                        this.setPayableAmount(100);
//                                        this.setInvoiceTime(LocalDateTime.now().minusMonths(1));
//                                        this.setClaim(createMockClaim("claim-2", null, 110, Claim.ClaimStatus.PENDING));
//                                    }}));
//                        }});
//                    }}
//            );
//
//            cases.add(
//                    new Case() {{
//                        this.setId("case-2");
//                        this.setPatientId("patient-1");
//                        this.setClinicId(ci);
//                        this.setSalesOrder(new SalesOrder() {{
//                            this.setStatus(SalesStatus.OPEN);
//                            this.setInvoices(Arrays.asList(
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(randomizePlanIds.nextInt(planIdList.size())));
//                                        this.setVisitId("visit-01;");
//                                        this.setTaxAmount(20);
//                                        this.setPayableAmount(200);
//                                        this.setInvoiceTime(LocalDateTime.now().minusDays(56));
//                                        this.setClaim(createMockClaim("claim-3", payerNric, 220, Claim.ClaimStatus.SUBMITTED));
//                                    }},
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(randomizePlanIds.nextInt(planIdList.size())));
//                                        this.setVisitId("visit-02");
//                                        this.setTaxAmount(10);
//                                        this.setPayableAmount(100);
//                                        this.setInvoiceTime(LocalDateTime.now().minusDays(26));
//                                        this.setClaim(createMockClaim("claim-4", payerNric, 110, Claim.ClaimStatus.SUBMITTED));
//                                    }}));
//                        }});
//                    }}
//            );
//
//            cases.forEach(c -> caseMap.put(c.getId(), c));
//            return cases;
//        });
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String cId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(cId);
//            }});
//        });
//
//        int expectedLength = 3;
//        List<ClaimViewCore> claimViewCoreList = claimService.listClaimsByClinic(null, "medical-coverage-01", payerNric, status, startDate, endDate, clinicId);
//
//        assertEquals("Expected value mismatch", expectedLength, claimViewCoreList.size());
//    }
//
//    @Test
//    @Ignore
//    public void listClaimsByClinicByTypeTest() throws Exception {
//        Random random = new Random();
//        Map<String, Case> caseMap = new HashMap<>();
//        String username = "user-1";
//        LocalDate startDate = LocalDate.now().minusYears(1), endDate = LocalDate.now();
//
//        List<CoveragePlan> coveragePlans = mockCoveragePlans();
//
//        when(clinicRepository.findById(anyString())).thenAnswer(invocation -> {
//            String clinicId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Clinic() {{
//                this.setId(clinicId);
//                this.getClinicStaffUsernames().add(username);
//            }});
//        });
//
//        when(customMedicalCoverageRepository.getMedicalCoverageRepository()).thenReturn(medicalCoverageRepository);
//
//        when(medicalCoverageRepository.findByType(any(MedicalCoverage.CoverageType.class))).thenAnswer(invocation -> {
//            MedicalCoverage.CoverageType coverageType = (MedicalCoverage.CoverageType) invocation.getArguments()[0];
//            return Arrays.asList(
//                    new MedicalCoverage() {{
//                        this.setId("medical-coverage-1");
//                        this.setType(coverageType);
//                        Collections.shuffle(coveragePlans);
//                        this.setCoveragePlans(coveragePlans.subList(0, 5));
//                    }},
//                    new MedicalCoverage() {{
//                        this.setId("medical-coverage-2");
//                        this.setType(coverageType);
//                        Collections.shuffle(coveragePlans);
//                        this.setCoveragePlans(coveragePlans.subList(0, 5));
//                    }},
//                    new MedicalCoverage() {{
//                        this.setId("medical-coverage-3");
//                        this.setType(coverageType);
//                        Collections.shuffle(coveragePlans);
//                        this.setCoveragePlans(coveragePlans.subList(0, 5));
//                    }}
//            );
//        });
//
//        when(caseRepository.findCasesByMedicalCoveragePlanIdsIn(anyList(),any(LocalDateTime.class), any(LocalDateTime.class))).thenAnswer(invocation -> {
//            List<String> planIdList = (ArrayList<String>) invocation.getArguments()[0];
//            List<Case> cases = new ArrayList<>();
//            String payerNric = "NRIC0001";
//
//            cases.add(
//                    new Case() {{
//                        this.setId("case-1");
//                        this.setPatientId("patient-1");
//                        this.setClinicId("clinic-1");
//                        this.setSalesOrder(new SalesOrder() {{
//                            this.setStatus(SalesStatus.OPEN);
//                            this.setInvoices(Arrays.asList(
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(1));
//                                        this.setVisitId("visit-01");
//                                        this.setTaxAmount(20);
//                                        this.setPayableAmount(200);
//                                        this.setInvoiceTime(LocalDateTime.now().minusMonths(2));
//                                        this.setClaim(createMockClaim("claim-1", payerNric, 220, Claim.ClaimStatus.SUBMITTED));
//                                    }},
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(3));
//                                        this.setVisitId("visit-02");
//                                        this.setTaxAmount(10);
//                                        this.setPayableAmount(100);
//                                        this.setInvoiceTime(LocalDateTime.now().minusMonths(1));
//                                        this.setClaim(createMockClaim("claim-2", null, 110, Claim.ClaimStatus.PENDING));
//                                    }}));
//                        }});
//                    }}
//            );
//
//            cases.add(
//                    new Case() {{
//                        this.setId("case-2");
//                        this.setPatientId("patient-1");
//                        this.setClinicId("clinic-1");
//                        this.setSalesOrder(new SalesOrder() {{
//                            this.setStatus(SalesStatus.OPEN);
//                            this.setInvoices(Arrays.asList(
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(2));
//                                        this.setVisitId("visit-01;");
//                                        this.setTaxAmount(20);
//                                        this.setPayableAmount(200);
//                                        this.setInvoiceTime(LocalDateTime.now().minusDays(56));
//                                        this.setClaim(createMockClaim("claim-3", payerNric, 220, Claim.ClaimStatus.SUBMITTED));
//                                    }},
//                                    new Invoice() {{
//                                        this.setInvoiceType(Invoice.InvoiceType.CREDIT);
//                                        this.setPlanId(planIdList.get(4));
//                                        this.setVisitId("visit-02");
//                                        this.setTaxAmount(10);
//                                        this.setPayableAmount(100);
//                                        this.setInvoiceTime(LocalDateTime.now().minusDays(26));
//                                        this.setClaim(createMockClaim("claim-4", payerNric, 110, Claim.ClaimStatus.SUBMITTED));
//                                    }}));
//                        }});
//                    }}
//            );
//
//            cases.forEach(c -> caseMap.put(c.getId(), c));
//            return cases;
//        });
//
//        when(caseRepository.findCaseByClaimId(anyString())).thenAnswer(invocation -> {
//            String claimId = String.valueOf(invocation.getArguments()[0]);
//            return caseMap.entrySet().stream()
//                    .filter(c -> c.getValue()
//                            .getSalesOrder()
//                            .getInvoices()
//                            .stream().filter(i -> i.getClaim().getClaimId().equals(claimId)).count() >= 1)
//                    .map(Map.Entry::getValue)
//                    .collect(Collectors.toList()).get(0);
//        });
//
//        when(patientRepository.findById(anyString())).thenAnswer(invocation -> {
//            String patientId = String.valueOf(invocation.getArguments()[0]);
//            return Optional.of(new Patient() {{
//                this.setId(patientId);
//            }});
//        });
//
//        List<ClaimViewCore> claimViewCoreList = claimService.listClaimsByClinicByType(null, username, MedicalCoverage.CoverageType.CORPORATE, "NRIC0001", "ALL", startDate, endDate);
//        System.out.println();
//        long matchingInvoiceCount = caseMap.values()
//                .stream()
//                .map(c -> c.getSalesOrder().getInvoices().toArray())
//                .flatMap(Arrays::stream)
//                .map(o -> (Invoice) o)
//                .filter(i -> i.getClaim().getPayersNric() != null)
//                .count();
//
//        int medicalCoverageCount = 3; //TODO: Count this programmatically
//        long expectedSize = matchingInvoiceCount * medicalCoverageCount;
//
//        assertEquals("Expected amount mismatch!", expectedSize, claimViewCoreList.size());
//    }
//
//    private Invoice createMockInvoice(Invoice.InvoiceType invoiceType, String planId, String visitId,
//                                      int taxAmount, int payableAmount) {
//        return new Invoice() {{
//            this.setVisitId(visitId);
//            this.setInvoiceType(invoiceType);
//            this.setPlanId(planId);
//            this.setTaxAmount(taxAmount);
//            this.setPayableAmount(payableAmount);
//
//        }};
//    }
//
//    private List<CoveragePlan> mockCoveragePlans() {
//        return Arrays.asList(
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-01");
//                    this.setName("Coverage Plan Uno");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(1000);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-02");
//                    this.setName("Coverage Plan Dos");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(1500);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-03");
//                    this.setName("Coverage Plan Tres");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(2000);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-04");
//                    this.setName("Coverage Plan Cuatro");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(2500);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-05");
//                    this.setName("Coverage Plan Cinco");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(2500);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-06");
//                    this.setName("Coverage Plan Seis");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(2500);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("coverage-plan-07");
//                    this.setName("Coverage Plan Siete");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(2500);
//                    }});
//                }},
//                new CoveragePlan() {{
//                    this.setId("absent-coverage-plan-01");
//                    this.setName("Coverage Plan Abs");
//                    this.setCapPerVisit(new CapLimiter() {{
//                        this.setLimit(1000);
//                    }});
//                }}
//        );
//    }
//
//    private void assertClaimFields(Invoice invoice) {
//        assertNotNull("Claim is not properly set to the invoice", invoice.getClaim());
//        Claim claim = invoice.getClaim();
//        assertFalse("Claim was not correctly marked as manual", claim.isManuallyUpdated());
//        assertEquals("Claim Status error", Claim.ClaimStatus.PENDING, claim.getClaimStatus());
//        assertEquals("Claim Expected amount error", invoice.getPayableAmount(), claim.getClaimExpectedAmt());
//        assertEquals("Claim Tax amount error", invoice.getTaxAmount(), claim.getGstAmount());
//        assertEquals("Claim Attending doctor error ", "DOCTOR-001", claim.getAttendingDoctorId());
//        assertEquals("Claim Claiming doctor error ", "DOCTOR-001", claim.getClaimDoctorId());
//        assertEquals("Claim payer details was not set properly", "PATIENT-NAME", claim.getPayersName());
//        assertEquals("Claim payer details was not set properly", "PATIENT-NRIC-001", claim.getPayersNric());
//    }
//
//    private SalesOrder prepareClaimPopulationContext() {
//        // cov-pln-001 -> MEDISAVE 3000
//        // cov-pln-002,3 -> CHAS 2X3000
//
//        SalesOrder salesOrder = new SalesOrder() {{
//            this.setPurchaseItems(Arrays.asList(
//                    createMockSalesItem("ITEM-REF-001", Set.of("cov-pln-001", "cov-pln-002"), 3000, Item.ItemType.DRUG)
//                    , createMockSalesItem("ITEM-REF-002", Set.of("cov-pln-002"), 2000, Item.ItemType.LABORATORY)
//                    , createMockSalesItem("ITEM-REF-003", Set.of("cov-pln-003"), 2500, Item.ItemType.CONSULTATION)
//                    , createMockSalesItem("ITEM-REF-004", Set.of(), 2500, Item.ItemType.IMPLANTS)
//            ));
//            this.setInvoices(Arrays.asList(
//                    createMockInvoice(Invoice.InvoiceType.DIRECT, null, "VISIT-001", 70, 1000)
//                    , createMockInvoice(Invoice.InvoiceType.CREDIT, "cov-pln-001", "VISIT-001", 210, 3000)
//                    , createMockInvoice(Invoice.InvoiceType.CREDIT, "cov-pln-002", "VISIT-001", 210, 3000)
//                    , createMockInvoice(Invoice.InvoiceType.CREDIT, "cov-pln-003", "VISIT-001", 210, 3000)
//            ));
//
//        }};
//
//        when(visitRegistryRepository.findById(anyString()))
//                .thenReturn(Optional.of(
//                        new PatientVisitRegistry() {{
//                            this.setClinicId("CLINIC-001");
//                            this.setPatientId("PATIENT-001");
//                            this.setMedicalReference(
//                                    new MedicalReference() {{
//                                        this.setConsultation(new Consultation() {{
//                                            this.setDoctorId("DOCTOR-001");
//                                        }});
//                                        this.setDiagnosisIds(Arrays.asList("DIAG-001", "DIAG-002", "DIAG-003"));
//                                    }}
//                            );
//                        }}));
//
//        when(runningNumberService.generateClaimRefNumber())
//                .thenReturn("claim-001")
//                .thenReturn("claim-002")
//                .thenReturn("claim-003");
//
//        when(medicalCoverageRepository.findMedicalCoverageByPlanId(anyString()))
//                .thenAnswer(invocationOnMock -> {
//                    String planId = invocationOnMock.getArgument(0);
//                    if ("cov-pln-001".equals(planId)) {
//                        return new MedicalCoverage() {{
//                            this.setType(CoverageType.MEDISAVE);
//                        }};
//                    } else {
//                        return new MedicalCoverage() {{
//                            this.setType(CoverageType.CHAS);
//                        }};
//                    }
//                });
//
//        when(doctorRepository.findById(anyString()))
//                .thenReturn(Optional.of(
//                        new Doctor() {{
//                            this.setDoctorGroup(DoctorGroup.ANCHOR);
//                            this.setId("DOCTOR-001");
//                        }}
//                ));
//
//        when(clinicDatabaseService.findOne(anyString()))
//                .thenReturn(Optional.of(
//                        new Clinic() {{
//                            this.setId("CLINIC-001");
//                        }}
//                ));
//
//        when(diagnosisRepository.findAllById(anyCollection()))
//                .thenReturn(Arrays.asList(
//                        createMockDiagnosis("DIAG-001", "DIAG-ICD10.CODE-001")
//                        , createMockDiagnosis("DIAG-002", "DIAG-ICD10.CODE-002")
//                        , createMockDiagnosis("DIAG-003", "DIAG-ICD10.CODE-003")
//                ));
//
//        when(patientRepository.findById(anyString()))
//                .thenReturn(
//                        Optional.of(new Patient() {{
//                            this.setName("PATIENT-NAME");
//                            this.setUserId(new UserId(UserId.IdType.NRIC, "PATIENT-NRIC-001"));
//                        }})
//                );
//
//        return salesOrder;
//    }
//
//    private Diagnosis createMockDiagnosis(String diagnosisId, String icd10Code) {
//        return new Diagnosis() {{
//            this.setId(diagnosisId);
//            this.setIcd10Code(icd10Code);
//        }};
//    }
//
//    private SalesItem createMockSalesItem(String itemRefId, Set<String> excludedCoveragePlans,
//                                          int soldPrice, Item.ItemType itemType) {
//        int purchaseQty = 10;
//        int sellingPrice = soldPrice / 10;
////        int adjustmentValue = 0;
////        ItemPriceAdjustment.PaymentType paymentType = ItemPriceAdjustment.PaymentType.DOLLAR;
//        return new SalesItem() {{
//            this.setItemRefId(itemRefId);
//            this.setExcludedCoveragePlanIds(excludedCoveragePlans);
//            this.setSoldPrice(soldPrice);
//            this.setItemType(itemType);
//            this.setPurchaseQty(purchaseQty);
//            this.setSellingPrice(new SellingPrice(sellingPrice, true));
////            this.setItemPriceAdjustment(new ItemPriceAdjustment(adjustmentValue, paymentType,""));
//        }};
//    }
//
//    private Claim createMockClaim(String claimId, String payerNric, int expectedAmt, Claim.ClaimStatus claimStatus) {
//        final String pnric = payerNric;
//        return new Claim() {{
//            this.setClaimId(claimId);
//            this.setClaimExpectedAmt(expectedAmt);
//            this.setClaimStatus(claimStatus);
//            if (pnric != null)
//                this.setPayersNric(pnric);
//        }};
//    }
}