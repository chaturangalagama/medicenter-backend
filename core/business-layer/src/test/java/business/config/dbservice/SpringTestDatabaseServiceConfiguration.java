package business.config.dbservice;

import business.config.repository.SpringTestRepositoryConfiguration;
import com.ilt.cms.database.clinic.system.PostcodeDatabaseService;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.ilt.cms.database.clinic.inventory.SupplierDatabaseService;
import com.ilt.cms.database.clinic.AllergyGroupDatabaseService;
import com.ilt.cms.database.patient.patientVisit.AppointmentDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationDatabaseService;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationFollowupDatabaseService;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationTemplateDatabaseService;
import com.ilt.cms.database.patient.patientVisit.DiagnosisDatabaseService;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.ilt.cms.database.clinic.inventory.ItemDatabaseService;
import com.ilt.cms.database.clinic.LabelDatabaseService;
import com.ilt.cms.database.clinic.NotificationDatabaseService;
import com.ilt.cms.database.patient.MedicalAlertDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.clinic.PatientNoteDatabaseService;
import com.ilt.cms.database.clinic.system.SystemStoreDatabaseService;
import com.ilt.cms.database.clinic.system.TemporaryStoreDatabaseService;
import com.ilt.cms.database.patient.VaccinationDatabaseService;
import com.ilt.cms.database.patient.patientVisit.PatientReferralDatabaseService;
import com.ilt.cms.database.patient.patientVisit.PatientVisitDatabaseService;
import com.ilt.cms.database.patient.patientVisit.VisitPurposeDatabaseService;
import com.ilt.cms.db.service.clinic.*;
import com.ilt.cms.db.service.patient.patientVisit.*;
import com.ilt.cms.db.service.clinic.system.MongoPostcodeDatabaseService;
import com.ilt.cms.db.service.clinic.system.MongoRunningNumberService;
import com.ilt.cms.db.service.clinic.system.MongoSystemStoreDatabaseService;
import com.ilt.cms.db.service.clinic.system.MongoTemporaryStoreDatabaseService;
import com.ilt.cms.db.service.patient.MongoMedicalAlertDatabaseService;
import com.ilt.cms.db.service.patient.MongoPatientDatabaseService;
import com.ilt.cms.db.service.patient.MongoVaccinationDatabaseService;
import com.ilt.cms.db.service.patient.patientVisit.consultation.MongoConsultationDatabaseService;
import com.ilt.cms.db.service.patient.patientVisit.consultation.MongoConsultationFollowupDatabaseService;
import com.ilt.cms.db.service.patient.patientVisit.consultation.MongoConsultationTemplateDatabaseService;
import com.ilt.cms.db.service.clinic.inventory.MongoItemDatabaseService;
import com.ilt.cms.db.service.clinic.inventory.MongoSupplierDatabaseService;
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
    public PatientVisitDatabaseService patientVisitRegistryDatabaseService() {
        return new MongoPatientVisitDatabaseService(repositoryConfiguration.patientVisitRegistryRepository(), repositoryConfiguration.mongoTemplate());
    }

    @Bean
    public AppointmentDatabaseService calendarDatabaseService(){
        return new MongoAppointmentDatabaseService(repositoryConfiguration.appointmentRepository(),
                repositoryConfiguration.clinicCalendarRepository(), repositoryConfiguration.doctorCalendarRepository());
    }
}
