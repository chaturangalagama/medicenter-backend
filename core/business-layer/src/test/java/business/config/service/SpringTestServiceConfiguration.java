package business.config.service;

import business.config.dbservice.SpringTestDatabaseServiceConfiguration;
import business.config.repository.SpringTestRepositoryConfiguration;
import com.ilt.cms.pm.business.service.clinic.inventory.ItemService;
import com.ilt.cms.pm.business.service.clinic.inventory.SupplierService;
import com.ilt.cms.pm.business.service.clinic.system.TemporaryStoreService;
import com.ilt.cms.pm.business.service.patient.patientVisit.*;
import com.ilt.cms.pm.business.service.clinic.billing.PriceCalculationService;
import com.ilt.cms.pm.business.service.clinic.billing.SalesOrderService;
import com.ilt.cms.pm.business.service.clinic.*;
import com.ilt.cms.pm.business.service.clinic.billing.LegacyInventoryService;
import com.ilt.cms.pm.business.service.patient.*;
import com.ilt.cms.pm.business.service.clinic.system.PostcodeService;
import com.ilt.cms.pm.business.service.clinic.PatientNoteService;
import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationFollowupService;
import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationService;
import com.ilt.cms.pm.business.service.patient.patientVisit.consultation.ConsultationTemplateService;
import com.ilt.cms.repository.clinic.ClinicGroupItemMasterRepository;
import com.ilt.cms.repository.clinic.ClinicItemMasterRepository;
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
        return new DiagnosisService(serviceConfiguration.diagnosisDatabaseService());
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
                serviceConfiguration.patientDatabaseService());
    }

    @Bean
    public VisitPurposeService visitPurposeService() {
        return new VisitPurposeService(serviceConfiguration.visitPurposeDatabaseService());
    }

    @Bean
    public PatientVisitService patientVisitService() {
        return new PatientVisitService(serviceConfiguration.patientVisitRegistryDatabaseService(),
                serviceConfiguration.patientDatabaseService(),
                serviceConfiguration.clinicDatabaseService(),
                serviceConfiguration.doctorDatabaseService(),
                consultationService(),
                consultationFollowupService(),
                patientReferralService(),
                itemService(),
                serviceConfiguration.mongoRunningNumberService(),
                diagnosisService(), queueService(), priceCalculationService());
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
        return new PriceCalculationService(springTestRepositoryConfiguration.itemRepository(),
                mock(ClinicGroupItemMasterRepository.class), mock(ClinicItemMasterRepository.class),
                springTestRepositoryConfiguration.clinicRepository(),
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
