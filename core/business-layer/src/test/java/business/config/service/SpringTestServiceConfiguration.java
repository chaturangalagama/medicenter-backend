package business.config.service;

import business.config.dbservice.SpringTestDatabaseServiceConfiguration;
import business.config.repository.SpringTestRepositoryConfiguration;
import com.ilt.cms.pm.business.service.appointment.AppointmentService;
import com.ilt.cms.pm.business.service.billing.NewCaseService;
import com.ilt.cms.pm.business.service.billing.PriceCalculationService;
import com.ilt.cms.pm.business.service.billing.SalesOrderService;
import com.ilt.cms.pm.business.service.clinic.*;
import com.ilt.cms.pm.business.service.coverage.MedicalCoverageService;
import com.ilt.cms.pm.business.service.coverage.PolicyHolderLimitService;
import com.ilt.cms.pm.business.service.coverage.PolicyHolderService;
import com.ilt.cms.pm.business.service.doctor.*;
import com.ilt.cms.pm.business.service.inventory.LegacyInventoryService;
import com.ilt.cms.pm.business.service.patient.*;
import com.ilt.cms.pm.business.service.queue.QueueService;
import com.ilt.cms.pm.business.service.util.PostcodeService;
import com.ilt.cms.repository.spring.ClinicGroupItemMasterRepository;
import com.ilt.cms.repository.spring.ClinicItemMasterRepository;
import com.ilt.cms.repository.spring.MedicalCoverageItemRepository;
import com.lippo.cms.util.AWSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Import(SpringTestDatabaseServiceConfiguration.class)
public class SpringTestServiceConfiguration {
    @MockBean
    AWSConfig awsConfig;
    @MockBean
    RestTemplate restTemplate;
    @Autowired
    private SpringTestDatabaseServiceConfiguration serviceConfiguration;
    @Autowired
    private SpringTestRepositoryConfiguration springTestRepositoryConfiguration;

    @Bean
    public PolicyHolderLimitService policyHolderLimitService() {
        return new PolicyHolderLimitService(springTestRepositoryConfiguration.caseRepositoryImpl(),
                springTestRepositoryConfiguration.caseRepository(),
                springTestRepositoryConfiguration.medicalCoverageRepository(),
                springTestRepositoryConfiguration.patientVisitRegistryRepository(),
                springTestRepositoryConfiguration.salesOrderRepository());
    }

    @Bean
    public LabelService labelService() {
        return new LabelService(serviceConfiguration.labelDatabaseService());
    }

    @Bean
    public ClinicService clinicService() {
        return new ClinicService(serviceConfiguration.clinicDatabaseService(), serviceConfiguration.doctorDatabaseService());
    }

    @Bean
    public ConsultationTemplateService consultationTemplateService() {
        return new ConsultationTemplateService(serviceConfiguration.consultationTemplateDatabaseService(), serviceConfiguration.doctorDatabaseService(), serviceConfiguration.patientDatabaseService());
    }

    @Bean
    public PatientService patientService() {
        return new PatientService(serviceConfiguration.mongoRunningNumberService(), serviceConfiguration.patientDatabaseService());
    }

    @Bean
    public AllergyGroupService allergyGroup() {
        return new AllergyGroupService(serviceConfiguration.allergyGroupDatabaseService(), serviceConfiguration.itemDatabaseService(), serviceConfiguration.patientDatabaseService());
    }

    @Bean
    public DiagnosisService diagnosisService() {
        return new DiagnosisService(serviceConfiguration.diagnosisDatabaseService(), serviceConfiguration.medicalCoverageDatabaseService());
    }

    @Bean
    public DoctorService doctorService() {
        return new DoctorService(serviceConfiguration.doctorDatabaseService(), serviceConfiguration.clinicDatabaseService());
    }

    @Bean
    public MedicalAlertService medicalAlertService() {
        return new MedicalAlertService(serviceConfiguration.patientDatabaseService(), serviceConfiguration.medicalAlertDatabaseService());
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationService(serviceConfiguration.notificationDatabaseService());
    }

    @Bean
    public PatientNoteService patientNoteService() {
        return new PatientNoteService(serviceConfiguration.patientNoteDatabaseService(), serviceConfiguration.doctorDatabaseService());
    }

    @Bean
    public PostcodeService postcodeService() {
        return new PostcodeService(serviceConfiguration.postcodeDatabaseService());
    }

    @Bean
    public SupplierService supplierService() {
        return new SupplierService(serviceConfiguration.supplierDatabaseService());
    }

    @Bean
    public TemporaryStoreService temporaryStoreService() {
        return new TemporaryStoreService(serviceConfiguration.temporaryStoreDatabaseService());
    }

    @Bean
    public VaccinationService vaccinationService() {
        return new VaccinationService(serviceConfiguration.vaccinationDatabaseService(),
                serviceConfiguration.patientDatabaseService(),
                serviceConfiguration.associatedCoverageVaccinationDatabaseService(),
                serviceConfiguration.medicalCoverageDatabaseService());
    }

    @Bean
    public VisitPurposeService visitPurposeService() {
        return new VisitPurposeService(serviceConfiguration.visitPurposeDatabaseService());
    }

    @Bean
    public PolicyHolderService policyHolderService() {
        return new PolicyHolderService(serviceConfiguration.policyHolderDatabaseService(), serviceConfiguration.medicalCoverageDatabaseService());
    }

    @Bean
    public MedicalCoverageService medicalCoverageService() {
        return new MedicalCoverageService(serviceConfiguration.medicalCoverageDatabaseService(), serviceConfiguration.policyHolderDatabaseService(),
                serviceConfiguration.clinicDatabaseService(), new ItemService(serviceConfiguration.itemDatabaseService(),
                serviceConfiguration.systemStoreDatabaseService(), serviceConfiguration.clinicDatabaseService()), policyHolderService());
    }

    @Bean
    public PatientVisitService patientVisitService() {
        return new PatientVisitService(serviceConfiguration.patientVisitRegistryDatabaseService(),
                serviceConfiguration.patientDatabaseService(),
                serviceConfiguration.caseDatabaseService(),
                serviceConfiguration.clinicDatabaseService(),
                serviceConfiguration.doctorDatabaseService(),
                consultationService(),
                consultationFollowupService(),
                patientReferralService(),
                itemService(),
                new NewCaseService(springTestRepositoryConfiguration.caseRepository(), springTestRepositoryConfiguration.patientRepository(),
                        springTestRepositoryConfiguration.clinicRepository(), serviceConfiguration.mongoRunningNumberService(),
                        springTestRepositoryConfiguration.patientVisitRegistryRepository(),
                        new SalesOrderService(springTestRepositoryConfiguration.salesOrderRepository(),
                                serviceConfiguration.mongoRunningNumberService(),
                                springTestRepositoryConfiguration.itemRepository()),
                        springTestRepositoryConfiguration.medicalCoverageRepository(),
                        mock(PriceCalculationService.class), mock(PolicyHolderLimitService.class)),
                serviceConfiguration.mongoRunningNumberService(),
                diagnosisService(), medicalCoverageService(), policyHolderService(), queueService(), priceCalculationService());
    }

    @Bean
    public ConsultationService consultationService() {
        return new ConsultationService(serviceConfiguration.consultationDatabaseService(),
                serviceConfiguration.clinicDatabaseService(),
                serviceConfiguration.patientDatabaseService(),
                serviceConfiguration.doctorDatabaseService());
    }

    @Bean
    public QueueService queueService() {
        return new QueueService(springTestRepositoryConfiguration.patientVisitRegistryRepository(), springTestRepositoryConfiguration.clinicRepository(),
                springTestRepositoryConfiguration.visitPurposeRepository(), serviceConfiguration.mongoRunningNumberService());
    }

    @Bean
    public ConsultationFollowupService consultationFollowupService() {
        return new ConsultationFollowupService(serviceConfiguration.consultationFollowupDatabaseService(),
                serviceConfiguration.clinicDatabaseService(),
                serviceConfiguration.doctorDatabaseService(),
                serviceConfiguration.patientDatabaseService());
    }

    @Bean
    public PatientReferralService patientReferralService() {
        return new PatientReferralService(serviceConfiguration.patientReferralDatabaseService());
    }

    @Bean
    public ItemService itemService() {
        return new ItemService(serviceConfiguration.itemDatabaseService(),
                serviceConfiguration.systemStoreDatabaseService(),
                serviceConfiguration.clinicDatabaseService());
    }




    @Bean
    public PriceCalculationService priceCalculationService() {
        return new PriceCalculationService(springTestRepositoryConfiguration.medicalCoverageRepository(),
                mock(MedicalCoverageItemRepository.class), springTestRepositoryConfiguration.itemRepository(),
                mock(ClinicGroupItemMasterRepository.class), mock(ClinicItemMasterRepository.class),
                springTestRepositoryConfiguration.clinicRepository(), springTestRepositoryConfiguration.caseRepository(),
                legacyInventoryService());
    }

    @Bean
    public AppointmentService appointmentService() {
        return new AppointmentService(serviceConfiguration.calendarDatabaseService());
    }

    @Bean
    public FileManagementService fileManagementService(){
        return new FileManagementService(awsConfig, serviceConfiguration.patientVisitRegistryDatabaseService(),
                serviceConfiguration.patientDatabaseService(), serviceConfiguration.clinicDatabaseService());
    }

    @Bean
    public SalesOrderService salesOrderService() {
        return new SalesOrderService(springTestRepositoryConfiguration.salesOrderRepository(),
                serviceConfiguration.mongoRunningNumberService(),
                springTestRepositoryConfiguration.itemRepository());
    }

    @Bean
    public LegacyInventoryService legacyInventoryService(){
        return new LegacyInventoryService(restTemplate);
    }
}