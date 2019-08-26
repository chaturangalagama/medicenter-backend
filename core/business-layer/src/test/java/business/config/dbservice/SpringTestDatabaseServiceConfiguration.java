package business.config.dbservice;

import business.config.repository.SpringTestRepositoryConfiguration;
import com.ilt.cms.database.PostcodeDatabaseService;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.database.SupplierDatabaseService;
import com.ilt.cms.database.allergy.AllergyGroupDatabaseService;
import com.ilt.cms.database.appointment.CalendarDatabaseService;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.consultation.ConsultationDatabaseService;
import com.ilt.cms.database.consultation.ConsultationFollowupDatabaseService;
import com.ilt.cms.database.consultation.ConsultationTemplateDatabaseService;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.diagnosis.DiagnosisDatabaseService;
import com.ilt.cms.database.doctor.DoctorDatabaseService;
import com.ilt.cms.database.item.ItemDatabaseService;
import com.ilt.cms.database.label.LabelDatabaseService;
import com.ilt.cms.database.notification.NotificationDatabaseService;
import com.ilt.cms.database.patient.MedicalAlertDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.patient.PatientNoteDatabaseService;
import com.ilt.cms.database.policyholder.*;
import com.ilt.cms.database.store.SystemStoreDatabaseService;
import com.ilt.cms.database.store.TemporaryStoreDatabaseService;
import com.ilt.cms.database.vaccination.AssociatedCoverageVaccinationDatabaseService;
import com.ilt.cms.database.vaccination.VaccinationDatabaseService;
import com.ilt.cms.database.visit.PatientReferralDatabaseService;
import com.ilt.cms.database.visit.PatientVisitRegistryDatabaseService;
import com.ilt.cms.database.visit.VisitPurposeDatabaseService;
import com.ilt.cms.db.service.*;
import com.ilt.cms.db.service.appointment.MongoCalendarDatabaseService;
import com.ilt.cms.db.service.builder.PatientBuilder;
import com.ilt.cms.db.service.policyholder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SpringTestRepositoryConfiguration.class)
public class SpringTestDatabaseServiceConfiguration{
    @Autowired
    private SpringTestRepositoryConfiguration repositoryConfiguration;

    @Bean
    public PatientBuilder patientBuilder(){
        return new PatientBuilder(repositoryConfiguration.customMedicalCoverageRepository(), repositoryConfiguration.policyHolderRepository());
    }
    @Bean
    public AllergyGroupDatabaseService allergyGroupDatabaseService(){
        return new MongoAllergyGroupDatabaseService(repositoryConfiguration.allergyGroupRepository());
    }
    @Bean
    public LabelDatabaseService labelDatabaseService(){
        return new MongoLabelDatabaseService(repositoryConfiguration.labelRepository());
    }
    @Bean
    public ClinicDatabaseService clinicDatabaseService(){
        return new MongoClinicDatabaseService(repositoryConfiguration.clinicRepository());
    }
    @Bean
    public DoctorDatabaseService doctorDatabaseService(){
        return new MongoDoctorDatabaseService(repositoryConfiguration.doctorRepository());
    }

    @Bean
    public RunningNumberService mongoRunningNumberService(){
        return new MongoRunningNumberService(repositoryConfiguration.mongoTemplate());
    }
    @Bean
    public PatientDatabaseService patientDatabaseService(){
        return new MongoPatientDatabaseService(repositoryConfiguration.patientRepository(), mongoRunningNumberService(), repositoryConfiguration.mongoTemplate(), patientBuilder());
    }
    @Bean
    public ConsultationTemplateDatabaseService consultationTemplateDatabaseService(){
        return new MongoConsultationTemplateDatabaseService(repositoryConfiguration.consultationTemplateRepository());
    }
    @Bean
    public DiagnosisDatabaseService diagnosisDatabaseService(){
        return new MongoDiagnosisDatabaseService(repositoryConfiguration.diagnosisRepository());
    }
    @Bean
    public SupplierDatabaseService supplierDatabaseService(){
        return new MongoSupplierDatabaseService(repositoryConfiguration.supplierRepository());
    }
    @Bean
    public MedicalCoverageDatabaseService medicalCoverageDatabaseService(){
        return new MongoMedicalCoverageDatabaseService(repositoryConfiguration.medicalCoverageRepository(), repositoryConfiguration.customMedicalCoverageRepository());
    }
    @Bean
    public MedicalAlertDatabaseService medicalAlertDatabaseService(){
        return new MongoMedicalAlertDatabaseService(repositoryConfiguration.medicalAlertRepository());
    }
    @Bean
    public NotificationDatabaseService notificationDatabaseService(){
        return new MongoNotificationDatabaseService(repositoryConfiguration.notificationRepository());
    }
    @Bean
    public PatientNoteDatabaseService patientNoteDatabaseService(){
        return new MongoPatientNoteDatabaseService(repositoryConfiguration.patientNoteRepository(), repositoryConfiguration.mongoTemplate());
    }
    @Bean
    public PostcodeDatabaseService postcodeDatabaseService(){
        return new MongoPostcodeDatabaseService(repositoryConfiguration.postcodeRepository());
    }
    @Bean
    public TemporaryStoreDatabaseService temporaryStoreDatabaseService(){
        return new MongoTemporaryStoreDatabaseService(repositoryConfiguration.temporaryStoreRepository());
    }
    @Bean
    public VaccinationDatabaseService vaccinationDatabaseService(){
        return new MongoVaccinationDatabaseService(repositoryConfiguration.vaccinationRepository());
    }
    @Bean
    public AssociatedCoverageVaccinationDatabaseService associatedCoverageVaccinationDatabaseService(){
        return new MongoAssociatedCoverageVaccinationDatabaseService(repositoryConfiguration.associatedCoverageVaccinationRepository());
    }
    @Bean
    public VisitPurposeDatabaseService visitPurposeDatabaseService(){
        return new MongoVisitPurposeDatabaseService(repositoryConfiguration.visitPurposeRepository());
    }
    @Bean
    public PolicyHolderDatabaseService policyHolderDatabaseService(){
        return new MongoPolicyHolderDatabaseService(repositoryConfiguration.policyHolderRepository(),
                chasDatabaseService(), corporateDatabaseService(),
                insuranceDatabaseService(), mediSaveDatabaseService());
    }
    @Bean
    public ChasDatabaseService chasDatabaseService(){
        return new MongoChasDatabaseService(repositoryConfiguration.chasRepository());
    }
    @Bean
    public CorporateDatabaseService corporateDatabaseService(){
        return new MongoCorporateDatabaseService(repositoryConfiguration.corporateRepository(), repositoryConfiguration.mongoTemplate());
    }
    @Bean
    public InsuranceDatabaseService insuranceDatabaseService(){
        return new MongoInsuranceDatabaseService(repositoryConfiguration.insuranceRepository());
    }
    @Bean
    public MediSaveDatabaseService mediSaveDatabaseService(){
        return new MongoMediSaveDatabaseService(repositoryConfiguration.mediSaveRepository());
    }
    @Bean
    public CaseDatabaseService caseDatabaseService() {
        return new MongoCaseDatabaseService(repositoryConfiguration.caseRepository(), repositoryConfiguration.mongoTemplate());
    }

    @Bean
    public ConsultationDatabaseService consultationDatabaseService() {
        return new MongoConsultationDatabaseService(repositoryConfiguration.consultationRepository());
    }

    @Bean
    public ConsultationFollowupDatabaseService consultationFollowupDatabaseService() {
        return new MongoConsultationFollowupDatabaseService(repositoryConfiguration.consultationFollowupRepository());
    }

    @Bean
    public PatientReferralDatabaseService patientReferralDatabaseService() {
        return new MongoPatientReferralDatabaseService(repositoryConfiguration.patientReferralRepository());
    }

    @Bean
    public ItemDatabaseService itemDatabaseService() {
        return new MongoItemDatabaseService(repositoryConfiguration.itemRepository());
    }


    @Bean
    public SystemStoreDatabaseService systemStoreDatabaseService() {
        return new MongoSystemStoreDatabaseService(repositoryConfiguration.systemStoreRepository(), repositoryConfiguration.mongoTemplate());
    }

    @Bean
    public PatientVisitRegistryDatabaseService patientVisitRegistryDatabaseService() {
        return new MongoPatientVisitRegistryDatabaseService(repositoryConfiguration.patientVisitRegistryRepository(), repositoryConfiguration.mongoTemplate());
    }

    @Bean
    public CalendarDatabaseService calendarDatabaseService(){
        return new MongoCalendarDatabaseService(repositoryConfiguration.appointmentRepository(),
                repositoryConfiguration.clinicCalendarRepository(), repositoryConfiguration.doctorCalendarRepository());
    }
}
