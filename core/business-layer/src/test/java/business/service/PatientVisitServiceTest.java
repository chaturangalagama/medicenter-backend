package business.service;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.*;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.QueueStore;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.sales.SalesOrder;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.core.entity.visit.VisitPurpose;
import com.ilt.cms.pm.business.service.patient.patientVisit.PatientVisitService;
import com.ilt.cms.pm.business.service.clinic.billing.SalesOrderService;
import com.ilt.cms.repository.clinic.*;
import com.ilt.cms.repository.clinic.billing.SalesOrderRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationFollowupRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationRepository;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import com.ilt.cms.repository.patient.PatientRepository;
import com.ilt.cms.repository.patient.patientVisit.DiagnosisRepository;
import com.ilt.cms.repository.patient.patientVisit.PatientReferralRepository;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class PatientVisitServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PatientVisitServiceTest.class);

    @Autowired
    private PatientVisitService patientVisitService;

    @Autowired
    private PatientVisitRepository patientVisitRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private PatientReferralRepository patientReferralRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private ConsultationFollowupRepository consultationFollowupRepository;
    @Autowired
    private VisitPurposeRepository visitPurposeRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;
    @Autowired
    private SalesOrderService salesOrderService;

    private MockMvc mockMvc;

//    @MockBean
//    private Authentication principal;

    @Before
    public void setUp() throws Exception {
//        when(principal.getName()).thenReturn("John");

        when(patientVisitRepository.findById("V0000")).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String idStr = invocation.getArgument(0);
                    PatientVisitRegistry visitRegistry = MockPatientVisitRegistry.mockVisitRegistry();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(visitRegistry, idStr);
                    return Optional.of(visitRegistry);
                }
        );

        when(patientVisitRepository.findById(eq("V0001"))).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String visitIdStr = invocation.getArgument(0);
                    PatientVisitRegistry visitRegistry = MockPatientVisitRegistry.mockVisitRegistry();
                    Field status = PatientVisitRegistry.class.getDeclaredField("visitStatus");
                    status.setAccessible(true);
                    status.set(visitRegistry, PatientVisitRegistry.PatientVisitState.CONSULT);
                    Field visitId = PersistedObject.class.getDeclaredField("id");
                    visitId.setAccessible(true);
                    visitId.set(visitRegistry, visitIdStr);
                    return Optional.of(visitRegistry);
                }
        );

        when(patientVisitRepository.findById(eq("V0002"))).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String visitIdStr = invocation.getArgument(0);
                    PatientVisitRegistry visitRegistry = MockPatientVisitRegistry.mockVisitRegistry();
                    Field status = PatientVisitRegistry.class.getDeclaredField("visitStatus");
                    status.setAccessible(true);
                    status.set(visitRegistry, PatientVisitRegistry.PatientVisitState.POST_CONSULT);
                    Field visitId = PersistedObject.class.getDeclaredField("id");
                    visitId.setAccessible(true);
                    visitId.set(visitRegistry, visitIdStr);
                    return Optional.of(visitRegistry);
                }
        );

        when(patientVisitRepository.findById(eq("V0003"))).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String visitIdStr = invocation.getArgument(0);
                    PatientVisitRegistry visitRegistry = MockPatientVisitRegistry.mockVisitRegistry();
                    Field status = PatientVisitRegistry.class.getDeclaredField("visitStatus");
                    status.setAccessible(true);
                    status.set(visitRegistry, PatientVisitRegistry.PatientVisitState.PAYMENT);
                    Field visitId = PersistedObject.class.getDeclaredField("id");
                    visitId.setAccessible(true);
                    visitId.set(visitRegistry, visitIdStr);
                    return Optional.of(visitRegistry);
                }
        );

        when(patientVisitRepository.findById(eq("1245842"))).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String visitIdStr = invocation.getArgument(0);
                    PatientVisitRegistry visitRegistry = MockPatientVisitRegistry.mockVisitRegistry();
                    Field visitId = PersistedObject.class.getDeclaredField("id");
                    visitId.setAccessible(true);
                    visitId.set(visitRegistry, visitIdStr);
                    return Optional.of(visitRegistry);
                }
        );

        when(patientVisitRepository.findByVisitNumber(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String visitIdStr = invocation.getArgument(0);
                    PatientVisitRegistry visitRegistry = MockPatientVisitRegistry.mockVisitRegistry();
                    Field visitId = PersistedObject.class.getDeclaredField("id");
                    visitId.setAccessible(true);
                    visitId.set(visitRegistry, visitIdStr);
                    return Optional.of(visitRegistry);
                }
        );


        when(patientVisitRepository.findAllById(anyList())).thenReturn(Arrays.asList(MockPatientVisitRegistry.mockVisitRegistry(), MockPatientVisitRegistry.mockVisitRegistry()));
        when(patientVisitRepository.findAllByPatientId(anyString())).thenReturn(Arrays.asList(MockPatientVisitRegistry.mockVisitRegistry(), MockPatientVisitRegistry.mockVisitRegistry()));
        Page<PatientVisitRegistry> mockVisit = mock(Page.class);
        when(mockVisit.getTotalElements()).thenReturn(1000L);
        when(mockVisit.getContent()).thenReturn(Arrays.asList(MockPatientVisitRegistry.mockVisitRegistry()));
        when(mockVisit.getTotalPages()).thenReturn(10);
        when(mockVisit.getNumber()).thenReturn(2);
        when(patientVisitRepository.findAll(any(Pageable.class))).thenReturn(mockVisit);
        when(patientVisitRepository.findAll()).thenAnswer(
                (Answer<List<PatientVisitRegistry>>) invocation -> {
                    PatientVisitRegistry registry = MockPatientVisitRegistry.mockVisitRegistry();
                    return Arrays.asList(registry);
                }
        );

        when(patientVisitRepository.save(any(PatientVisitRegistry.class))).thenReturn(MockPatientVisitRegistry.mockVisitRegistryTwo());

        when(patientRepository.existsById(anyString())).thenReturn(true);
        when(clinicRepository.existsById(anyString())).thenReturn(true);
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(MockClinic.mockClinic()));
        when(visitPurposeRepository.findByName(anyString())).thenReturn(Optional.of(new VisitPurpose("Visit propose")));
        when(doctorRepository.existsById(anyString())).thenReturn(true);
        when(doctorRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String doctorId = invocation.getArgument(0);
                    Doctor doctor = MockDoctor.mockDoctor();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(doctor, doctorId);
                    doctor.setUsername("John");
                    return Optional.of(doctor);
                }
        );
        when(consultationRepository.existsById(anyString())).thenReturn(true);
        when(diagnosisRepository.existsById(anyString())).thenReturn(true);
        when(patientVisitRepository.existsByVisitNumber(anyString())).thenReturn(true);
        when(patientVisitRepository.existsById(anyString())).thenReturn(true);

        when(mongoTemplate.find(any(Query.class), any())).thenReturn(Arrays.asList(MockPatientVisitRegistry.mockVisitRegistry()));

        when(mongoTemplate.findAndModify(any(), any(Update.class), any(FindAndModifyOptions.class), eq(QueueStore.class)))
                .thenReturn(new QueueStore(LocalDate.now(), 100L));


        when(consultationRepository.save(any(Consultation.class))).thenReturn(MockPatientVisitRegistry.mockConsultation());
        when(consultationRepository.findById(anyString()))
                .thenAnswer(
                        (Answer<Optional>) invocation -> {
                            Consultation consultation = MockPatientVisitRegistry.mockConsultation();
                            return Optional.of(consultation);
                        }
                );
        when(consultationFollowupRepository.save(any(ConsultationFollowup.class))).thenReturn(MockPatientVisitRegistry.mockConsultationFollowup());
        when(consultationFollowupRepository.findById(anyString()))
                .thenAnswer(
                        (Answer<Optional>) invocation -> {
                            ConsultationFollowup followup = MockPatientVisitRegistry.mockConsultationFollowup();
                            return Optional.of(followup);
                        }
                );
        when(patientReferralRepository.save(any(PatientReferral.class))).thenReturn(MockPatientVisitRegistry.mockPatientReferral());
        when(patientReferralRepository.findById(anyString()))
                .thenAnswer(
                        (Answer<Optional>) invocation -> {
                            PatientReferral referral = MockPatientVisitRegistry.mockPatientReferral();
                            return Optional.of(referral);
                        }
                );
        when(itemRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String itemId = invocation.getArgument(0);
                    Item item = MockItem.mockItem(itemId);
                    return Optional.of(item);
                }
        );
        when(itemRepository.existsById(any())).thenReturn(true);
        when(itemRepository.findAllById(any())).thenAnswer(
                (Answer<List<Item>>) invocation -> {
                    List<String> arguments = (List) invocation.getArgument(0);
                    if (arguments == null) {
                        return new ArrayList<>();
                    }
                    return arguments.stream()
                            .map(strings -> {
                                Item item = MockItem.mockItem();
                                item.setId(strings);
                                item.setStatus(Status.ACTIVE);
                                return item;
                            })
                            .collect(Collectors.toList());
                }
        );
        when(patientReferralRepository.findById(anyString())).thenReturn(Optional.of(MockPatientVisitRegistry.mockPatientReferral()));
        when(consultationFollowupRepository.findById(anyString())).thenReturn(Optional.of(MockPatientVisitRegistry.mockConsultationFollowup()));
        when(patientReferralRepository.existsById(anyString())).thenReturn(true);
        when(consultationFollowupRepository.existsById(anyString())).thenReturn(true);
        when(salesOrderRepository.findById(any())).thenReturn(Optional.of(mock(SalesOrder.class)));
        when(salesOrderService.updateSalesOrder(any())).thenReturn(new SalesOrder());
    }

    @Test
    public void searchById() throws CMSException {
        PatientVisitRegistry visit = patientVisitService.searchById("V0000");
        assertEquals("V0000", visit.getId());
    }

    @Test
    public void searchByVisitNumber() throws CMSException {
        PatientVisitRegistry visit = patientVisitService.searchByVisitNumber("V0001");
        assertEquals("V0001", visit.getId());
    }

    @Test
    public void searchByIds() throws CMSException {
        List<PatientVisitRegistry> visits = patientVisitService.searchByIds(Arrays.asList("V0000", "74852"));
        assertEquals(2, visits.size());
    }

    @Test
    public void listAll() throws CMSException {
        List<PatientVisitRegistry> visits = patientVisitService.listVisits("P0001");
        assertEquals("1245842", visits.get(0).getId());
    }

    @Test
    public void listAllPageable() throws CMSException {
        Page<PatientVisitRegistry> visits = patientVisitService.listVisits("P0001", 1, 10);
        assertEquals("1245842", visits.getContent().get(0).getId());
    }

    @Test
    public void updatePatientVisitRegistry() throws CMSException {
        PatientVisitRegistry registry = MockPatientVisitRegistry.mockVisitRegistry();
        PatientVisitRegistry updateRegistry = patientVisitService.updatePatientVisitRegistry("1245842", registry);
        assertEquals("1245842", updateRegistry.getId());
    }

    @Test
    public void changeStateToConsult() throws CMSException {
        PatientVisitRegistry savedRegistry = patientVisitService.changeStateToConsult("1245842", "57645", true);
        assertEquals("1245842", savedRegistry.getId());
    }

//    @Test
//    public void saveConsultationData() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReference();
//        PatientVisitRegistry savedRegistry = patientVisitService.saveConsultationData("V0001", medicalReference, principal);
//        assertEquals("1245842", savedRegistry.getId());
//    }

//    @Test
//    public void changeStateToPostConsult() throws CMSException {
//        PatientVisitRegistry savedRegistry = patientVisitService.changeStateToPostConsult("V0001", MockPatientVisitRegistry.mockMedicalReference(), principal);
//        assertEquals("1245842", savedRegistry.getId());
//
//    }

    @Test
    public void rollbackStatusToPostConsult() throws CMSException {
        PatientVisitRegistry savedRegistry = patientVisitService.rollbackStatusToPostConsult("V0003");
        assertEquals("1245842", savedRegistry.getId());
    }

//    @Test
//    public void saveDispensingData() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        PatientVisitRegistry savedRegistry = patientVisitService.saveDispensingData("V0002", medicalReference, principal);
//        assertEquals("1245842", savedRegistry.getId());
//
//    }

//    @Test
//    public void changeStatusToPayment() throws CMSException {
//        PatientVisitRegistry savedRegistry = patientVisitService.changeStatusToPayment("V0002",
//                MockPatientVisitRegistry.mockMedicalReference(), principal, Collections.emptyMap());
//        assertEquals("1245842", savedRegistry.getId());
//    }

    @Test
    public void changeStatusToComplete() throws CMSException {
        PatientVisitRegistry savedRegistry = patientVisitService.changeStatusToComplete("V0002");
        assertEquals("1245842", savedRegistry.getId());
    }

//    @Test
//    public void updateConsultation() throws CMSException {
//        Consultation consultation = MockPatientVisitRegistry.mockConsultation();
//        PatientVisitRegistry registry = patientVisitService.updateConsultation("V0001", consultation, Arrays.asList("4854"), principal);
//        assertEquals("1245842", registry.getId());
//    }

//    @Test
//    public void updatePatientReferral() throws CMSException {
//        PatientReferral referral = MockPatientVisitRegistry.mockPatientReferral();
//        PatientVisitRegistry registry = patientVisitService.updatePatientReferral("V0001", referral, principal);
//        assertEquals("V0001", registry.getId()); //the main visit should not need to update because its just an update call
//    }

//    @Test
//    public void updateConsultationFollowup() throws CMSException {
//        ConsultationFollowup followup = MockPatientVisitRegistry.mockConsultationFollowup();
//        PatientVisitRegistry registry = patientVisitService.updateConsultationFollowup("V0001", followup, principal);
//        assertEquals("1245842", registry.getId());
//    }

//    @Test
//    public void updateMedicalCertificates() throws CMSException {
//        List<MedicalCertificate> certificates = Arrays.asList(MockPatientVisitRegistry.mockMedicalCertificate());
//        PatientVisitRegistry registry = patientVisitService.updateMedicalCertificates("V0001", certificates, principal);
//        assertEquals("1245842", registry.getId());
//    }

//    @Test
//    public void saveDispensingDataMap() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        PatientVisitRegistry savedRegistry = patientVisitService.saveDispensingData("V0002", medicalReference, principal);
//        assertEquals(400, savedRegistry.getMedicalReference().getDispatchItems().get(0).getOriTotalPrice());
//    }
//
//    @Test
//    public void saveConsultationDataMap() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        PatientVisitRegistry savedRegistry = patientVisitService.saveConsultationData("V0001", medicalReference, principal);
//        assertEquals(400, savedRegistry.getMedicalReference().getDispatchItems().get(0).getOriTotalPrice());
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeInitialToPostConsult() throws CMSException {
//        patientVisitService.changeStateToPostConsult("1245842", MockPatientVisitRegistry.mockMedicalReference(), principal);
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeInitialToPayment() throws CMSException {
//        patientVisitService.changeStatusToPayment("1245842", MockPatientVisitRegistry.mockMedicalReference(), principal, Collections.emptyMap());
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeConsultToPayment() throws CMSException {
//        patientVisitService.changeStatusToPayment("V0001", MockPatientVisitRegistry.mockMedicalReference(), principal, Collections.emptyMap());
//    }
//
//    @Test(expected = CMSException.class)
//    public void changePaymentToConsult() throws CMSException {
//        patientVisitService.changeStateToConsult("V0003", "D0001", true);
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeConsultationDataOnInitialState() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        patientVisitService.saveConsultationData("1245842", medicalReference, principal);
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeConsultationDataOnPostConsultState() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        patientVisitService.saveConsultationData("V0002", medicalReference, principal);
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeDispensingDataOnConsultationState() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        patientVisitService.saveDispensingData("V0001", medicalReference, principal);
//    }
//
//    @Test(expected = CMSException.class)
//    public void changeDispensingDataOnPaymentState() throws CMSException {
//        MedicalReference medicalReference = MockPatientVisitRegistry.mockMedicalReferenceTwo();
//        patientVisitService.saveDispensingData("V0003", medicalReference, principal);
//    }
}
