package business.config.dbservice;

import business.config.repository.SpringTestRepositoryConfiguration;
import com.ilt.cms.database.PostcodeDatabaseService;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.database.SupplierDatabaseService;
import com.ilt.cms.database.allergy.AllergyGroupDatabaseService;
import com.ilt.cms.database.appointment.CalendarDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.consultation.ConsultationDatabaseService;
import com.ilt.cms.database.consultation.ConsultationFollowupDatabaseService;
import com.ilt.cms.database.consultation.ConsultationTemplateDatabaseService;
import com.ilt.cms.database.diagnosis.DiagnosisDatabaseService;
import com.ilt.cms.database.doctor.DoctorDatabaseService;
import com.ilt.cms.database.item.ItemDatabaseService;
import com.ilt.cms.database.label.LabelDatabaseService;
import com.ilt.cms.database.notification.NotificationDatabaseService;
import com.ilt.cms.database.patient.MedicalAlertDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.patient.PatientNoteDatabaseService;
import com.ilt.cms.database.store.SystemStoreDatabaseService;
import com.ilt.cms.database.store.TemporaryStoreDatabaseService;
import com.ilt.cms.database.vaccination.VaccinationDatabaseService;
import com.ilt.cms.database.visit.PatientReferralDatabaseService;
import com.ilt.cms.database.visit.PatientVisitRegistryDatabaseService;
import com.ilt.cms.database.visit.VisitPurposeDatabaseService;
import com.ilt.cms.db.service.*;
import com.ilt.cms.db.service.appointment.MongoCalendarDatabaseService;
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
        return new MongoPatientDatabaseService(repositoryConfiguration.patientRepository(), mongoRunningNumberService(), repositoryConfiguration.mongoTemplate());
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
    public VisitPurposeDatabaseService visitPurposeDatabaseService(){
        return new MongoVisitPurposeDatabaseService(repositoryConfiguration.visitPurposeRepository());
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
